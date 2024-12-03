package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.applications.*
import ru.sexit.platform.api.http.slot.SlotWithDate
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.ApplicationRepository
import ru.sexit.platform.infrastructure.exception.InvalidDataException
import ru.sexit.platform.infrastructure.exception.InvalidOperationException
import ru.sexit.platform.infrastructure.exception.NotFoundException
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo
import ru.sexit.platform.utils.RequestMode
import java.time.LocalDateTime

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val slotService: SlotService,
    private val bbbMeetingService: BbbMeetingService,
    private val keycloakAdminAPI: KeycloakAdminAPI,
    @Qualifier("keycloakAdminIntegrationRetrofitClient")
    private val integrationClient: IntegrationRetrofitClient<KeycloakError>,
) {
    @Autowired
    @Lazy
    lateinit var subscriptionService: SubscriptionService

    @Transactional
    fun createApplication(applicationRequest: NewApplicationRequest): Application {
        val slot = slotService.getById(applicationRequest.slotId)
        return applicationRepository.save(
            Application(
                id = 0L,
                creationTime = LocalDateTime.now(),
                anonType = applicationRequest.anonType,
                visitType = applicationRequest.visitType,
                description = applicationRequest.description,
                status = SlotStatus.NEED_REVIEW,
                link = null,
                address = null,
                results = null,
                note = null,
                userId = getRequestAuthorUserInfo().id,
            ).also { it.slot = slot }
        )
    }

    @Transactional
    fun acceptApplication(id: Long) {
        val application =
            applicationRepository.findById(id).orElseThrow { NotFoundException("No such application with id: $id") }
        application.status = SlotStatus.PLANNED
        if(application.visitType == VisitType.ONLINE) {
            val currentSubscription = subscriptionService.getCurrentSubscription()
            if (currentSubscription.current == null || currentSubscription.usageStats == null) {
                throw InvalidOperationException("NO_ACTIVE_SUBSCRIPTION")
            }

            if (currentSubscription.usageStats.used >= currentSubscription.usageStats.max) {
                throw InvalidOperationException("LIMIT_REACHED")
            }

            bbbMeetingService.createMeeting(application.id)
            application.link = null
        }
    }

    @Transactional
    fun rejectApplication(id: Long) {
        val application =
            applicationRepository.findById(id).orElseThrow { NotFoundException("No such application with id: $id") }
        application.status = SlotStatus.REJECTED
    }

    fun getByPsychoId(psychoId: Long): List<ApplicationWithClientView> {
        val applications = applicationRepository.findApplicationsByPsychoId(psychoId)
            ?: throw NotFoundException("No applications by psychoId: $psychoId")

        val users = applications.map {
            integrationClient.invokeExternalService(
                downstreamService = DownstreamServices.KEYCLOAK,
            ) {
                keycloakAdminAPI.getUserRepresentationById(it.userId)
            }.content!!
        }.associateBy { it.id }

        return applications.map {
            ApplicationWithClientView(
                id = it.id,
                clientName = users[it.userId]!!.fullName,
                slot = SlotWithDate(
                    id = it.slot.id,
                    time = it.slot.time,
                    monthId = it.slot.monthId,
                    dayId = it.slot.dayId,
                    yearId = it.slot.yearId,
                ),
                creationTime = it.creationTime,
                anonType = it.anonType,
                visitType = it.visitType,
                status = it.status,
                description = it.description,
                link = it.link,
                address = it.address,
            )
        }
    }

    fun getById(id: Long): Application = applicationRepository.findById(id).orElseThrow { NotFoundException("No such application with id: $id") }

    fun getAcceptedApplications(psychoName: String?, appStatus: SlotStatus?): List<AcceptedApplicationView> {
        if (appStatus != null && appStatus != SlotStatus.NEED_REVIEW && appStatus != SlotStatus.PLANNED && appStatus != SlotStatus.REJECTED) {
            throw InvalidDataException("appStatus param should have one following values: NEED_REVIEW, PLANNED, REJECTED")
        }

        val appStatus1 = appStatus ?: SlotStatus.PLANNED
        return applicationRepository.findAcceptedApplicationsByPsychoNameAndAppStatus1AndAppStatus2(
            psychoName = psychoName,
            appStatus1 = appStatus1,
            appStatus2 = if (appStatus1 == SlotStatus.PLANNED) SlotStatus.DONE else null
        ).map {
            val joinUrl = if (appStatus == SlotStatus.PLANNED) {
                bbbMeetingService.joinMeeting(
                    applicationId = it.id,
                    mode = RequestMode.CLIENT,
                )
            } else null
            it.toAcceptedApplicationView(joinUrl)
        }
    }

    fun countOnlineFinishedApplicationsForMonth(
        yearId: Int,
        monthId: Int,
        psychoId: Long
    ): Int = applicationRepository.countBySlotPsychoProfileIdAndStatusAndVisitTypeAndSlotYearIdAndSlotMonthId(
        psychoId = psychoId,
        status = SlotStatus.DONE,
        visitType = VisitType.ONLINE,
        yearId = yearId,
        monthId = monthId,
    )

    fun countOnlinePlannedApplicationsForMonth(
        yearId: Int,
        monthId: Int,
        psychoId: Long
    ): Int = applicationRepository.countBySlotPsychoProfileIdAndStatusAndVisitTypeAndSlotYearIdAndSlotMonthId(
        psychoId = psychoId,
        status = SlotStatus.PLANNED,
        visitType = VisitType.ONLINE,
        yearId = yearId,
        monthId = monthId,
    )

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun patchApplicationNote(applicationId: Long, request: NoteRequest) {
        val psychoId = getRequestAuthorUserInfo().id
        val application = getById(applicationId)

        if (psychoId != application.slot.psychoProfile.userId) {
            throw InvalidOperationException("Psycho can create notes only for applications points to him")
        }

        application.note = request.note
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun finishApplication(applicationId: Long, request: NoteRequest) {
        val psychoId = getRequestAuthorUserInfo().id
        val application = getById(applicationId)

        if (psychoId != application.slot.psychoProfile.userId) {
            throw InvalidOperationException("Psycho can create notes only for applications points to him")
        }

        application.status = SlotStatus.DONE
        application.results = request.note
    }
}
