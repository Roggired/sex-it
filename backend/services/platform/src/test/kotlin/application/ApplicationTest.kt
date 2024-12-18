package application

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.ApplicationRepository
import ru.sexit.platform.domain.repo.ReferralProgramRepo
import ru.sexit.platform.domain.service.ApplicationService
import ru.sexit.platform.domain.service.ReferralService
import ru.sexit.platform.domain.service.SlotService
import ru.sexit.platform.api.http.applications.NewApplicationRequest
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminAPI
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionStatus
import ru.sexit.platform.api.http.applications.AnonType
import ru.sexit.platform.api.http.applications.VisitType
import ru.sexit.platform.domain.model.SlotStatus
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError
import java.time.LocalDateTime

class ApplicationServiceTest : DescribeSpec({

    val applicationRepository: ApplicationRepository = mockk()
    val slotService: SlotService = mockk()
    val referralService: ReferralService = mockk()
    val keycloakAdminAPI: KeycloakAdminAPI = mockk()
    val referralProgramRepo: ReferralProgramRepo = mockk()
    val platformTransactionManager: PlatformTransactionManager = mockk()
    val integrationClient: IntegrationRetrofitClient<KeycloakError> = mockk()

    val applicationService = ApplicationService(
        applicationRepository = applicationRepository,
        slotService = slotService,
        bbbMeetingService = mockk(),
        keycloakAdminAPI = keycloakAdminAPI,
        referralService = referralService,
        integrationClient = integrationClient,
        friendService = mockk(),
        platformTransactionManager = platformTransactionManager,
        referralProgramRepo = referralProgramRepo
    )

    describe("Application service test (create application with and without referId)") {

        context("valid application for save with referId") {
            val slotId = 1L
            val referId = 2L


            val newApplicationRequest = NewApplicationRequest(
                slotId = slotId,
                anonType = AnonType.ANON,
                visitType = VisitType.ONLINE,
                description = "помогите спасите умоляю я хочу отчислиться"
            )


            val slot = mockk<Slot>()
            val savedApplication = Application(
                id = 1L,
                creationTime = LocalDateTime.now(),
                anonType = AnonType.ANON,
                visitType = VisitType.ONLINE,
                status = SlotStatus.NEED_REVIEW,
                description = "помогите спасите умоляю я хочу отчислиться",
                link = null,
                address = null,
                results = null,
                note = null,
                userId = "1masha",
                referId = referId,
                friendName = null,
                friendId = null
            )
            val transactionStatus: TransactionStatus = mockk()
            val savedReferral = ReferralProgram(
                applicationId = 0,
                friendId = 1L,
                psychoId = 1L,
                id = 2L,
                status = ReferralProgramStatus.CREATED.toString(),
                paidStatus = PaidStatus.NOT_PAID.toString()
            )
            val referralProgramProjection = mockk<ReferralProgramForApplicationProjection>()

            every { referralProgramProjection.name } returns "Маша"
            every { referralProgramProjection.referId } returns referId
            every { referralProgramProjection.friendId } returns 1L

            beforeEach {
                every { slotService.getById(slotId) } returns slot
                every { applicationRepository.save(any()) } returns savedApplication
                every { referralService.getReferralProgramById(referId) } returns savedReferral
                every { referralService.getReferralProjectionByApplicationId(any()) } returns referralProgramProjection
                every { platformTransactionManager.getTransaction(any()) } returns transactionStatus
                every { platformTransactionManager.commit(transactionStatus) } just Runs
                every { platformTransactionManager.rollback(transactionStatus) } just Runs
            }

            it("application should be created") {

                val application = applicationService.createApplication(newApplicationRequest, referId)

                application.id shouldBe 1L
                application.status shouldBe SlotStatus.NEED_REVIEW
                application.anonType shouldBe newApplicationRequest.anonType
                application.visitType shouldBe newApplicationRequest.visitType
                application.description shouldBe newApplicationRequest.description
                application.referId shouldBe referId
            }

            it("referralService should be called because of having referId") {
                verify { referralService.getReferralProgramById(referId) }
            }
        }

        context("valid application for save without referId") {
            val slotId = 1L
            val newApplicationRequest = NewApplicationRequest(
                slotId = slotId,
                anonType = AnonType.ANON,
                visitType = VisitType.ONLINE,
                description = "помогите спасите"
            )

            val slot = mockk<Slot>()
            val savedApplication = Application(
                id = 1L,
                creationTime = LocalDateTime.now(),
                anonType = AnonType.ANON,
                visitType = VisitType.ONLINE,
                status = SlotStatus.NEED_REVIEW,
                description = "помогите спасите",
                link = null,
                address = null,
                results = null,
                note = null,
                userId = "masha1",
                referId = null,
                friendName = null,
                friendId = null
            )
            val transactionStatus: TransactionStatus = mockk()

            beforeEach {
                every { slotService.getById(slotId) } returns slot
                every { applicationRepository.save(any()) } returns savedApplication
                every { platformTransactionManager.getTransaction(any()) } returns transactionStatus
                every { platformTransactionManager.commit(transactionStatus) } just Runs
                every { platformTransactionManager.rollback(transactionStatus) } just Runs
            }

            it("application should be created without referId") {

                val application = applicationService.createApplication(newApplicationRequest, null)

                application.id shouldBe 1L
                application.status shouldBe SlotStatus.NEED_REVIEW
                application.anonType shouldBe newApplicationRequest.anonType
                application.visitType shouldBe newApplicationRequest.visitType
                application.description shouldBe newApplicationRequest.description
                application.referId shouldBe null
            }
        }
    }
})
