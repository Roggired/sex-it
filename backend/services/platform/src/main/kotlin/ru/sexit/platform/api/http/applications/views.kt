package ru.sexit.platform.api.http.applications

import ru.sexit.platform.api.http.profile.PsychoProfileReduced
import ru.sexit.platform.api.http.slot.SlotWithDate
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.SlotStatus
import java.time.LocalDateTime

enum class AnonType {
    ANON,
    NE_ANON
}

enum class VisitType {
    ONLINE,
    OFFLINE
}

data class ApplicationViewCreated(
    val slotId: Long,
    val anonType: AnonType,
    val visitType: VisitType,
    val description: String?
)


data class ApplicationView(
    val id: Long,
    val slot: SlotWithDate,
    val creationTime: LocalDateTime,
    val anonType: AnonType?,
    val visitType: VisitType?,
    val status: SlotStatus,
    val description: String?,
    val link: String?,
    val address: String?
)

data class AcceptedApplicationView(
    val id: Long,
    val psycho: PsychoProfileReduced,
    val slot: SlotWithDate,
    val status: SlotStatus,
    val anonType: AnonType,
    val visitType: VisitType,
    val link: String?,
    val address: String?,
    val results: String?
)
