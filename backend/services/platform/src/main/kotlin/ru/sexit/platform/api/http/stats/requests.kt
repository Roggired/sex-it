package ru.sexit.platform.api.http.stats

import java.time.LocalDateTime

data class GetConsultsRequest(
    val from: LocalDateTime,
    val to: LocalDateTime,
)
