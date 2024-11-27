package ru.sexit.platform.infrastructure.integration.keycloak.authentication

import org.springframework.stereotype.Service
import ru.sexit.platform.api.http.security.RefreshAccessRequest
import ru.sexit.platform.api.http.security.TokenExchangeRequest
import ru.sexit.platform.config.properties.KeycloakSecurityProperties
import ru.sexit.platform.infrastructure.exception.StateIsInvalidException
import ru.sexit.platform.infrastructure.exception.StateIsNotProvidedException
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto.KeycloakAccess
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.entity.KeycloakStateEntity
import ru.sexit.platform.utils.currentUTCTime
import ru.sexit.platform.utils.log

@Service
class KeycloakOAuth2AuthenticationService(
    private val keycloakProperties: KeycloakSecurityProperties,
    private val keycloakStateEntityRepository: KeycloakStateEntityRepository,
    private val keycloakOAuth2API: KeycloakOAuth2API,
) {
    fun getAuthorizeRedirectionUrl(redirectUri: String): String {
        val keycloakGeneratedAuthorizeUrl = keycloakProperties.getAuthorizeUrl(redirectUri)
        if (keycloakGeneratedAuthorizeUrl.useState) {
            keycloakStateEntityRepository.save(
                KeycloakStateEntity(
                    id = 0L,
                    state = keycloakGeneratedAuthorizeUrl.state,
                    activeBefore = currentUTCTime().plusMinutes(keycloakProperties.stateLifetime.toMinutes()),
                    isUsed = false,
                )
            )
        }
        return keycloakGeneratedAuthorizeUrl.authorizeUrl
    }

    fun getLogoutUrl(redirectUri: String): String = keycloakProperties.getLogoutUrl(redirectUri)

    fun tokenExchange(request: TokenExchangeRequest): KeycloakAccess {
        if (keycloakProperties.useStateForCSRFProtection) {
            val state = request.state ?: throw StateIsNotProvidedException()
            val keycloakStateEntity = keycloakStateEntityRepository.findByState(state) ?: throw StateIsInvalidException()

            if (keycloakStateEntity.isUsed ||
                keycloakStateEntity.activeBefore.isBefore(currentUTCTime())) {
                markStateLikeUsed(keycloakStateEntity)
                throw StateIsInvalidException()
            }

            markStateLikeUsed(keycloakStateEntity)
        }

        val keycloakAccess = keycloakOAuth2API.tokenRequest(
            authorizationCode = request.authorizationCode,
            redirectUri = request.redirectUri,
        )

        log.debug("Successfully obtain tokens from Keycloak: ${keycloakProperties.internalBaseUrl}${keycloakProperties.tokenEndpoint}")
        return keycloakAccess
    }

    private fun markStateLikeUsed(keycloakStateEntity: KeycloakStateEntity) {
        keycloakStateEntityRepository.save(
            KeycloakStateEntity(
                id = keycloakStateEntity.id,
                state = keycloakStateEntity.state,
                activeBefore = keycloakStateEntity.activeBefore,
                isUsed = true,
            )
        )
    }

    fun refreshAccess(request: RefreshAccessRequest): KeycloakAccess {
        val keycloakAccess = keycloakOAuth2API.refreshAccess(
            refreshToken = request.refreshToken
        )

        log.debug("Successfully refresh tokens from keycloak: ${keycloakProperties.internalBaseUrl}${keycloakProperties.tokenEndpoint}")
        return keycloakAccess
    }
}
