CREATE TABLE keycloak_generated_states
(
    id            BIGSERIAL PRIMARY KEY,
    state         TEXT      NOT NULL,
    active_before TIMESTAMP NOT NULL,
    is_used       BOOLEAN   NOT NULL
);
