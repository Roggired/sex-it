package ru.sexit.platform.api.http.feedback

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

data class FeedbackRequest(
    @field:Min(value = 1)
    @field:Max(value = 5)
    val rating: Int,
    @field:NotBlank
    val text: String,
)
