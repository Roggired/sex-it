package ru.sexit.platform.domain.model

import jakarta.persistence.*
import ru.sexit.platform.api.http.applications.*
import ru.sexit.platform.api.http.profile.PsychoProfileReduced
import ru.sexit.platform.api.http.slot.SlotWithDate
import java.time.LocalDateTime
import java.time.LocalTime


@Entity
@Table(name = "applications")
class Application(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val creationTime: LocalDateTime,
    @Enumerated(value = EnumType.STRING)
    val anonType: AnonType,
    @Enumerated(value = EnumType.STRING)
    val visitType: VisitType,
    @Enumerated(value = EnumType.STRING)
    var status: SlotStatus,
    val description: String?,
    var link: String?,
    var address: String?,
    var results: String?,
    var note: String?,
    val userId: String
) {
    @ManyToOne
    @JoinColumn(name = "slot_id")
    lateinit var slot: Slot

    companion object {
        fun stub(id: Long): Application = Application(
            id = id,
            creationTime = LocalDateTime.now(),
            anonType = AnonType.ANON,
            visitType = VisitType.ONLINE,
            status = SlotStatus.EMPTY,
            description = null,
            link = null,
            address = null,
            results = null,
            note = null,
            userId = "",
        )
    }
}


fun Application.toView(): ApplicationView = ApplicationView(
    id = id,
    slot = SlotWithDate(slot.id, slot.time, slot.monthId, slot.dayId, slot.yearId),
    creationTime = creationTime,
    anonType = anonType,
    visitType = visitType,
    status = status,
    description = description,
    link = link,
    address = address
)

data class AcceptedApplication(
    val id: Long,
    val psychoId: Long,
    val psychoName: String,
    val psychoPrice: Int,
    val slotId: Long,
    val slotTime: LocalTime,
    val slotMonthId: Int,
    val slotDayId: Int,
    val slotYearId: Int,
    val status: SlotStatus,
    val anonType: AnonType,
    val visitType: VisitType,
    val link: String?,
    val address: String?,
    val results: String?
)

fun AcceptedApplication.toAcceptedApplicationView(link: String?): AcceptedApplicationView = AcceptedApplicationView(
    id = id,
    psycho = PsychoProfileReduced(id = psychoId, name = psychoName, price = psychoPrice),
    slot = SlotWithDate(id = slotId, time = slotTime, monthId = slotMonthId, dayId = slotDayId, yearId = slotYearId),
    status = status,
    anonType = anonType,
    visitType = visitType,
    link = link,
    address = address,
    results = results
)
