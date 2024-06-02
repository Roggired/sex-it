package ru.sexit.platform.api.http.profile

import ru.sexit.platform.domain.model.PsychoProfile
import java.time.LocalDateTime

data class ProfilePsychoView(
    val id: Long,
    val name: String,
    val email: String,
    val price: Int,
    val isFirstFree: Boolean,
    val bio: String,
)

fun PsychoProfile.toPsychoView(): ProfilePsychoView = ProfilePsychoView(
    id = id,
    name = name,
    email = email,
    price = price,
    isFirstFree = isFirstFree,
    bio = bio,
)

data class ProfileClientView(
    val id: Long,
    val name: String,
    val email: String,
    val price: Int,
    val isFirstFree: Boolean,
    val bio: String,
    val rating: Double,
    val feedbacks: List<FeedbackView>,
)

fun PsychoProfile.toClientView(): ProfileClientView = ProfileClientView(
    id = id,
    name = name,
    email = email,
    price = price,
    isFirstFree = isFirstFree,
    bio = bio,
    rating = 0.0,
    feedbacks = emptyList(),
)

data class FeedbackView(
    val id: Long,
    val creationTime: LocalDateTime,
    val text: String,
)
