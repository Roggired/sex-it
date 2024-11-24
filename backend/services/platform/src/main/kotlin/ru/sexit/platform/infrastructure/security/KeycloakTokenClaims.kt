package ru.sexit.platform.infrastructure.security

enum class KeycloakTokenClaims(
    val claimName: String
) {
    PREFERRED_USERNAME("preferred_username"),
    EMAIL("email"),
    GIVEN_NAME("given_name"),
    FAMILY_NAME("family_name"),
    REALM_ACCESS("realm_access"),
    ROLES_IN_REALM_ACCESS("roles"),
    ;
}
