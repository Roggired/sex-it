package ru.sexit.platform.infrastructure.exception

import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import ru.sexit.platform.utils.log

enum class ErrorType {
    INVALID_DATA,
    INVALID_OPERATION,
    FORBIDDEN,
    BODY_NOT_READABLE,
    ALREADY_EXIST,
    NOT_FOUND,
    UNEXPECTED_EXCEPTION,
    BBB_EXCEPTION,
    UNAUTHORIZED,
    DOWNSTREAM_SERVICE_IN_UNAVAILABLE,
    ;
}

class ErrorResponse(
    val status: ErrorType,
    val description: String?,
    val exceptionId: String? = null,
)

@ControllerAdvice
@Order(2)
class ExceptionHandler {
    @ExceptionHandler
    fun handleBbbIntegrationException(e: BbbIntegrationException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = ErrorType.BBB_EXCEPTION,
                    description = e.message,
                )
            ).also { log.error("BBB integration exception occurred", e) }

    @ExceptionHandler
    fun handleMissingServletRequestParameter(e: MissingServletRequestParameterException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.INVALID_DATA,
                    description = e.message,
                )
            ).also { log.debug("MissingServletRequestParameterException has occurred", e) }

    @ExceptionHandler
    fun handleForbidden(e: AccessDeniedException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(
                ErrorResponse(
                    status = ErrorType.FORBIDDEN,
                    description = e.message ?: "Forbidden"
                )
            ).also { log.debug("AccessDeniedException has occurred", e) }

    @ExceptionHandler
    fun handleMethodArgumentNotValid(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.BODY_NOT_READABLE,
                    description = "Invalid request data",
                )
            ).also { log.debug("MethodArgumentNotValidException has occurred", e) }

    @ExceptionHandler
    fun handleMismatchInput(e: MismatchedInputException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.BODY_NOT_READABLE,
                    description = e.message ?: "Invalid request data",
                )
            ).also { log.debug("MismatchedInputException has occurred", e) }

    @ExceptionHandler
    fun handleHttpMessageNotReadable(e: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.BODY_NOT_READABLE,
                    description = e.message ?: "Invalid request data",
                )
            ).also { log.debug("HttpMessageNotReadableException has occurred", e) }
    }

    @ExceptionHandler
    fun handleInvalidData(e: InvalidDataException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.INVALID_DATA,
                    description = e.message ?: "Invalid request data",
                )
            ).also { log.debug("InvalidDataException has occurred", e) }

    @ExceptionHandler
    fun handleAlreadyExists(e: AlreadyExistException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .badRequest()
            .body(
                ErrorResponse(
                    status = ErrorType.ALREADY_EXIST,
                    description = e.message ?: "Resource already exists",
                )
            ).also { log.debug("AlreadyExistException has occurred", e) }

    @ExceptionHandler
    fun handleNotFound(e: NotFoundException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    status = ErrorType.NOT_FOUND,
                    description = e.message ?: "Requested resource not found",
                )
            ).also { log.debug("NotFoundException has occurred", e) }

    @ExceptionHandler
    fun handleInvalidOperation(e: InvalidOperationException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = ErrorType.INVALID_OPERATION,
                    description = e.message ?: "This operation is invalid"
                )
            ).also { log.debug("InvalidOperationException has occurred", e) }

    @ExceptionHandler
    fun handleMethodArgumentTypeMismatch(e: MethodArgumentTypeMismatchException): ResponseEntity<ErrorResponse> =
        ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    status = ErrorType.INVALID_DATA,
                    description = e.message ?: "Method argument type is invalid"
                )
            ).also { log.debug("MethodArgumentTypeMismatchException has occurred", e) }

    @ExceptionHandler
    fun handleOthersRuntime(e: RuntimeException): ResponseEntity<Any> {
        log.error("Unexpected exception", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = ErrorType.UNEXPECTED_EXCEPTION,
                    description = "Internal error",
                )
            )
    }

    @ExceptionHandler
    fun handleOthers(e: Exception): ResponseEntity<Any> {
        log.error("Unexpected exception", e)
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ErrorResponse(
                    status = ErrorType.UNEXPECTED_EXCEPTION,
                    description = "Internal error",
                )
            )
    }
}
