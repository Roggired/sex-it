package ru.sexit.platform.api.http.referralprogram

data class ReferralProgramView(
    val id: Long,
    val name: String,
    val paidStatus: String
)