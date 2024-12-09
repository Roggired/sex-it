package ru.sexit.platform.api.http.profile.friend.model

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

// FriendRequest -- запрос на создание/изменение профиля другана психолога
data class FriendRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val email: String,
    @field:Positive
    val percent: Int, // процент, который друган психолога забирает себе в карман
)

data class FriendshipRequest(
    @field:Positive
    val psychoId: Int
)