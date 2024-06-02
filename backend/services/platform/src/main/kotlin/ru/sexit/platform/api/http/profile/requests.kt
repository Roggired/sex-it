package ru.sexit.platform.api.http.profile

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

enum class ProfileViewMode {
    CLIENT,
    PSYCHO,
    ;
}

data class PsychoProfileRequest(
    @field:NotBlank
    val name: String,
    @field:NotBlank
    val email: String,
    @field:Positive
    val price: Int,
    val isFirstFree: Boolean,
    val bio: String,
)
