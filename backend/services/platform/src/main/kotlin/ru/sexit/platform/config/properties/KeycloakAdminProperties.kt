package ru.sexit.platform.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "keycloak.admin")
data class KeycloakAdminProperties(
    val baseUrl: String,
    val authenticationBaseUrl: String,
    val adminClientId: String,
    val adminUsername: String,
    val adminPassword: String,
)
