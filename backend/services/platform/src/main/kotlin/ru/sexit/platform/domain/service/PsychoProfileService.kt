package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.profile.FilterPsychoRequest
import ru.sexit.platform.api.http.profile.PsychoProfileRequest
import ru.sexit.platform.api.http.referralprogram.PsychoRefersView
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.FeedbackRepo
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
class PsychoProfileService(
    private val psychoProfileRepo: PsychoProfileRepo,
    private val feedbackRepo: FeedbackRepo,
    private val friendService: FriendService,

    private val keycloakAdminAPI: KeycloakAdminAPI,
    @Qualifier("keycloakAdminIntegrationRetrofitClient")
    private val integrationClient: IntegrationRetrofitClient<KeycloakError>,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateOrCreate(id: Long, request: PsychoProfileRequest): PsychoProfile {
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
            return create(request)
                .also { log.info("Profile (id: ${it.id}) has been created") }
        }

        return update(id, request)
            .also { log.info("Profile (id: $id) has been updated") }
    }

    private fun create(request: PsychoProfileRequest): PsychoProfile {
        val existedProfile = psychoProfileRepo.findByEmail(request.email)
        if (existedProfile != null) {
            throw AlreadyExistException("Psycho profile with such email already exists")
        }

        return psychoProfileRepo.save(
            PsychoProfile(
                id = 0L,
                userId = getRequestAuthorUserInfo().id,
                name = request.name,
                email = request.email,
                price = request.price,
                isFirstFree = request.isFirstFree,
                bio = request.bio,
            )
        )
    }

    private fun update(id: Long, request: PsychoProfileRequest): PsychoProfile {
        val profile = getProfileById(id)
        val profileByEmail = psychoProfileRepo.findByEmail(request.email)

        if (profileByEmail != null && profile.id != profileByEmail.id) {
            throw AlreadyExistException("Psycho profile with such email already exists")
        }

        with(profile) {
            name = request.name
            email = request.email
            price = request.price
            isFirstFree = request.isFirstFree
            bio = request.bio
        }

        return psychoProfileRepo.save(profile)
    }

    fun getProfileById(id: Long): PsychoProfile = psychoProfileRepo.findById(id)
        .orElseThrow { NotFoundException("No such profile exists") }

    fun getMyProfile(): PsychoProfile = psychoProfileRepo.findByUserId(
        getRequestAuthorUserInfo().id
    ) ?: throw NotFoundException("No profile found")

    fun getPsychoRatings(id: Long): PsychoRating {
        val feedbacks = feedbackRepo.findAllByPsychoProfileId(id)
        val number = feedbacks.size

        if (number == 0) {
            return PsychoRating(
                rating = 0.0,
                feedbacks = emptyList(),
            )
        }

        return PsychoRating(
            rating = feedbacks.sumOf { it.rating }.toDouble() / number,
            feedbacks = feedbacks,
        )
    }

    fun filterPsychoProfiles(
        request: FilterPsychoRequest,
        pageNumber: Int,
        pageSize: Int
    ): Page<PsychoProfileForCatalogueProjection> =
        psychoProfileRepo.findPagedByFilters(
            name = request.filters.name,
            priceFrom = request.filters.priceFrom,
            priceTo = request.filters.priceTo,
            minRating = request.filters.minRating,
            pageable = PageRequest.of(pageNumber, pageSize)
        )

    fun getFriendshipProjectionForPsycho(pageNumber: Int, pageSize: Int): Page<PsychoRefersView>{
        val psychoId = getMyProfile().id
        return friendService.getFriendsByPsycho(psychoId, pageNumber,pageSize).map { it.toView() }
    }
}
