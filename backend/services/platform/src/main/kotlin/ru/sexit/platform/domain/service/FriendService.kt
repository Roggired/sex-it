package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.profile.friend.model.FriendProfileView
import ru.sexit.platform.api.http.profile.friend.model.FriendRequest
import ru.sexit.platform.api.http.profile.friend.model.toView
import ru.sexit.platform.api.http.referralprogram.FriendRefersView
import ru.sexit.platform.domain.model.FriendProfile
import ru.sexit.platform.domain.model.Friendship
import ru.sexit.platform.domain.model.PsychoProfileForCatalogueProjection
import ru.sexit.platform.domain.model.toView
import ru.sexit.platform.domain.repo.FriendRepo
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
    private val keycloakAdminAPI: KeycloakAdminAPI,
    @Qualifier("keycloakAdminIntegrationRetrofitClient")
    private val integrationClient: IntegrationRetrofitClient<KeycloakError>,
    private val referralService: ReferralService
) {

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createFriend(request: FriendRequest):FriendProfileView {
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

        return create(request).toView()
    }

    fun create(request: FriendRequest): FriendProfile{
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

    fun getAvailablePsycho(pageNumber: Int, pageSize: Int): Page<PsychoProfileForCatalogueProjection> {
        return friendshipService.getAllAvailablePsycho(
            pageNumber = pageNumber,
            pageSize = pageSize
        )
    }

    fun getFriendIdByUserId(userId: String): FriendProfile {
        return friendRepo.findByUserId(userId) ?: throw NotFoundException("No profile found")
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun getFriendshipProjection(): FriendRefersView? {
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        log.debug(friendId.toString() + " ТУТ")
        return friendshipService.getFriendshipProjectionForFriend(friendId)?.toView()
    }

    fun getFriendship(): Friendship{
        val friendId = getFriendIdByUserId(getRequestAuthorUserInfo().id).id
        return friendshipService.getFriendship(friendId)
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createReferralProgram(): String {
        val friendship = getFriendship()
        return referralService.createReferralProgram(
            friendId = friendship.friendId,
            psychoId = friendship.psychoId
        ).toString() // todo возвращать путь до referralProgramController, где по психу будет урл на его слоты
    }

    @Transactional
    fun getFriendshipProjectionForPsycho(){

    }
}