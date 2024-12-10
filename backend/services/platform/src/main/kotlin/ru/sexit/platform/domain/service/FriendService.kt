package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.profile.PsychoProfileForFriendshipView
import ru.sexit.platform.api.http.profile.friend.model.FriendProfileView
import ru.sexit.platform.api.http.profile.friend.model.FriendRequest
import ru.sexit.platform.api.http.profile.friend.model.toView
import ru.sexit.platform.api.http.referralprogram.FriendRefersView
import ru.sexit.platform.api.http.referralprogram.ReferralProgramView
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.FriendRepo
import ru.sexit.platform.domain.repo.PsychoProfileRepo
import ru.sexit.platform.infrastructure.exception.AlreadyExistException
import ru.sexit.platform.infrastructure.exception.NotFoundException
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo
import ru.sexit.platform.utils.log

@Service
class FriendService(
    private val friendshipService: FriendshipService,
    private val friendRepo: FriendRepo,
    private val psychoRepo: PsychoProfileRepo,
    private val keycloakAdminAPI: KeycloakAdminAPI,
    @Qualifier("keycloakAdminIntegrationRetrofitClient")
    private val integrationClient: IntegrationRetrofitClient<KeycloakError>,
    private val referralService: ReferralService
) {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createOrUpdateFriend(id: Long, request: FriendRequest): FriendProfileView {
        val keycloakUser = integrationClient.invokeExternalService(
            downstreamService = DownstreamServices.KEYCLOAK,
        ) {
            keycloakAdminAPI.getUserRepresentationById(getRequestAuthorUserInfo().id)
        }.content!!

        if (keycloakUser.email != request.email) {
            integrationClient.invokeExternalService(
                downstreamService = DownstreamServices.KEYCLOAK,
            ) {
                keycloakAdminAPI.updateUser(
                    id = getRequestAuthorUserInfo().id,
                    user = keycloakUser.copy(
                        email = request.email,
                    )
                )
            }
        }

        if (id == 0L) {
            return create(request).toView()
                .also { log.info("Profile (id: ${it.id}) has been created") }
        }

        return update(id, request).toView()
            .also { log.info("Profile (id: $id) has been updated") }
    }

    private fun update(id: Long, request: FriendRequest): FriendProfile {
        val profile = friendRepo.findById(id).orElseThrow { NotFoundException("no such friend") }
        val profileByEmail = friendRepo.findByEmail(request.email)

        if (profileByEmail != null && profile.id != profileByEmail.id) {
            throw AlreadyExistException("Psycho profile with such email already exists")
        }

        with(profile) {
            name = request.name
            email = request.email
            percent = request.percent

        }

        return friendRepo.save(profile)
    }

    fun create(request: FriendRequest): FriendProfile {
        return friendRepo.save(
            FriendProfile(
                0L,
                userId = getRequestAuthorUserInfo().id,
                name = request.name,
                email = request.email,
                percent = request.percent
            )
        )
    }


    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createFriendship(
        psychoId: Long
    ) {
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        friendshipService.createFriendship(
            psychoId = psychoId,
            friendId = friendId
        )
    }

    fun getAvailablePsycho(name: String?, pageNumber: Int, pageSize: Int): Page<PsychoProfileForFriendshipView> {
        val friendId = getMyProfile().id
        return friendshipService.getAllAvailablePsycho(
            name = name,
            friendId = friendId,
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    fun getMyProfile(): FriendProfile {
        return getFriendIdByUserId(getRequestAuthorUserInfo().id)
    }

    fun getFriendIdByUserId(userId: String): FriendProfile {
        return friendRepo.findByUserId(userId) ?: throw NotFoundException("No profile found")
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getFriendshipProjection(pageNumber: Int, pageSize: Int): Page<FriendRefersView> {
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        return friendshipService.getFriendshipProjectionForFriend(
            friendId,
            pageable = PageRequest.of(pageNumber, pageSize)
        ).map { it.toView() }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createReferralProgram(psychoId: Long): Long {
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        return referralService.createReferralProgram(
            friendId = friendId,
            psychoId = psychoId
        )
    }

    fun getFriendsByPsycho(psychoId: Long, pageNumber: Int, pageSize: Int): Page<FriendshipProjectionByPsycho> {
        return friendRepo.getFriendsByPsychoId(psychoId, pageable = PageRequest.of(pageNumber, pageSize))
    }

    fun getReferralProgramByFriend(pageNumber: Int, pageSize: Int): Page<ReferralProgramView> {
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        return referralService.getReferralProgramByFriendId(friendId, pageNumber, pageSize)
    }

}
