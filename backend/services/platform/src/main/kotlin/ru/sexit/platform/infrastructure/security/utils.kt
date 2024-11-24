package ru.sexit.platform.infrastructure.security

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

/**
 * Helper function which returns [SecurityUserInfo] fetched from current Spring's SecurityContext.
 * If an authentication object retrieved from SecurityContext is null or not an instance of
 * [UserInfoAuthentication] then this function returns [SecurityUserInfo.Companion.anonymous] DTO object.
 *
 * @author ego
 */
fun getRequestAuthorUserInfo(): SecurityUserInfo {
    val authentication = SecurityContextHolder.getContext()?.authentication
    if (authentication == null || (authentication !is UserInfoAuthentication && authentication !is JwtAuthenticationToken)) {
        return SecurityUserInfo.anonymous()
    }

    if (authentication is UserInfoAuthentication) {
        return SecurityUserInfo(
            id = authentication.userId,
            username = authentication.preferredUsername,
            email = authentication.email,
            firstName = authentication.givenName,
            lastName = authentication.familyName,
            roles = authentication.roles,
        )
    }

    authentication as JwtAuthenticationToken
    //TODO: performance???
    return SecurityUserInfo(
        id = authentication.name,
        username = authentication.token.getClaimAsString(KeycloakTokenClaims.PREFERRED_USERNAME.claimName),
        email = authentication.token.getClaimAsString(KeycloakTokenClaims.EMAIL.claimName),
        firstName = authentication.token.getClaimAsString(KeycloakTokenClaims.GIVEN_NAME.claimName),
        lastName = authentication.token.getClaimAsString(KeycloakTokenClaims.FAMILY_NAME.claimName),
        roles = KeycloakJwtGrantedAuthoritiesConverter().convertToKeycloakRoles(authentication.token)
    )
}
