package ru.sexit.platform.infrastructure.integration.keycloak.authentication.entity

import java.time.LocalDateTime
import jakarta.persistence.*

@Entity
@Table(name = "keycloak_generated_states")
class KeycloakStateEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val state: String,
    val activeBefore: LocalDateTime,
    val isUsed: Boolean,
)
