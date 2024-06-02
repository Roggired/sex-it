package ru.sexit.platform.api.http.feedback

import ru.sexit.platform.domain.model.Feedback
import java.time.LocalDateTime

data class FeedbackView(
    val id: Long,
    val rating: Int,
    val creationTime: LocalDateTime,
    val text: String,
)

fun Feedback.toView(): FeedbackView = FeedbackView(
    id = id,
    rating = rating,
    creationTime = creationTime,
    text = text,
)
