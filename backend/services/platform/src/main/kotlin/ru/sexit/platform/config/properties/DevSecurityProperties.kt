package ru.sexit.platform.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile

@ConfigurationProperties(prefix = "security.dev-only")
@Profile("dev")
data class DevSecurityProperties(
    val apiKeyHeader: String,
    val psycho: DevUserProperties,
    val client: DevUserProperties,
    val psychoFriend: DevUserProperties,
    val admin: DevUserProperties,
)

data class DevUserProperties(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val apiKey: String,
)
