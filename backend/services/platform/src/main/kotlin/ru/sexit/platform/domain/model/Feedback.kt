package ru.sexit.platform.domain.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "feedbacks")
class Feedback(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val creationTime: LocalDateTime,
    val rating: Int,
    val text: String,
) {
    @ManyToOne(fetch = FetchType.LAZY, cascade = [])
    @JoinColumn(name = "psycho_id")
    lateinit var psychoProfile: PsychoProfile
}
