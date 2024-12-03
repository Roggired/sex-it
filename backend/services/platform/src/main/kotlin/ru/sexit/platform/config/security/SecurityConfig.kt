package ru.sexit.platform.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import ru.sexit.platform.config.properties.DevSecurityProperties
import ru.sexit.platform.config.properties.KeycloakSecurityProperties
import ru.sexit.platform.infrastructure.security.DevApiKeySecurityFilter
import ru.sexit.platform.infrastructure.security.KeycloakJwtGrantedAuthoritiesConverter
import ru.sexit.platform.infrastructure.security.SexItPermissions

@Configuration
class SecurityConfig(
    private val keycloakProperties: KeycloakSecurityProperties,
    private val devSecurityProperties: DevSecurityProperties,
) {
    @Bean
    fun httpSecurity(http: HttpSecurity): SecurityFilterChain {
        http {
            csrf { disable() }
            cors {  }
            sessionManagement { sessionCreationPolicy = SessionCreationPolicy.STATELESS }

            authorizeHttpRequests {
                authorize("/api/v1/sso/**", permitAll)
                authorize(HttpMethod.POST, "/api/v1/applications", hasAuthority(SexItPermissions.CLIENT.name))
                authorize(HttpMethod.GET, "/api/v1/applications", hasAnyAuthority(SexItPermissions.PSYCHO.name, SexItPermissions.CLIENT.name))
                authorize("/api/v1/applications/reject", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/applications/accept", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/applications/accepted", hasAuthority(SexItPermissions.CLIENT.name))
                authorize("/api/v1/applications/*/give-feedback", hasAuthority(SexItPermissions.CLIENT.name))
                authorize("/api/v1/applications/*/note", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/applications/*/finish", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/dev-only/meetings", hasAnyAuthority(SexItPermissions.PSYCHO.name, SexItPermissions.CLIENT.name))
                authorize(HttpMethod.PATCH, "/api/v1/profiles/*", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize(HttpMethod.GET, "/api/v1/profiles/*", hasAnyAuthority(SexItPermissions.PSYCHO.name, SexItPermissions.CLIENT.name))
                authorize(HttpMethod.POST, "/api/v1/profiles/filtered", hasAuthority(SexItPermissions.CLIENT.name))
                authorize(HttpMethod.POST, "/api/v1/slots", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize(HttpMethod.DELETE, "/api/v1/slots/*", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/slots/*", hasAnyAuthority(SexItPermissions.PSYCHO.name, SexItPermissions.CLIENT.name))
                authorize("/api/v1/subscriptions", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/subscriptions/*", hasAuthority(SexItPermissions.PSYCHO.name))
                authorize("/api/v1/feedbacks/last-ten-by-psycho", hasAuthority(SexItPermissions.CLIENT.name))

                authorize("/swagger-ui/*", permitAll)
                authorize("/v3/api-docs/**", permitAll)

                authorize(anyRequest, authenticated)
            }
            oauth2ResourceServer {
                jwt {
                    jwkSetUri = keycloakProperties.getJwksUrl()
                    jwtAuthenticationConverter = JwtAuthenticationConverter()
                        .apply {
                            setJwtGrantedAuthoritiesConverter(
                                KeycloakJwtGrantedAuthoritiesConverter()
                            )
                        }
                }
            }

            addFilterBefore<BearerTokenAuthenticationFilter>(DevApiKeySecurityFilter(devSecurityProperties))
        }
        return http.build()
    }
}
