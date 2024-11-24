package ru.sexit.platform.config.http

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import ru.sexit.platform.infrastructure.exception.*
import ru.sexit.platform.infrastructure.integration.DownstreamServices
import ru.sexit.platform.infrastructure.integration.ErrorToExceptionMapper
import ru.sexit.platform.infrastructure.integration.RetrofitErrorResponseDeserializer

fun getRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit =
    Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(
            JacksonConverterFactory.create(
                jacksonObjectMapper().apply {
                    registerModule(JavaTimeModule())
                }
            )
        )
        .build()

fun getOkHttpClient(
    additionalInterceptors: List<Interceptor> = emptyList()
): OkHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(
        HttpLoggingInterceptor()
            .apply { level = HttpLoggingInterceptor.Level.HEADERS }
    )
    .also {  okhttpClientBuilder ->
        if (additionalInterceptors.isNotEmpty()) {
            additionalInterceptors.forEach { okhttpClientBuilder.addInterceptor(it) }
        }
    }
    .build()

class ErrorResponseDeserializer(
    private val objectMapper: ObjectMapper,
): RetrofitErrorResponseDeserializer<ErrorResponse> {
    override fun deserialize(response: Response<*>): ErrorResponse? =
        response
            .errorBody()
            ?.bytes()
            ?.decodeToString()
            ?.let { objectMapper.readValue(it, ErrorResponse::class.java) }
}

class GenericErrorToExceptionMapper: ErrorToExceptionMapper<ErrorResponse> {
    override fun handle(downstreamService: DownstreamServices, errorContent: ErrorResponse?): RuntimeException {
        if (errorContent == null) {
            return InvalidDataException("No extra info provided")
        }

        return when(errorContent.status) {
            ErrorType.UNEXPECTED_EXCEPTION -> InternalServerException(errorContent.description)
            ErrorType.DOWNSTREAM_SERVICE_IN_UNAVAILABLE -> InternalServerException(errorContent.description)
            ErrorType.INVALID_DATA -> BadRequestFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.INVALID_OPERATION -> BadRequestFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.BODY_NOT_READABLE -> InternalServerException("Body not readable from downstream service. Most likely, error in API Gateway code. Description: ${errorContent.description}")
            ErrorType.NOT_FOUND -> NotFoundFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.ALREADY_EXIST -> AlreadyExistFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.UNAUTHORIZED -> UnauthorizedFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.FORBIDDEN -> ForbiddenFromDownstreamServiceException(downstreamService, errorContent)
            ErrorType.BBB_EXCEPTION -> InternalServerException(errorContent.description)
        }
    }
}
