package ru.sexit.platform.domain.model

import ru.sexit.platform.api.http.UserInfoDto

data class UserInfo(
    val id: String,
    val email: String,
    val username: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
)

fun UserInfo.toDto(): UserInfoDto = UserInfoDto(
    id = id,
    email = email,
    username = username,
    surname = surname,
    patronymic = patronymic,
    name = name,
)
