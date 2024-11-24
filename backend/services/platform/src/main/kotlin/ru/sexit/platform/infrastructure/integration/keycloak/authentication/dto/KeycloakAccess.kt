package ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class KeycloakAccess(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("expires_in")
    val expiresIn: Int,
)
