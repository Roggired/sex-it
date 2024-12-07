package ru.sexit.platform.api.http.stats

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sexit.platform.domain.model.ConsultsStats
import ru.sexit.platform.domain.model.SubscriptionStats
import ru.sexit.platform.domain.service.StatsService

@RestController
@RequestMapping("/api/v1/stats")
class StatsController(
    private val statsService: StatsService,
) {
    @PostMapping("/count-subscription-stats")
    fun countSubscriptionStats(): SubscriptionStats = statsService.getSubscriptionsStats()

    @PostMapping("/count-consults-stats")
    fun countConsultStats(
        @RequestBody @Validated request: GetConsultsRequest,
    ): ConsultsStats = statsService.getConsultsStats(request)
}
