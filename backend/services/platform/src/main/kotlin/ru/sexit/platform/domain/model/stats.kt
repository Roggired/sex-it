package ru.sexit.platform.domain.model

data class SubscriptionStats(
    val totalPsychos: Int,
    val freeSubscriptions: Int,
    val basicSubscriptions: Int,
    val proSubscriptions: Int,
)

data class ConsultsStats(
    val numberOfOnlinePerformed: Int,
)
