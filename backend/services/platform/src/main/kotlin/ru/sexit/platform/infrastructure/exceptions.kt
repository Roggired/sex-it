package ru.sexit.platform.infrastructure

class AlreadyExistException(override val message: String? = null) : RuntimeException(message)
class NotFoundException(override val message: String? = null) : RuntimeException(message)
class InvalidDataException(override val message: String? = null) : RuntimeException(message)
class InvalidOperationException(override val message: String? = null) : RuntimeException(message)
class InternalServerException(override val message: String? = null) : RuntimeException(message)
class BbbIntegrationException(override val message: String? = null) : RuntimeException(message)
