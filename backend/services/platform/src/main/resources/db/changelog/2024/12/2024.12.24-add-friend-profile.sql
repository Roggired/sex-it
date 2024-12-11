CREATE TABLE friend_profiles
(
    id            BIGSERIAL PRIMARY KEY,
    user_id       VARCHAR(255) NOT NULL,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    percent       INT          NOT NULL
);

CREATE TABLE friendship
(
    id             BIGSERIAL PRIMARY KEY,
    psycho_id      BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    friend_id      BIGINT REFERENCES friend_profiles (id) ON DELETE CASCADE NOT NULL,
    status         VARCHAR(255) NOT NULL
);

CREATE TABLE referral_program
(
    id                     BIGSERIAL    PRIMARY KEY,
    client_id              VARCHAR(255),
    psycho_id              BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    friend_id              BIGINT REFERENCES friend_profiles (id) ON DELETE CASCADE NOT NULL,
    application_id         BIGINT,
    status                 VARCHAR(255),
    paid_status            VARCHAR(255)
);

ALTER TABLE applications ADD COLUMN refer_id BIGINT;
ALTER TABLE applications ADD COLUMN friend_id BIGINT;
ALTER TABLE applications ADD COLUMN friend_name VARCHAR(255);