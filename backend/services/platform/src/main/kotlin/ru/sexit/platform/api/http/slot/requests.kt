package ru.sexit.platform.api.http.slot

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import ru.sexit.platform.infrastructure.InvalidDataException
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalTime

data class SlotRequest(
    val time: LocalTime,
    @field:Min(value = 2024)
    @field:Max(value = 10000)
    val yearId: Int,
    @field:Min(value = 0)
    @field:Max(value = 11)
    val monthId: Int,
    @field:Min(value = 0)
    @field:Max(value = 30)
    val dayId: Int,
) {
    fun validateDate() {
        try {
            LocalDate.of(yearId, monthId + 1, dayId + 1)
        } catch (e: DateTimeException) {
            throw InvalidDataException("Slot's date is invalid")
        }
    }
}
