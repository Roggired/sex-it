package ru.sexit.platform.config.properties

import org.apache.commons.lang3.RandomStringUtils
import org.springframework.boot.context.properties.ConfigurationProperties
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto.KeycloakGeneratedAuthorizeUrl
import java.time.Duration

@ConfigurationProperties(prefix = "security.keycloak")
data class KeycloakSecurityProperties(
    // two urls because of Mac OS
    val publicBaseUrl: String,
    val internalBaseUrl: String,
    val jwksEndpoint: String,
    val authorizeEndpoint: String,
    val tokenEndpoint: String,
    val logoutEndpoint: String,
    val clientId: String,
    val clientSecret: String,
    val useStateForCSRFProtection: Boolean,
    val stateLifetime: Duration,
) {
    fun getAuthorizeUrl(redirectUri: String): KeycloakGeneratedAuthorizeUrl {
        val authorizeUrl = "$publicBaseUrl$authorizeEndpoint" +
                "?response_type=code" +
                "&client_id=$clientId" +
                "&redirect_uri=$redirectUri"

        if (useStateForCSRFProtection) {
            val state = RandomStringUtils.randomAlphanumeric(43, 128)
            return KeycloakGeneratedAuthorizeUrl(
                authorizeUrl = "$authorizeUrl&state=$state",
                state = state,
                useState = true,
            )
        }

        return KeycloakGeneratedAuthorizeUrl(
            authorizeUrl = authorizeUrl,
            state = "",
            useState = false,
        )
    }

    fun getLogoutUrl(redirectUri: String): String = "$publicBaseUrl$logoutEndpoint?post_logout_redirect_uri=$redirectUri&client_id=$clientId"

    fun getJwksUrl(): String = "$internalBaseUrl$jwksEndpoint"

    companion object {
        const val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"
        const val GRANT_TYPE_REFRESH_TOKEN = "refresh_token"
    }
}
