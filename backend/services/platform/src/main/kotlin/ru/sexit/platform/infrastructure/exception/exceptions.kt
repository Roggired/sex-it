package ru.sexit.platform.infrastructure.exception

import ru.sexit.platform.infrastructure.integration.DownstreamServices

class AlreadyExistException(override val message: String? = null) : RuntimeException(message)
class NotFoundException(override val message: String? = null) : RuntimeException(message)
class InvalidDataException(override val message: String? = null) : RuntimeException(message)
class InvalidOperationException(override val message: String? = null) : RuntimeException(message)
class InternalServerException(override val message: String? = null) : RuntimeException(message)
class BbbIntegrationException(override val message: String? = null) : RuntimeException(message)

class StateIsNotProvidedException(): RuntimeException()
class StateIsInvalidException(): RuntimeException()
class AuthorizationCodeIsInvalidException(): RuntimeException()
class KeycloakAnsweredUnauthorizedException(): RuntimeException()
class KeycloakAnsweredForbiddenException(): RuntimeException()
class KeycloakIsUnavailableException(): RuntimeException()

abstract class AbstractDownstreamServiceException(
    msg: String? = null
): RuntimeException(msg)

class DownstreamServiceIsUnavailableException(msg: String): AbstractDownstreamServiceException(msg)
class BadRequestFromDownstreamServiceException(
    val downstreamService: DownstreamServices? = null,
    val errorResponse: ErrorResponse?
): AbstractDownstreamServiceException()
class NotFoundFromDownstreamServiceException(
    val downstreamService: DownstreamServices? = null,
    val errorResponse: ErrorResponse?
): AbstractDownstreamServiceException()
class AlreadyExistFromDownstreamServiceException(
    val downstreamService: DownstreamServices? = null,
    val errorResponse: ErrorResponse?
): AbstractDownstreamServiceException()
class UnauthorizedFromDownstreamServiceException(
    val downstreamService: DownstreamServices? = null,
    val errorResponse: ErrorResponse?
): AbstractDownstreamServiceException()
class ForbiddenFromDownstreamServiceException(
    val downstreamService: DownstreamServices? = null,
    val errorResponse: ErrorResponse?
): AbstractDownstreamServiceException()
