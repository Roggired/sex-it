package ru.sexit.platform.api.http.subscription

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.sexit.platform.domain.model.Subscription
import ru.sexit.platform.domain.service.SubscriptionService

@RestController
@RequestMapping("/api/v1/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {
    @PostMapping
    fun createSubscription(
        @RequestBody @Validated request: CreateSubscriptionRequest
    ): Subscription = subscriptionService.create(request)

    @GetMapping("/available")
    fun getAvailableSubscriptions(): List<AvailableSubscription> = subscriptionService.getAvailableSubscriptions()

    @GetMapping("/current")
    fun getCurrentSubscription(): CurrentSubscriptionResponse = CurrentSubscriptionResponse(
        current = subscriptionService.getCurrentSubscription()
    )
}
