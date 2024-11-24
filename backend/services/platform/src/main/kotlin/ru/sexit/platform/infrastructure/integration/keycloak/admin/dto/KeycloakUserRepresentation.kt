package ru.sexit.platform.infrastructure.integration.keycloak.admin.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import ru.sexit.platform.domain.model.UserInfo
import ru.sexit.platform.infrastructure.exception.InternalServerException

const val PATRONYMIC_ATTRIBUTE_KEY = "patronymic"

@JsonIgnoreProperties(ignoreUnknown = true)
data class KeycloakUserRepresentation(
    val attributes: Map<String, List<String>>?,
    val createdTimestamp: Long?,
    val email: String?,
    val emailVerified: Boolean?,
    val enabled: Boolean?,
    val firstName: String?,
    val id: String?,
    val lastName: String?,
    val username: String?,
    val realmRoles: List<String>?
) {
    @JsonIgnore
    val patronymic = attributes?.let { extractPatronymic(attributes) }
    @JsonIgnore
    val fullName = patronymic?.let { "$lastName $firstName $patronymic" } ?: "$lastName $firstName"
}

fun KeycloakUserRepresentation.toUserInfo(): UserInfo {
    val nullField: String? = if (id == null) {
        "id"
    } else if (email == null) {
        "email"
    } else if (username == null) {
        "username"
    } else if (firstName == null) {
        "name"
    } else if (lastName == null) {
        "surname"
    } else null

    if (nullField != null) {
        throw InternalServerException("${KeycloakUserRepresentation::class.simpleName} cannot be mapped to " +
                "${UserInfo::class.simpleName}, because mandatory field is null: $nullField")
    }

    return UserInfo(
        id = id!!,
        email = email!!,
        username = username!!,
        name = firstName!!,
        surname = lastName!!,
        patronymic = patronymic,
    )
}

private fun extractPatronymic(attributes: Map<String, List<String>>): String? {
    val values = attributes[PATRONYMIC_ATTRIBUTE_KEY]
    if (values.isNullOrEmpty()) return null

    return values[0]
}
