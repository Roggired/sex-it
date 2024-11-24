CREATE TABLE subscriptions
(
    id          BIGSERIAL PRIMARY KEY,
    psycho_id VARCHAR(255) NOT NULL,
    type        VARCHAR(31) NOT NULL,
    valid_until TIMESTAMP   NOT NULL,
    paid_at   TIMESTAMP NOT NULL,
    suspended BOOLEAN   NOT NULL
);
