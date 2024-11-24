package ru.sexit.platform.api.http

data class UserInfoDto(
    val id: String,
    val email: String,
    val username: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
)
