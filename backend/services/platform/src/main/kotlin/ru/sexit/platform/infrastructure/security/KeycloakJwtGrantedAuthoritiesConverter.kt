package ru.sexit.platform.infrastructure.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import ru.sexit.platform.utils.log

class KeycloakJwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> {
    fun convertToKeycloakRoles(jwt: Jwt): Collection<KeycloakRoles> {
        val realmAccessClaim = KeycloakTokenClaims.REALM_ACCESS.claimName
        val rolesInRealmAccessClaim = KeycloakTokenClaims.ROLES_IN_REALM_ACCESS.claimName

        if (!jwt.hasClaim(realmAccessClaim)) {
            log.debug("Can't find KEYCLOAK_REALM_ACCESS_CLAIM: $realmAccessClaim in JWT. " +
                    "Therefore, returns an empty collection of granted authorities")
            return emptyList()
        }

        val realmAccess = jwt.getClaimAsMap(realmAccessClaim)
        val rolesListAsObject = realmAccess[rolesInRealmAccessClaim]
        if (rolesListAsObject == null) {
            log.debug("Can't find KEYCLOAK_ROLES_IN_REALM_ACCESS_CLAIM: $rolesInRealmAccessClaim " +
                    "inside $realmAccessClaim claim. " +
                    "Therefore, returns an empty collection of granted authorities")
            return emptyList()
        }

        if (rolesListAsObject !is Collection<*>) {
            log.debug("Founded KEYCLOAK_ROLES_IN_REALM_ACCESS_CLAIM: $rolesInRealmAccessClaim " +
                    "inside $realmAccessClaim claim is not a collection. " +
                    "Therefore, returns an empty collection of granted authorities")
            return emptyList()
        }

        val providedRolesIndex = rolesListAsObject.filterIsInstance<String>().toSet()
        return KeycloakRoles.entries
            .filter { it.toString() in providedRolesIndex }
    }

    override fun convert(jwt: Jwt): Collection<GrantedAuthority> {
        return convertToKeycloakRoles(jwt)
            .flatMap { it.permissions }
            .map { SimpleGrantedAuthority(it.toString()) }
    }
}

