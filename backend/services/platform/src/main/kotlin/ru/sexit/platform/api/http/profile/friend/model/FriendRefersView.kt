package ru.sexit.platform.api.http.profile.friend.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import ru.sexit.platform.domain.model.FriendProfile


data class FriendProfileView(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val email: String,
    @field:Positive
    val percent: Int,
)


fun FriendProfile.toView(): FriendProfileView = FriendProfileView(
    name = name,
    email = email,
    percent = percent,
)