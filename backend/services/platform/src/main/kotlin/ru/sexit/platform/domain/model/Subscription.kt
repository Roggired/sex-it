package ru.sexit.platform.domain.model

import jakarta.persistence.*
import ru.sexit.platform.utils.currentUTCTime
import java.time.LocalDateTime
import java.util.UUID

enum class SubscriptionType {
    FREE,
    BASIC,
    PRO,
    ;
}

@Entity
@Table(name = "subscriptions")
class Subscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val psychoId: String,
    @Enumerated(EnumType.STRING)
    val type: SubscriptionType,
    var validUntil: LocalDateTime,
    var paidAt: LocalDateTime,
    var suspended: Boolean,
) {
    @Transient
    val isExpired = validUntil.isBefore(currentUTCTime())
}
