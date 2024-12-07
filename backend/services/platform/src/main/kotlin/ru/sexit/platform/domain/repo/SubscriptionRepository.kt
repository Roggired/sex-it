package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.Subscription
import ru.sexit.platform.domain.model.SubscriptionType
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface SubscriptionRepository: JpaRepository<Subscription, Long> {
    @Query(
        """
            SELECT s
            FROM Subscription s
            WHERE s.psychoId = :psychoId AND s.validUntil >= :validUntil AND s.suspended = FALSE 
        """
    )
    fun findAllValidByPsychoId(
        psychoId: String,
        validUntil: LocalDateTime,
    ): List<Subscription>

    fun findAllByPsychoIdAndType(psychoId: String, type: SubscriptionType): List<Subscription>

    @Query(
        """
            SELECT COUNT(*)
            FROM Subscription s
            WHERE s.suspended = FALSE AND s.type = :type AND s.validUntil >= :currentTime
        """
    )
    fun countAliveSubscriptions(
        type: SubscriptionType,
        currentTime: LocalDateTime,
    ): Int
}
