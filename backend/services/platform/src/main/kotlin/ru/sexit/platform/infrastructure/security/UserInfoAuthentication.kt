package ru.sexit.platform.infrastructure.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority

class UserInfoAuthentication(
    val userId: String,
    val preferredUsername: String,
    val email: String,
    val givenName: String?,
    val familyName: String?,
    val roles: Collection<KeycloakRoles>,
    permissions: Collection<SexItPermissions> = roles.flatMap { it.permissions },
): AbstractAuthenticationToken(
    permissions.map { SimpleGrantedAuthority(it.name) }
) {
    override fun getCredentials(): Any {
        throw NotImplementedError("Credentials are not supported for UserInfoAuthentication")
    }

    override fun getPrincipal(): String {
        return userId
    }
}
