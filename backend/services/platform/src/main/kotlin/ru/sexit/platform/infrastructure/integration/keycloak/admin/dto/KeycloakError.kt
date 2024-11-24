package ru.sexit.platform.infrastructure.integration.keycloak.admin.dto

import org.springframework.http.HttpStatus

data class KeycloakError(
    val description: String?,
    val status: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
)
