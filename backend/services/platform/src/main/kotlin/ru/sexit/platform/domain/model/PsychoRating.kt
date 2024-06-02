package ru.sexit.platform.domain.model

data class PsychoRating(
    val rating: Double,
    val feedbacks: List<Feedback>,
)
