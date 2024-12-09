package ru.sexit.platform.domain.model

import jakarta.persistence.*

enum class ReferralProgramStatus {
    CREATED,
    CREATED_APPLICATION,
    VISITED_MEET
    ;
}

@Entity
@Table(name = "referral_program")
class ReferralProgram(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    var psychoId: Long,
    var friendId: Long,
    var applicationId: Long?,
    var status: String
)