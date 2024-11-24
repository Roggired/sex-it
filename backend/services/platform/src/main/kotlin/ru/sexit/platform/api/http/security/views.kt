package ru.sexit.platform.api.http.security

import ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto.KeycloakAccess

data class KeycloakAccessView(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Int,
)

fun KeycloakAccess.toView(): KeycloakAccessView = KeycloakAccessView(
    accessToken = accessToken,
    refreshToken = refreshToken,
    expiresIn = expiresIn
)
