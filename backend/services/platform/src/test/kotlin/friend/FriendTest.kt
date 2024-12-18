package friend

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.keycloak.common.util.Time
import org.springframework.data.domain.Page
import ru.sexit.platform.api.http.profile.PsychoProfileForFriendshipView
import ru.sexit.platform.api.http.profile.friend.model.FriendRequest
import ru.sexit.platform.domain.model.FriendProfile
import ru.sexit.platform.domain.repo.FriendRepo
import ru.sexit.platform.domain.repo.PsychoProfileRepo
import ru.sexit.platform.domain.service.FriendService
import ru.sexit.platform.domain.service.FriendshipService
import ru.sexit.platform.domain.service.ReferralService
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakUserRepresentation
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitResponse
import ru.sexit.platform.infrastructure.security.KeycloakRoles

class FriendServiceTest : DescribeSpec({

    val friendRepo = mockk<FriendRepo>()
    val psychoRepo = mockk<PsychoProfileRepo>()
    val keycloakAdminAPI = mockk<KeycloakAdminAPI>()
    val integrationClient = mockk<IntegrationRetrofitClient<KeycloakError>>()
    val referralService = mockk<ReferralService>()
    val friendshipService = mockk<FriendshipService>()

    val friendService = FriendService(
        friendshipService,
        friendRepo,
        psychoRepo,
        keycloakAdminAPI,
        integrationClient,
        referralService
    )

    describe("FriendService tests") {

        context("success creating/updating profile") {
            val userId = "1"
            val request = FriendRequest("Маша Карасёва", "masha@example.com", 50)
            val existingProfile = FriendProfile(1L, userId, "Маша Карасёва", "masha@example.com", 50)
            val mockUserRepresentation = KeycloakUserRepresentation(
                id = userId,
                email = "masha@example.com",
                firstName = "Маша",
                lastName = "Карасёва",
                realmRoles = listOf(KeycloakRoles.PSYCHO_FRIEND.toString()),
                username = "masha",
                enabled = true,
                emailVerified = true,
                createdTimestamp = Time.currentTime().toLong(),
                attributes = null
            )

            beforeEach {
                val mockResponse = mockk<IntegrationRetrofitResponse<KeycloakUserRepresentation, KeycloakError>>()
                every {
                    integrationClient.invokeExternalService<KeycloakUserRepresentation>(
                        DownstreamServices.KEYCLOAK,
                        any()
                    )
                } returns mockResponse
                every { mockResponse.content } returns mockUserRepresentation
                every { friendRepo.save(any()) } returns existingProfile
            }

            it("should create a new friend profile") {
                val result = friendService.createOrUpdateFriend(0L, request)

                result.name shouldBe "Маша Карасёва"
                result.email shouldBe "masha@example.com"
                result.percent shouldBe 50

                verify { friendRepo.save(any()) }
            }
        }
        context("when getting available psycho profiles") {
            val userId = "1"
            val pageNumber = 0
            val pageSize = 10
            val name = "Маша Карасёва"
            val friendId = 1L
            val page = mockk<Page<PsychoProfileForFriendshipView>>()
            val existingProfile = FriendProfile(1L, userId, name, "masha@example.com", 50)


            beforeEach {
                every { friendService.getMyProfile() } returns existingProfile
                every { friendshipService.getAllAvailablePsycho(name, friendId, pageNumber, pageSize) } returns page
            }

            it("should get available psycho profiles") {
                val result = friendService.getAvailablePsycho(name, pageNumber, pageSize)
                result shouldBe page
                verify { friendshipService.getAllAvailablePsycho(name, friendId, pageNumber, pageSize) }
            }
        }
    }
})
