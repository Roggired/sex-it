package ru.sexit.platform.config.http

import okhttp3.Interceptor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import retrofit2.Response
import ru.sexit.platform.config.properties.KeycloakAdminProperties
import ru.sexit.platform.infrastructure.exception.*
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.ErrorToExceptionMapper
import ru.sexit.platform.infrastructure.integration.IntegrationRetrofitClient
import ru.sexit.platform.infrastructure.integration.RetrofitErrorResponseDeserializer
import ru.sexit.platform.infrastructure.integration.keycloak.admin.KeycloakAdminOAuthService
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.api.KeycloakAdminOAuthAPI
import ru.sexit.platform.infrastructure.integration.keycloak.admin.dto.KeycloakError

@Configuration
class KeycloakAdminHttpIntegrationConfig(
    private val keycloakAdminProperties: KeycloakAdminProperties,
) {
    @Bean
    fun keycloakAdminAPI(
        keycloakAdminOAuthService: KeycloakAdminOAuthService,
    ): KeycloakAdminAPI = getRetrofit(
        baseUrl = keycloakAdminProperties.baseUrl,
        okHttpClient = getOkHttpClient(
            additionalInterceptors = listOf(KeycloakAdminAccessInterceptor(keycloakAdminOAuthService))
        ),
    ).create(KeycloakAdminAPI::class.java)

    @Bean
    fun keycloakAdminOAuthAPI(): KeycloakAdminOAuthAPI = getRetrofit(
        baseUrl = keycloakAdminProperties.authenticationBaseUrl,
        okHttpClient = getOkHttpClient(),
    ).create(KeycloakAdminOAuthAPI::class.java)

    @Bean
    fun keycloakAdminIntegrationRetrofitClient(): IntegrationRetrofitClient<KeycloakError> = IntegrationRetrofitClient(
        retrofitErrorResponseDeserializer = KeycloakErrorDeserializer(),
        errorToExceptionMapper = KeycloakErrorToExceptionMapper()
    )
}

class KeycloakAdminAccessInterceptor(
    private val keycloakAdminOAuthService: KeycloakAdminOAuthService,
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val okHttpRequest = chain.request()

        val requestBuilder = okHttpRequest.newBuilder()
        keycloakAdminOAuthService.addCredentials(requestBuilder)

        return chain.proceed(requestBuilder.build())
    }
}

class KeycloakErrorDeserializer: RetrofitErrorResponseDeserializer<KeycloakError> {
    override fun deserialize(response: Response<*>): KeycloakError =
        KeycloakError(
            description = response.errorBody()
                ?.bytes()
                ?.decodeToString(),
            status = HttpStatus.valueOf(response.code()),
        )
}

class KeycloakErrorToExceptionMapper: ErrorToExceptionMapper<KeycloakError> {
    override fun handle(downstreamService: DownstreamServices, errorContent: KeycloakError?): RuntimeException {
        if (errorContent == null) {
            return InvalidDataException("No extra info provided")
        }

        if (errorContent.status == HttpStatus.UNAUTHORIZED) {
            throw UnauthorizedFromDownstreamServiceException(
                downstreamService = DownstreamServices.KEYCLOAK,
                errorResponse = ErrorResponse(
                    status = ErrorType.UNAUTHORIZED,
                    description = ""
                )
            )
        }

        throw InternalServerException("Got something unexpected from Keycloak: ${errorContent.description}")
    }
}
