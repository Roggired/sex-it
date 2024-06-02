package ru.sexit.platform.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "bbb")
data class BbbProps(
    val url: String,
    val secret: String,
    val logoutUrl: String,
    val errorRedirectUrl: String,
    val durationInMinutes: Int,
)
