package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.applications.*
import ru.sexit.platform.domain.model.*
import ru.sexit.platform.domain.repo.ApplicationRepository
import ru.sexit.platform.infrastructure.NotFoundException
import ru.sexit.platform.utils.RequestMode
import java.time.LocalDateTime

@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository,
    private val slotService: SlotService,
    private val bbbMeetingService: BbbMeetingService
) {
    @Transactional
    fun createApplication(applicationRequest: NewApplicationRequest): ApplicationViewCreated {
        val slot = slotService.getById(applicationRequest.slotId)
        return applicationRepository.save(
            ApplicationEntity(
                id = 0L,
                creationTime = LocalDateTime.now(),
                anonType = applicationRequest.anonType,
                visitType = applicationRequest.visitType,
                description = applicationRequest.description,
                status = SlotStatus.NEED_REVIEW,
                link = null,
                address = null,
                results = null
            ).also { it.slot = slot }
        ).toViewCreated()
    }

    @Transactional
    fun acceptApplication(id: Long) {
        val application =
            applicationRepository.findById(id).orElseThrow { NotFoundException("No such application with id: $id") }
        application.status = SlotStatus.PLANNED
        if(application.visitType == VisitType.ONLINE) {
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

    fun getByPsychoId(psychoId: Long): List<ApplicationView> {
        val applications = applicationRepository.findApplicationsByPsychoId(psychoId)
            ?: throw NotFoundException("No applications by psychoId: $psychoId")
        return applications.map { it.toView() }
    }

    fun getAcceptedApplicationsByPsychoName(psychoName: String): List<AcceptedApplicationView> {
        return applicationRepository.findAcceptedApplicationsByPsychoName(psychoName).map {
            val joinUrl = bbbMeetingService.joinMeeting(
                applicationId = it.id,
                mode = RequestMode.CLIENT,
            )
            it.toAcceptedApplicationView(joinUrl)
        }
    }
}
