package ru.sexit.platform.infrastructure.integration.keycloak.authentication

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.sexit.platform.infrastructure.integration.keycloak.authentication.entity.KeycloakStateEntity

@Repository
interface KeycloakStateEntityRepository : JpaRepository<KeycloakStateEntity, Long> {
    fun findByState(state: String): KeycloakStateEntity?
}
