package ru.sexit.platform.domain.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.slot.SlotDayView
import ru.sexit.platform.api.http.slot.SlotMonthView
import ru.sexit.platform.api.http.slot.SlotRequest
import ru.sexit.platform.domain.model.Slot
import ru.sexit.platform.domain.model.SlotStatus
import ru.sexit.platform.domain.repo.SlotRepo
import ru.sexit.platform.infrastructure.exception.AlreadyExistException
import ru.sexit.platform.infrastructure.exception.InternalServerException
import ru.sexit.platform.infrastructure.exception.InvalidDataException
import ru.sexit.platform.infrastructure.exception.NotFoundException
import ru.sexit.platform.utils.RequestMode
import ru.sexit.platform.utils.log

@Service
class SlotService(
    private val bbbMeetingService: BbbMeetingService,
    private val slotRepo: SlotRepo,
    private val psychoProfileService: PsychoProfileService,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createSlot(request: SlotRequest): Slot {
        request.validateDate()
        val psychoProfile = psychoProfileService.getMyProfile()
        val isSlotCaptured = slotRepo.isSlotAlreadyCaptured(
            yearId = request.yearId,
            monthId = request.monthId,
            dayId = request.dayId,
            timeFrom = request.time.minusHours(1),
            timeTo = request.time.plusHours(1),
            psychoProfileId = psychoProfile.id
        )

        if (isSlotCaptured) {
            throw AlreadyExistException("Slot overlaps an existing one")
        }

        return slotRepo.save(
            Slot(
                id = 0L,
                yearId = request.yearId,
                monthId = request.monthId,
                dayId = request.dayId,
                time = request.time,
            ).apply {
                this.psychoProfile = psychoProfile
            }
        ).also { log.info("New slot ${it.dayId + 1}.${it.monthId + 1}.${it.yearId} ${it.time} has been created for psycho profile: ${it.psychoProfile.id}") }
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun deleteSlot(id: Long) {
        val slot = getById(id)
        slotRepo.delete(slot)
    }

    fun getById(id: Long): Slot =
        slotRepo.findByIdOrNull(id) ?: throw NotFoundException("Slot doesn't exist")

    fun getAllSlotsByMonth(
        yearId: Int = 2024,
        monthId: Int,
        mode: RequestMode,
        psychoProfileId: Long = 1L,
    ): List<SlotMonthView> {
        if (monthId < 0 || monthId > 11) {
            throw InvalidDataException("Invalid monthId")
        }

        return when(mode) {
            RequestMode.CLIENT -> slotRepo.findAllSlotsByMonthOrDayForClient(
                yearId = yearId,
                monthId = monthId,
                psychoId = psychoProfileId
            ).map {
                SlotMonthView(
                    id = it.id,
                    time = it.time,
                    status = calcStatusForSlot(it),
                    dayId = it.dayId,
                )
            }.filter { it.status == SlotStatus.EMPTY }
            RequestMode.PSYCHO -> slotRepo.findAllSlotsByMonthOrDayForPsycho(
                yearId = yearId,
                monthId = monthId,
                psychoId = psychoProfileId,
            ).map {
                SlotMonthView(
                    id = it.id,
                    time = it.time,
                    status = calcStatusForSlot(it),
                    dayId = it.dayId,
                )
            }
        }
    }

    private fun calcStatusForSlot(slot: Slot): SlotStatus {
        val numberOfPlanned = slot.applications.count { it.status == SlotStatus.PLANNED }
        val numberOfDone = slot.applications.count { it.status == SlotStatus.DONE }
        val numberOfNeedReview = slot.applications.count { it.status == SlotStatus.NEED_REVIEW }

        if (numberOfDone > 0) return SlotStatus.DONE
        if (numberOfPlanned > 0) return SlotStatus.PLANNED
        if (numberOfNeedReview > 0) return SlotStatus.NEED_REVIEW

        return SlotStatus.EMPTY
    }

    fun getAllSlotsByDay(
        yearId: Int = 2024,
        monthId: Int,
        dayId: Int,
        mode: RequestMode,
        psychoId: Long = 1L,
    ): List<SlotDayView> {
        if (monthId < 0 || monthId > 11) {
            throw InvalidDataException("Invalid monthId")
        }

        if (dayId < 0 || dayId > 30) {
            throw InvalidDataException("Invalid dayId")
        }

        return when(mode) {
            RequestMode.CLIENT -> slotRepo.findAllSlotsByMonthOrDayForClient(
                yearId = yearId,
                monthId = monthId,
                dayId = dayId,
                psychoId = psychoId
            ).filter { calcStatusForSlot(it) == SlotStatus.EMPTY }
                .map {
                    SlotDayView(
                        id = it.id,
                        time = it.time,
                        status = SlotStatus.EMPTY,
                        applicationId = null,
                        anonType = null,
                        visitType = null,
                        description = null,
                        link = null,
                        address = null,
                    )
                }
            RequestMode.PSYCHO -> slotRepo.findAllSlotsByMonthOrDayForPsycho(
                yearId = yearId,
                monthId = monthId,
                dayId = dayId,
                psychoId = psychoId,
            ).map {
                val status = calcStatusForSlot(it)
                if (status == SlotStatus.NEED_REVIEW || status == SlotStatus.EMPTY) {
                    SlotDayView(
                        id = it.id,
                        time = it.time,
                        status = status,
                        applicationId = null,
                        anonType = null,
                        visitType = null,
                        description = null,
                        link = null,
                        address = null,
                    )
                } else {
                    val targetApplication = it.applications.firstOrNull { app -> app.status == status }
                        ?: throw InternalServerException("O_o")
                    SlotDayView(
                        id = it.id,
                        time = it.time,
                        status = status,
                        applicationId = targetApplication.id,
                        anonType = targetApplication.anonType,
                        visitType = targetApplication.visitType,
                        description = targetApplication.description,
                        link = bbbMeetingService.joinMeeting(
                            applicationId = targetApplication.id,
                            mode = RequestMode.PSYCHO,
                        ),
                        address = targetApplication.address,
                    )
                }
            }
        }
    }
}
