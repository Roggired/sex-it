package ru.sexit.platform.infrastructure.integration.keycloak.authentication

import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.HttpServerErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import ru.sexit.platform.config.properties.KeycloakSecurityProperties
import ru.sexit.platform.infrastructure.exception.InternalServerException
import ru.sexit.platform.infrastructure.exception.KeycloakAnsweredForbiddenException
import ru.sexit.platform.infrastructure.exception.KeycloakAnsweredUnauthorizedException
import ru.sexit.platform.infrastructure.exception.KeycloakIsUnavailableException
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.dto.KeycloakAccess
import ru.sexit.platform.utils.log
import java.io.IOException

typealias FormUrlEncodedData = LinkedMultiValueMap<String, String>

@Service
class KeycloakOAuth2API(
    private val keycloakProperties: KeycloakSecurityProperties,
) {
    fun tokenRequest(authorizationCode: String, redirectUri: String,): KeycloakAccess {
        val keycloakResponse = templateKeycloakRequest(
            endpoint = keycloakProperties.tokenEndpoint,
            method = HttpMethod.POST,
            httpEntity = formUrlencodedHttpEntity {
                add("grant_type", KeycloakSecurityProperties.GRANT_TYPE_AUTHORIZATION_CODE)
                add("code", authorizationCode)
                add("redirect_uri", redirectUri)
                add("client_id", keycloakProperties.clientId)
                add("client_secret", keycloakProperties.clientSecret)
            },
            responseType = KeycloakAccess::class.java,
        )

        return handleEmptyBodyResponse(keycloakResponse)
    }

    fun refreshAccess(refreshToken: String): KeycloakAccess {
        val keycloakResponse = templateKeycloakRequest(
            endpoint = keycloakProperties.tokenEndpoint,
            method = HttpMethod.POST,
            httpEntity = formUrlencodedHttpEntity {
                add("grant_type", KeycloakSecurityProperties.GRANT_TYPE_REFRESH_TOKEN)
                add("refresh_token", refreshToken)
                add("client_id", keycloakProperties.clientId)
                add("client_secret", keycloakProperties.clientSecret)
            },
            responseType = KeycloakAccess::class.java,
        )

        return handleEmptyBodyResponse(keycloakResponse)
    }

    private fun <T> handleEmptyBodyResponse(response: ResponseEntity<T>): T {
        val body = response.body
        if (body == null) {
            log.error("Keycloak returned OK response, " +
                    "but no body on ${keycloakProperties.internalBaseUrl}${keycloakProperties.tokenEndpoint}. " +
                    "ResponseStatus: ${response.statusCode.value()}. ")
            throw InternalServerException("Keycloak returned ok status, but empty body")
        }

        return body
    }

    private fun formUrlencodedHttpEntity(
        dataConfigurer: FormUrlEncodedData.() -> Unit
    ): HttpEntity<FormUrlEncodedData> {
        return HttpEntity<FormUrlEncodedData>(
            FormUrlEncodedData().apply { dataConfigurer.invoke(this) },
            HttpHeaders().apply { contentType = MediaType.APPLICATION_FORM_URLENCODED }
        )
    }

    private fun <T, R> templateKeycloakRequest(
        endpoint: String,
        method: HttpMethod,
        httpEntity: HttpEntity<T>,
        responseType: Class<R>,
    ): ResponseEntity<R> {
        return try {
            RestTemplate().exchange(
                "${keycloakProperties.internalBaseUrl}$endpoint",
                method,
                httpEntity,
                responseType
            )
        } catch (e: RestClientException) {
            when(e) {
                is HttpClientErrorException.BadRequest -> {
                    val responseAsJsonString = e.responseBodyAsString.replace(" ", "")
                    val sessionNotActiveThereforeInvalidGrantText = "{\"error\":\"invalid_grant\",\"error_description\":\"Sessionnotactive\"}"
                    val tokenIsExpiredText = "{\"error\":\"invalid_grant\",\"error_description\":\"Tokenisnotactive\"}"
                    if (responseAsJsonString == sessionNotActiveThereforeInvalidGrantText) {
                        throw KeycloakAnsweredUnauthorizedException()
                    }

                    if (responseAsJsonString == tokenIsExpiredText) {
                        throw KeycloakAnsweredUnauthorizedException()
                    }

                    log.error("Keycloak answered BagRequest. Most likely, our backend send invalid data. " +
                            "Therefore, this exception is treated like InternalServerException.", e)
                    throw InternalServerException("Keycloak answered BagRequest")
                }
                is HttpClientErrorException.MethodNotAllowed,
                is HttpClientErrorException.NotAcceptable,
                is HttpClientErrorException.NotFound -> {
                    log.error("Keycloak answered BagRequest. Most likely, our backend send invalid data. " +
                            "Therefore, this exception is treated like InternalServerException.", e)
                    throw InternalServerException("Keycloak answered BagRequest")
                }
                is HttpClientErrorException.Unauthorized -> {
                    log.warn("Keycloak answered 401. We assume that this exception has appeared because of " +
                            "frontend error")
                    throw KeycloakAnsweredUnauthorizedException()
                }
                is HttpClientErrorException.Forbidden -> {
                    log.warn("Keycloak answered 403. We assume that this exception has appeared because of " +
                            "frontend error")
                    throw KeycloakAnsweredForbiddenException()
                }
                is HttpClientErrorException -> {
                    if (e.statusCode.value() >= 500) {
                        log.error("Keycloak is unavailable! Keycloak answered 5xx status code!", e)
                        throw KeycloakIsUnavailableException()
                    }

                    log.error("Unexpected exception during Keycloak REST API invocation! We can't interpret this " +
                            "situation", e)
                    throw InternalServerException("Unexpected error during Keycloak REST API invocation")
                }
                is HttpServerErrorException -> {
                    log.error("Keycloak is unavailable! Keycloak answered 5xx status code!", e)
                    throw KeycloakIsUnavailableException()
                }
                else -> {
                    if (e.cause != null && e.cause is IOException) {
                        log.error("IOException has occured during Keycloak REST API invocation. We iterpret this situation " +
                                "like Keycloak is unavailable", e)
                        throw KeycloakIsUnavailableException()
                    }

                    log.error("Unexpected exception during Keycloak REST API invocation! We can't interpret this " +
                            "situation", e)
                    throw InternalServerException("Unexpected error during Keycloak REST API invocation")
                }
            }
        }
    }
}
