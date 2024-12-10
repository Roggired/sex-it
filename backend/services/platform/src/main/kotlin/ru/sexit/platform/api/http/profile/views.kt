package ru.sexit.platform.api.http.profile

import ru.sexit.platform.api.http.feedback.FeedbackView
import ru.sexit.platform.api.http.feedback.toView
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.PsychoRating

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

fun PsychoProfile.toClientView(
    psychoRating: PsychoRating
): ProfileClientView = ProfileClientView(
    id = id,
    name = name,
    email = email,
    price = price,
    isFirstFree = isFirstFree,
    bio = bio,
    rating = psychoRating.rating,
    feedbacks = psychoRating.feedbacks.map {
        it.toView()
    },
)

data class PsychoProfileReduced(
    val id: Long,
    val name: String,
    val price: Int
)

data class PsychoProfileForCatalogueView(
    val id: Long,
    val name: String,
    val price: Int,
    val rating: Double?,
)

data class PsychoProfileForFriendshipView(
    val id: Long,
    val name: String,
)
