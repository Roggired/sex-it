package ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto

data class KeycloakGeneratedAuthorizeUrl(
    val authorizeUrl: String,
    val state: String,
    val useState: Boolean,
)
