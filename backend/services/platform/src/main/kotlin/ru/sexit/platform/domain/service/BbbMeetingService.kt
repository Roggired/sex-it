package ru.sexit.platform.domain.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.domain.model.BbbMeeting
import ru.sexit.platform.domain.repo.BbbMeetingRepo
import ru.sexit.platform.infrastructure.exception.AlreadyExistException
import ru.sexit.platform.infrastructure.exception.BbbIntegrationException
import ru.sexit.platform.infrastructure.exception.InvalidOperationException
import ru.sexit.platform.infrastructure.exception.NotFoundException
import ru.sexit.platform.infrastructure.integration.bbb.client.BbbClient
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbCreateMeetingRequest
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbJoinMeetingRequest
import ru.sexit.platform.infrastructure.integration.bbb.model.BbbUserRole
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo
import ru.sexit.platform.utils.RequestMode
import ru.sexit.platform.utils.log
import java.io.IOException
import java.util.*

@Service
class BbbMeetingService(
    private val bbbMeetingRepo: BbbMeetingRepo,
    private val bbbClient: BbbClient,
) {
    @Suppress("VarCouldBeVal")
    @Lazy
    @field:Autowired
    private lateinit var applicationService: ApplicationService

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createMeeting(
        applicationId: Long,
    ): BbbMeeting {
        val application = applicationService.getById(applicationId)

        val existingBbbMeeting = bbbMeetingRepo.findByApplicationId(applicationId)
        if (existingBbbMeeting != null) {
            throw AlreadyExistException("Meeting already exists")
        }

        val bbbMeeting = bbbMeetingRepo.save(
            BbbMeeting(
                id = 0L,
                uuid = UUID.randomUUID(),
                applicationId = applicationId,
                psychoProfileId = application.slot.psychoProfile.id,
            )
        )

        try {
            bbbClient.createMeeting(
                request = BbbCreateMeetingRequest(
                    name = "Consultation",
                    meetingId = bbbMeeting.uuid,
                )
            )
        } catch (e: IOException) {
            rollbackBbbMeetingLocally(bbbMeeting)
            throw e
        } catch (e: BbbIntegrationException) {
            rollbackBbbMeetingLocally(bbbMeeting)
            throw e
        }

        log.info("Online consultation has been successfully created for PsychoProfile: ${bbbMeeting.psychoProfileId} and Application: ${bbbMeeting.applicationId}. MeetingID: ${bbbMeeting.uuid}")
        return bbbMeeting
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun deleteMeeting(
        applicationId: Long,
    ) {
        val existingBbbMeeting = getMeetingByApplicationId(applicationId)
        bbbMeetingRepo.delete(existingBbbMeeting)
    }

    private fun getMeetingByApplicationId(applicationId: Long): BbbMeeting =
        bbbMeetingRepo.findByApplicationId(applicationId)
            ?: throw NotFoundException("Meeting not found")

    private fun rollbackBbbMeetingLocally(bbbMeeting: BbbMeeting) {
        try {
            bbbMeetingRepo.deleteById(bbbMeeting.id)
        } catch (e: Exception) {
            log.error("Cannot rollback BBB meeting creation at local db -- PANIC!!!", e)
            throw e
        }
    }

    fun joinMeeting(
        applicationId: Long,
        mode: RequestMode,
    ): String {
        val bbbMeeting = getMeetingByApplicationId(applicationId)
        val application = applicationService.getById(applicationId)

        val joinRequest = when(mode) {
            RequestMode.CLIENT -> if (application.userId == getRequestAuthorUserInfo().id) {
                BbbJoinMeetingRequest(
                    userId = application.userId,
                    userFullName = "Мария Карасёва",
                    meetingId = bbbMeeting.uuid,
                    userRole = BbbUserRole.VIEWER,
                )
            } else throw InvalidOperationException("Request author is not the client")
            RequestMode.PSYCHO -> if (application.slot.psychoProfile.userId == getRequestAuthorUserInfo().id) {
                BbbJoinMeetingRequest(
                    userId = application.slot.psychoProfile.userId,
                    userFullName = "Сергей Викторович",
                    meetingId = bbbMeeting.uuid,
                    userRole = BbbUserRole.MODERATOR,
                )
            } else throw InvalidOperationException("Request author is not the psycho")
        }

        return bbbClient.joinMeeting(joinRequest).redirectUrl
    }
}
