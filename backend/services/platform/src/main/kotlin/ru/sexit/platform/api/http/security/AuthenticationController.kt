package ru.sexit.platform.api.http.security

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.KeycloakOAuth2AuthenticationService
import java.net.URI

@RestController
@RequestMapping("/api/v1/sso")
class AuthenticationController(
    private val keycloakOAuth2AuthenticationService: KeycloakOAuth2AuthenticationService,
) {
    @GetMapping("/authorize")
    fun authorize(
        redirectUri: String,
    ): ResponseEntity<Any> {
        val authorizeRedirectionUrl = keycloakOAuth2AuthenticationService.getAuthorizeRedirectionUrl(redirectUri)
        return ResponseEntity
            .status(HttpStatus.FOUND)
            .location(URI.create(authorizeRedirectionUrl))
            .build()
    }

    @GetMapping("/logout")
    fun logout(redirectUri: String): ResponseEntity<Any> = ResponseEntity
        .status(HttpStatus.FOUND)
        .location(URI.create(keycloakOAuth2AuthenticationService.getLogoutUrl(redirectUri)))
        .build()

    @PostMapping("/token")
    fun tokenExchange(
        @RequestBody request: TokenExchangeRequest,
    ): KeycloakAccessView = keycloakOAuth2AuthenticationService.tokenExchange(request).toView()

    @PostMapping("/refresh-access")
    fun refreshAccess(
        @RequestBody request: RefreshAccessRequest,
    ): KeycloakAccessView = keycloakOAuth2AuthenticationService.refreshAccess(request).toView()

}
