package ru.sexit.platform.api.http.referralprogram

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

data class UpdateReferRequest(
    @field:Positive
    val psychoId: Long,
    @field:NotNull
    val status: String
)

// FriendRefersView -- ответ, содержащий информацию дружбе другана и психолога (для другана)
data class FriendRefersView(
    val psychoName: String,
    val friendshipStatus: String
)

// FriendRefersView -- ответ, содержащий информацию дружбе другана и психолога (для психа)
data class PsychoRefersView(
    val id: Long,
    val friendName: String,
    val percent: Int,
    val status: String
)

