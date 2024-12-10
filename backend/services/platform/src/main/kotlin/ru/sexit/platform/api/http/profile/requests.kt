package ru.sexit.platform.api.http.profile

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

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

data class FilterPsychoRequest(
    val filters: Filters = Filters(),
) {
    data class Filters(
        val name: String? = null,
        val priceFrom: Int? = null,
        val priceTo: Int? = null,
        val minRating: Double? = null,
    )
}


data class FilterAvailablePsycho(
    val name: String? = null
)