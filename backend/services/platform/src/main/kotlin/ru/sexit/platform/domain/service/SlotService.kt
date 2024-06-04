package ru.sexit.platform.domain.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.slot.SlotDayView
import ru.sexit.platform.api.http.slot.SlotMonthView
import ru.sexit.platform.api.http.slot.SlotRequest
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.Slot
import ru.sexit.platform.domain.model.SlotStatus
import ru.sexit.platform.domain.repo.SlotRepo
import ru.sexit.platform.infrastructure.AlreadyExistException
import ru.sexit.platform.infrastructure.InvalidDataException
import ru.sexit.platform.infrastructure.NotFoundException
import ru.sexit.platform.utils.RequestMode
import ru.sexit.platform.utils.log

@Service
class SlotService(
    private val slotRepo: SlotRepo,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun createSlot(request: SlotRequest): Slot {
        request.validateDate()
        val isSlotCaptured = slotRepo.isSlotAlreadyCaptured(
            yearId = request.yearId,
            monthId = request.monthId,
            dayId = request.dayId,
            timeFrom = request.time.minusHours(1),
            timeTo = request.time.plusHours(1),
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
                psychoProfile = PsychoProfile.stub(1L) // TODO only for arch
            }
        ).also { log.info("New slot ${it.dayId + 1}.${it.monthId + 1}.${it.yearId} ${it.time} has been created") }
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
        psychoId: Long = 1L,
    ): List<SlotMonthView> {
        if (monthId < 0 || monthId > 11) {
            throw InvalidDataException("Invalid monthId")
        }

        return when(mode) {
            RequestMode.CLIENT -> slotRepo.findAllSlotsByMonthOrDayForClient(
                yearId = yearId,
                monthId = monthId,
                psychoId = psychoId
            ).map {
                SlotMonthView(
                    id = it.id,
                    time = it.time,
                    status = SlotStatus.EMPTY,
                )
            }
            RequestMode.PSYCHO -> slotRepo.findAllSlotsByMonthOrDayForPsycho(
                yearId = yearId,
                monthId = monthId,
                psychoId = psychoId,
            ).map {
                SlotMonthView(
                    id = it.id,
                    time = it.time,
                    status = SlotStatus.EMPTY,
                )
            }
        }
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
            ).map {
                SlotDayView(
                    id = it.id,
                    time = it.time,
                    status = SlotStatus.EMPTY,
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
                SlotDayView(
                    id = it.id,
                    time = it.time,
                    status = SlotStatus.EMPTY,
                    anonType = null,
                    visitType = null,
                    description = null,
                    link = null,
                    address = null,
                )
            }
        }
    }
}
