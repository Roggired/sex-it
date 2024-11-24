package ru.sexit.platform.api.http.slot

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.domain.service.SlotService
import ru.sexit.platform.utils.RequestMode

@RestController
@RequestMapping("/api/v1/slots")
class SlotController(
    private val slotService: SlotService,
) {
    @PostMapping
    fun createSlot(
        @RequestBody @Validated request: SlotRequest,
    ): SlotShortView = slotService.createSlot(request).toShortView()

    @DeleteMapping("/{id}")
    fun deleteSlot(
        @PathVariable("id") id: Long,
    ): Unit = slotService.deleteSlot(id)

    @GetMapping("/by-month")
    fun getAllSlotsByMonth(
        @RequestParam(required = false) yearId: Int?,
        monthId: Int,
        mode: RequestMode,
        @RequestParam(required = true) psychoId: Long
    ): List<SlotMonthView> = slotService.getAllSlotsByMonth(
        yearId = yearId ?: 2024,
        monthId = monthId,
        mode = mode,
        psychoProfileId = psychoId,
    )

    @GetMapping("/by-day")
    fun getAllSlotsByDay(
        @RequestParam(required = false) yearId: Int?,
        monthId: Int,
        dayId: Int,
        mode: RequestMode,
        @RequestParam(required = true) psychoId: Long
    ): List<SlotDayView> = slotService.getAllSlotsByDay(
        yearId = yearId ?: 2024,
        monthId = monthId,
        dayId = dayId,
        mode = mode,
        psychoId = psychoId,
    )
}
