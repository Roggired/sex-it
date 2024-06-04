package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.domain.model.BbbMeeting
import ru.sexit.platform.domain.repo.BbbMeetingRepo
import ru.sexit.platform.infrastructure.AlreadyExistException
import ru.sexit.platform.infrastructure.BbbIntegrationException
import ru.sexit.platform.infrastructure.NotFoundException
import ru.sexit.platform.infrastructure.bbb.client.BbbClient
import ru.sexit.platform.infrastructure.bbb.model.BbbCreateMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbJoinMeetingRequest
import ru.sexit.platform.infrastructure.bbb.model.BbbUserRole
import ru.sexit.platform.utils.RequestMode
import ru.sexit.platform.utils.log
import java.io.IOException
import java.util.*

@Service
class BbbMeetingService(
    private val bbbMeetingRepo: BbbMeetingRepo,
    private val bbbClient: BbbClient,
) {
    // TODO: integrate with apps
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createMeeting(
        applicationId: Long,
    ): BbbMeeting {
        val existingBbbMeeting = bbbMeetingRepo.findByApplicationId(applicationId)
        if (existingBbbMeeting != null) {
            throw AlreadyExistException("Meeting already exists")
        }

        val bbbMeeting = bbbMeetingRepo.save(
            BbbMeeting(
                id = 0L,
                uuid = UUID.randomUUID(),
                applicationId = applicationId,
                psychoId = 1L, // TODO: stub
                clientId = 2L, // TODO: stub
            )
        )

        try {
            bbbClient.createMeeting(
                request = BbbCreateMeetingRequest(
                    name = "Consultation between ${bbbMeeting.psychoId} and ${bbbMeeting.clientId}",
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

        log.info("Online consultation has been successfully created for users: ${bbbMeeting.psychoId} and ${bbbMeeting.clientId}. MeetingID: ${bbbMeeting.uuid}")
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

        val joinRequest = when(mode) {
            RequestMode.CLIENT -> BbbJoinMeetingRequest(
                userId = 2L,
                userFullName = "Мария Карасёва",
                meetingId = bbbMeeting.uuid,
                userRole = BbbUserRole.VIEWER,
            )
            RequestMode.PSYCHO -> BbbJoinMeetingRequest(
                userId = 1L,
                userFullName = "Сергей Викторович",
                meetingId = bbbMeeting.uuid,
                userRole = BbbUserRole.MODERATOR,
            )
        }

        return bbbClient.joinMeeting(joinRequest).redirectUrl
    }
}
