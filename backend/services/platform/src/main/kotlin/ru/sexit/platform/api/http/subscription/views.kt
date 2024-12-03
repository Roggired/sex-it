package ru.sexit.platform.api.http.subscription

import ru.sexit.platform.domain.model.Subscription
import ru.sexit.platform.domain.model.SubscriptionType

data class AvailableSubscription(
    val type: SubscriptionType,
    val price: Int,
)

data class CurrentSubscriptionResponse(
    val current: Subscription?,
    val usageStats: UsageStatsResponse?
)

data class UsageStatsResponse(
    val used: Int,
    val max: Int,
)
