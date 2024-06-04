package ru.sexit.platform.api.http.slot

import ru.sexit.platform.domain.model.Slot
import ru.sexit.platform.domain.model.SlotStatus
import java.time.LocalTime

data class SlotShortView(
    val id: Long,
    val time: LocalTime,
)

fun Slot.toShortView(): SlotShortView = SlotShortView(
    id = id,
    time = time,
)

data class SlotMonthView(
    val id: Long,
    val time: LocalTime,
    val status: SlotStatus,
)

data class SlotDayView(
    val id: Long,
    val time: LocalTime,
    val anonType: String?, // TODO enum
    val visitType: String?, // TODO enum
    val status: SlotStatus,
    val description: String?,
    val link: String?,
    val address: String?,
)

data class SlotWithDate(
    val id: Long,
    val time: LocalTime,
    val monthId: Int,
    val dayId: Int,
    val yearId: Int
)