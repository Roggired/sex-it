package ru.sexit.platform.infrastructure.exception

import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.sexit.platform.utils.log
import java.util.UUID

@ControllerAdvice
@Order(1)
class ServiceExceptionHandler {
    @ExceptionHandler
    fun handleStateIsNotProvidedException(e: StateIsNotProvidedException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    status = ErrorType.UNAUTHORIZED,
                    description = "state param has not been provided for token-exchange request"
                )
            )

    @ExceptionHandler
    fun handleStateIsInvalidException(e: StateIsInvalidException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    status = ErrorType.UNAUTHORIZED,
                    description = "state param has invalid value"
                )
            )

    @ExceptionHandler
    fun handleAuthorizationCodeIsInvalidException(e: AuthorizationCodeIsInvalidException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                ErrorResponse(
                    status = ErrorType.UNAUTHORIZED,
                    description = "Provided authorization code in not valid",
                )
            )

    @ExceptionHandler
    fun handleKeycloakIsUnavailableException(e: KeycloakIsUnavailableException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                ErrorResponse(
                    status = ErrorType.DOWNSTREAM_SERVICE_IN_UNAVAILABLE,
                    description = "Keycloak is currently unavailable",
                )
            )

    @ExceptionHandler
    fun handleKeycloakAnsweredUnauthorizedException(e: KeycloakAnsweredUnauthorizedException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .build()

    @ExceptionHandler
    fun handleKeycloakAnsweredForbiddenException(e: KeycloakAnsweredForbiddenException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .build()

    @ExceptionHandler
    fun handleDownstreamServiceIsUnavailableException(e: DownstreamServiceIsUnavailableException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.SERVICE_UNAVAILABLE)
            .body(
                ErrorResponse(
                    status = ErrorType.DOWNSTREAM_SERVICE_IN_UNAVAILABLE,
                    description = e.message ?: "Some of downstream microservices is currently unavailable",
                )
            )

    @ExceptionHandler
    fun handleBadRequestFromDownstreamServiceException(e: BadRequestFromDownstreamServiceException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.errorResponse
                ?:
                ErrorResponse(
                    status = e.errorResponse?.status ?: ErrorType.INVALID_DATA,
                    description = "Downstream service: ${e.downstreamService} answered bad request. " +
                            "Most likely, bad data from client"
                )
            )

    @ExceptionHandler
    fun handleAlreadyExistFromDownstreamServiceException(e: AlreadyExistFromDownstreamServiceException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(e.errorResponse
                ?:
                ErrorResponse(
                    status = e.errorResponse?.status ?: ErrorType.ALREADY_EXIST,
                    description = "Downstream service: ${e.downstreamService} answered bad request. " +
                            "Most likely, client data violates unique constraint"
                )
            )

    @ExceptionHandler
    fun handleNotFoundFromDownstreamServiceException(e: NotFoundFromDownstreamServiceException): ResponseEntity<ErrorResponse> {
        if (e.errorResponse != null) {
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.errorResponse)
        }

        val exceptionId = UUID.randomUUID().toString()
        log.error("While requesting downstream service: ${e.downstreamService} got a 404 HTTP response. " +
                "Most likely, the problem connected with invalid request URL. " +
                "Resolve exception as HttpStatus.INTERNAL_SERVER_ERROR. ExceptionID: $exceptionId")
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = ErrorType.UNEXPECTED_EXCEPTION,
                    description = "While requesting downstream service: ${e.downstreamService} got a 404 HTTP response. " +
                            "Most likely, the problem connected with invalid request URL",
                    exceptionId = exceptionId,
                )
            )
    }

    @ExceptionHandler
    fun handleUnauthorizedFromDownstreamServiceException(e: UnauthorizedFromDownstreamServiceException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(e.errorResponse)

    @ExceptionHandler
    fun handleForbiddenFromDownstreamServiceException(e: ForbiddenFromDownstreamServiceException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(e.errorResponse)
}
