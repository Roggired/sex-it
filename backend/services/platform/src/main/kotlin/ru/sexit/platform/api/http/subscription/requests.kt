package ru.sexit.platform.api.http.subscription

import ru.sexit.platform.domain.model.SubscriptionType

data class CreateSubscriptionRequest(
    val type: SubscriptionType,
)
