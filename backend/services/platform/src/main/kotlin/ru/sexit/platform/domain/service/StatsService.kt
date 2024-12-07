package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import ru.sexit.platform.api.http.stats.GetConsultsRequest
import ru.sexit.platform.domain.model.ConsultsStats
import ru.sexit.platform.domain.model.SubscriptionStats
import ru.sexit.platform.domain.model.SubscriptionType
import ru.sexit.platform.domain.repo.ApplicationRepository
import ru.sexit.platform.domain.repo.PsychoProfileRepo
import ru.sexit.platform.domain.repo.SubscriptionRepository
import ru.sexit.platform.utils.currentUTCTime

@Service
class StatsService(
    private val psychoProfileRepo: PsychoProfileRepo,
    private val subscriptionRepository: SubscriptionRepository,
    private val applicationRepository: ApplicationRepository,
) {
    fun getSubscriptionsStats(): SubscriptionStats {
        val currentTime = currentUTCTime()
        return SubscriptionStats(
            totalPsychos = psychoProfileRepo.count().toInt(),
            freeSubscriptions = subscriptionRepository.countAliveSubscriptions(
                type = SubscriptionType.FREE,
                currentTime = currentTime,
            ),
            basicSubscriptions = subscriptionRepository.countAliveSubscriptions(
                type = SubscriptionType.BASIC,
                currentTime = currentTime,
            ),
            proSubscriptions = subscriptionRepository.countAliveSubscriptions(
                type = SubscriptionType.PRO,
                currentTime = currentTime,
            )
        )
    }

    fun getConsultsStats(request: GetConsultsRequest): ConsultsStats = ConsultsStats(
        numberOfOnlinePerformed = applicationRepository.countPerformedAtPeriod(
            from = request.from,
            to = request.to,
        )
    )
}
