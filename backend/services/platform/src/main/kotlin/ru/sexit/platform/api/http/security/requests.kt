package ru.sexit.platform.api.http.security

data class TokenExchangeRequest(
    val authorizationCode: String,
    val state: String?,
    val redirectUri: String,
)

data class RefreshAccessRequest(
    val refreshToken: String,
)
