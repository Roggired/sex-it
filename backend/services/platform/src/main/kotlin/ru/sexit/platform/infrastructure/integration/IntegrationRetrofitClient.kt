package ru.sexit.platform.infrastructure.integration

import okio.IOException
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import retrofit2.Call
import retrofit2.Response
import ru.sexit.platform.utils.log

enum class DownstreamServices {
    KEYCLOAK,
    ;
}

data class IntegrationRetrofitResponse<Success, Error>(
    val httpStatusCode: HttpStatus,
    val content: Success? = null,
    val errorContent: Error? = null,
)

/**
 * see [IntegrationRetrofitClient]
 */
interface RetrofitErrorResponseDeserializer<Error> {
    fun deserialize(response: Response<*>): Error?
}

/**
 * see [IntegrationRetrofitClient]
 */
interface ErrorToExceptionMapper<Error> {
    fun handle(downstreamService: DownstreamServices, errorContent: Error?): RuntimeException
}

class ExternalServiceCannotBeInvokedException: RuntimeException()

/**
 * IntegrationRetrofitClient is designed to add support for unified error handling when API Gateway executes any
 * request to other services. It supports RetrofitErrorResponseDeserializer to deserialize any 4xx error from a
 * downstream service. It supports ErrorToExceptionMapper to map deserialized 4xx error to Kotlin RuntimeException.
 *
 * So, with that class you can easily 'call Retrofit inside error handling logic`. Of course,
 * IntegrationRetrofitClient also do some logging.
 *
 * @author ego
 */
class IntegrationRetrofitClient<Error>(
    private val retrofitErrorResponseDeserializer: RetrofitErrorResponseDeserializer<Error>,
    private val errorToExceptionMapper: ErrorToExceptionMapper<Error>? = null,
) {
    fun <Success> invokeExternalService(
        downstreamService: DownstreamServices,
        invocation: () -> Call<Success>,
    ): IntegrationRetrofitResponse<Success, Error> {
        try {
            val retrofitResponse = invocation.invoke().execute()

            if (retrofitResponse.code() >= 500) {
                val errorContent = if (retrofitResponse.errorBody() == null) {
                    "<NO CONTENT>"
                } else if (retrofitResponse.errorBody()?.contentType() == null) {
                    "<NO MEDIA TYPE>"
                } else if (retrofitResponse.errorBody()?.contentType()?.toString() == MediaType.TEXT_HTML_VALUE) {
                    "<HTML ERROR>"
                } else {
                    retrofitResponse.errorBody()?.bytes()?.decodeToString() ?: "<NO CONTENT>"
                }

                log.error("External service returns 5xx code. " +
                        "Status code: ${retrofitResponse.code()}. " +
                        "Error content: $errorContent");
                throw ExternalServiceCannotBeInvokedException()
            }

            if (retrofitResponse.code() >= 400) {
                val errorContent = retrofitErrorResponseDeserializer.deserialize(retrofitResponse)

                if (errorToExceptionMapper != null) {
                    throw errorToExceptionMapper.handle(downstreamService, errorContent)
                }

                return IntegrationRetrofitResponse(
                    httpStatusCode = HttpStatus.valueOf(retrofitResponse.code()),
                    errorContent = errorContent
                )
            }

            if (retrofitResponse.code() >= 300) {
                throw NotImplementedError("Handling for 3xx responses from external service not implemented")
            }

            return IntegrationRetrofitResponse(
                httpStatusCode = HttpStatus.valueOf(retrofitResponse.code()),
                content = retrofitResponse.body()
            )
        } catch (e: IOException) {
            log.error("Unexpected IOException during external service invocation", e);
            throw ExternalServiceCannotBeInvokedException()
        }
    }
}
