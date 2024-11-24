package ru.sexit.platform.infrastructure.security

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import ru.sexit.platform.config.properties.DevSecurityProperties
import ru.sexit.platform.config.properties.DevUserProperties

/**
 * DevApiKeySecurityFilter is designed to provide an additional security schema (API KEY) to be used
 * for dev profile only. This Filter MUST be inserted into Spring SecurityFilterChain **before**
 * [org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter].
 *
 * ```kotlin
 * @Configuration
 * @Profile("dev")
 * class DevSpringSecurityConfig {
 *      @Bean
 *      fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
 *          http {
 *              addFilterBefore<BearerTokenAuthenticationFilter>(DevApiKeySecurityFilter())
 *          }
 *          return http.build()
 *      }
 * }
 * ```
 *
 * For more information about how to configure params of [DevSecurityProperties] see docs/dev-authentication.md
 *
 * @author ego
 */
class DevApiKeySecurityFilter(
    private val devSecurityProperties: DevSecurityProperties,
) : Filter {
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpRequest = request!! as HttpServletRequest
        val httpResponse = response!! as HttpServletResponse
        val filterChain = chain!!

        val apiKey = httpRequest.getHeader(devSecurityProperties.apiKeyHeader)
        val authentication = createAuthenticationBasedOnApikey(apiKey)
        if (authentication == null) {
            filterChain.doFilter(httpRequest, httpResponse)
            return
        }

        val securityContext = SecurityContextHolder.getContext()
        authentication.isAuthenticated = true
        securityContext.authentication = authentication
        filterChain.doFilter(httpRequest, httpResponse)
    }

    private fun createAuthenticationBasedOnApikey(apiKey: String?): UserInfoAuthentication? = when(apiKey) {
        devSecurityProperties.psycho.apiKey -> convertDevUserPropertiesToUserInfoAuthentication(
            devUserProperties = devSecurityProperties.psycho,
            roles = listOf(KeycloakRoles.PSYCHO),
        )
        devSecurityProperties.psychoFriend.apiKey -> convertDevUserPropertiesToUserInfoAuthentication(
            devUserProperties = devSecurityProperties.psychoFriend,
            roles = listOf(KeycloakRoles.PSYCHO_FRIEND)
        )
        devSecurityProperties.client.apiKey -> convertDevUserPropertiesToUserInfoAuthentication(
            devUserProperties = devSecurityProperties.client,
            roles = listOf(KeycloakRoles.CLIENT)
        )
        devSecurityProperties.admin.apiKey -> convertDevUserPropertiesToUserInfoAuthentication(
            devUserProperties = devSecurityProperties.admin,
            roles = listOf(KeycloakRoles.ADMIN)
        )
        else -> null
    }

    private fun convertDevUserPropertiesToUserInfoAuthentication(
        devUserProperties: DevUserProperties,
        roles: List<KeycloakRoles>,
    ): UserInfoAuthentication = UserInfoAuthentication(
        userId = devUserProperties.id,
        preferredUsername = devUserProperties.username,
        email = devUserProperties.email,
        givenName = devUserProperties.firstName,
        familyName = devUserProperties.lastName,
        roles = roles
    )
}
