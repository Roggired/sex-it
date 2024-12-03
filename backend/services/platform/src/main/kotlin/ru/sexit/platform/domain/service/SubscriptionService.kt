package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.subscription.AvailableSubscription
import ru.sexit.platform.api.http.subscription.CreateSubscriptionRequest
import ru.sexit.platform.api.http.subscription.CurrentSubscriptionResponse
import ru.sexit.platform.api.http.subscription.UsageStatsResponse
import ru.sexit.platform.domain.model.Subscription
import ru.sexit.platform.domain.model.SubscriptionType
import ru.sexit.platform.domain.repo.SubscriptionRepository
import ru.sexit.platform.infrastructure.exception.InvalidOperationException
import ru.sexit.platform.infrastructure.security.getRequestAuthorUserInfo
import ru.sexit.platform.utils.currentUTCTime
import ru.sexit.platform.utils.log

@Service
class SubscriptionService(
    private val subscriptionRepository: SubscriptionRepository,
    private val applicationService: ApplicationService,
    private val psychoProfileService: PsychoProfileService,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun create(request: CreateSubscriptionRequest): Subscription {
        if (request.type == SubscriptionType.FREE) {
            val availableSubscriptions = getAvailableSubscriptions()
            if (availableSubscriptions.firstOrNull { it.type == SubscriptionType.FREE } == null) {
                throw InvalidOperationException("Free subscription is not allowed")
            }
        }

        val psychoId = getRequestAuthorUserInfo().id
        val currentTime = currentUTCTime()

        val subscriptions = subscriptionRepository.findAllValidByPsychoId(
            psychoId = psychoId,
            validUntil = currentTime,
        )

        val thisTypeSubscription = subscriptions.firstOrNull { it.type == request.type }
        if (thisTypeSubscription != null) {
            thisTypeSubscription.validUntil = currentTime.plusYears(1)
            thisTypeSubscription.paidAt = currentTime
            return subscriptionRepository.save(thisTypeSubscription)
                .also {
                    log.info("Existed subscription (id: ${it.id}) has been extended")
                }
        }

        val otherTypeSubscriptions = subscriptions.filter { it.type !== request.type }
        if (otherTypeSubscriptions.isNotEmpty()) {
            subscriptionRepository.saveAll(
                otherTypeSubscriptions.map {
                    it.suspended = true
                    it
                }
            )
            log.info("Subscriptions: ${otherTypeSubscriptions.map { it.id }} has been suspended")
        }

        return subscriptionRepository.save(
            Subscription(
                id = 0L,
                psychoId = psychoId,
                type = request.type,
                validUntil = if (request.type == SubscriptionType.FREE) {
                    currentTime.plusMonths(1)
                } else currentTime.plusYears(1),
                paidAt = currentTime,
                suspended = false,
            )
        ).also {
            log.info("New subscription (id: ${it.id}) has been paid")
        }
    }

    fun getAvailableSubscriptions(): List<AvailableSubscription> {
        val psychoId = getRequestAuthorUserInfo().id

        val usedFreeSubscriptions = subscriptionRepository.findAllByPsychoIdAndType(
            psychoId = psychoId,
            type = SubscriptionType.FREE,
        )

        val availableSubscriptions = listOf(
            AvailableSubscription(
                type = SubscriptionType.FREE,
                price = 0,
            ),
            AvailableSubscription(
                type = SubscriptionType.BASIC,
                price = 8000,
            ),
            AvailableSubscription(
                type = SubscriptionType.PRO,
                price = 16000,
            ),
        )

        if (usedFreeSubscriptions.isNotEmpty()) {
            return availableSubscriptions.filter { it.type != SubscriptionType.FREE }
        }

        return availableSubscriptions
    }

    fun getCurrentSubscription(): CurrentSubscriptionResponse {
        val psychoId = getRequestAuthorUserInfo().id
        val psychoProfile = psychoProfileService.getMyProfile()
        val currentTime = currentUTCTime()

        val validSubscriptions = subscriptionRepository.findAllValidByPsychoId(
            psychoId = psychoId,
            validUntil = currentTime,
        )

        if (validSubscriptions.isEmpty()) return CurrentSubscriptionResponse(current = null, usageStats = null)

        val subscription = validSubscriptions.maxByOrNull { it.type }
        val usageStats = if (subscription != null) UsageStatsResponse(
            used = applicationService.countOnlineFinishedApplicationsForMonth(
                yearId = currentTime.year,
                monthId = currentTime.monthValue,
                psychoId = psychoProfile.id,
            ) + applicationService.countOnlinePlannedApplicationsForMonth(
                yearId = currentTime.year,
                monthId = currentTime.monthValue,
                psychoId = psychoProfile.id,
            ),
            max = when(subscription.type) {
                SubscriptionType.FREE -> 10
                SubscriptionType.BASIC -> 20
                SubscriptionType.PRO -> 0
            }
        ) else null
        return CurrentSubscriptionResponse(
            current = subscription,
            usageStats = usageStats,
        )
    }
}
