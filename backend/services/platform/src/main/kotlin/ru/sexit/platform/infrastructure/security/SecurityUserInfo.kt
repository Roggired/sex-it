package ru.sexit.platform.infrastructure.security

const val ANONYMOUS_USER_ID = "00000000-0000-0000-0000-000000000000"
const val ANONYMOUS_USER_USERNAME = "anonymous-username"
const val ANONYMOUS_USER_EMAIL = "anonymous@anonymous.com"
const val ANONYMOUS_USER_GIVEN_NAME = "Anonymous"
const val ANONYMOUS_USER_FAMILY_NAME = "Anon"

/**
 * Helper DTO object which represents information about the user retrieved from provided credentials
 *
 * @author ego
 */
data class SecurityUserInfo(
    val id: String,
    val username: String,
    val email: String,
    val firstName: String?,
    val lastName: String?,
    val roles: Collection<KeycloakRoles>,
    val permissions: Collection<SexItPermissions> = roles.flatMap { it.permissions },
    val isAnonymous: Boolean = false,
) {
    companion object {
        fun anonymous(): SecurityUserInfo = SecurityUserInfo(
            id = ANONYMOUS_USER_ID,
            username = ANONYMOUS_USER_USERNAME,
            email = ANONYMOUS_USER_EMAIL,
            firstName = ANONYMOUS_USER_GIVEN_NAME,
            lastName = ANONYMOUS_USER_FAMILY_NAME,
            roles = emptyList(),
            isAnonymous = true,
        )
    }
}
