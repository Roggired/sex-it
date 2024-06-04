package ru.sexit.platform.domain.model

import jakarta.persistence.*
import java.time.LocalTime

enum class SlotStatus {
    EMPTY,
    DONE,
    PLANNED,
    NEED_REVIEW,
    ;
}

@Entity
@Table(name = "slots")
class Slot(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var time: LocalTime,
    var yearId: Int,
    var monthId: Int,
    var dayId: Int,
) {
    @ManyToOne
    @JoinColumn(name = "psycho_id")
    lateinit var psychoProfile: PsychoProfile
}
