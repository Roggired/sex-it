package ru.sexit.platform.domain.model

import jakarta.persistence.*
import ru.sexit.platform.api.http.referralprogram.ReferralProgramView

enum class ReferralProgramStatus {
    CREATED,
    CREATED_APPLICATION,
    ACCEPTED_APPLICATION,
    VISITED_MEET
    ;
}

enum class PaidStatus {
    NOT_PAID,
    PAID
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
    var status: String,
    var paidStatus: String,
)

interface ReferralProgramProjection {
    val id: Long
    val name: String
}

fun ReferralProgramProjection.toView(): ReferralProgramView = ReferralProgramView(
    id = id,
    name = name,
)

interface ReferralProgramForApplicationProjection {
    var referId: Long
    var friendId: Long
    var name: String
}