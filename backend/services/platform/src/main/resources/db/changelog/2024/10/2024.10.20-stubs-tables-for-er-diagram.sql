CREATE TABLE users
(
    id         BIGSERIAL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL,
    password   TEXT         NOT NULL,
    created_at TIMESTAMP    NOT NULL
);

CREATE TABLE user_roles
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    role    VARCHAR(63)                                    NOT NULL
);

ALTER TABLE psycho_profiles
    ADD COLUMN user_id BIGINT REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE psycho_profiles ALTER COLUMN user_id SET NOT NULL ;

ALTER TABLE applications
    ADD COLUMN user_id BIGINT REFERENCES users (id) ON DELETE CASCADE;
ALTER TABLE applications ALTER COLUMN user_id SET NOT NULL ;

ALTER TABLE bbb_meetings
    ADD CONSTRAINT fk_meeting_client FOREIGN KEY (client_id) REFERENCES users (id) ON DELETE CASCADE;

CREATE TABLE friendships
(
    id           BIGSERIAL PRIMARY KEY,
    psycho_id    BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    friend_id    BIGINT REFERENCES users (id) ON DELETE CASCADE           NOT NULL,
    created_at   TIMESTAMP                                                NOT NULL,
    approved_at  TIMESTAMP,
    discarded_at TIMESTAMP
);

CREATE TABLE referral_links
(
    id             BIGSERIAL PRIMARY KEY,
    psycho_id      BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    friend_id      BIGINT REFERENCES users (id) ON DELETE CASCADE           NOT NULL,
    activation_key TEXT                                                     NOT NULL,
    used_at        TIMESTAMP,
    payed_at       TIMESTAMP
);

CREATE TABLE subscriptions
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT REFERENCES users (id) ON DELETE CASCADE NOT NULL,
    type        VARCHAR(31) NOT NULL,
    valid_until TIMESTAMP   NOT NULL
);

CREATE TABLE subscription_pays
(
    id              BIGSERIAL PRIMARY KEY,
    sum             DOUBLE PRECISION                                       NOT NULL,
    payed_at        TIMESTAMP                                              NOT NULL,
    subscription_id BIGINT REFERENCES subscriptions (id) ON DELETE CASCADE NOT NULL
);

-- password == 12345
INSERT INTO users(email, password, created_at) values ('alla.sergeevna@example.com', '3627909a29c31381a071ec27f7c9ca97726182aed29a7ddd2e54353322cfb30abb9e3a6df2ac2c20fe23436311d678564d0c8d305930575f60e2d3d048184d79', now());
INSERT INTO user_roles(user_id, role) values (1, 'PSYCHO');
INSERT INTO psycho_profiles(name, email, price, is_first_free, bio, user_id) VALUES ('Алла Сергеевна', 'alla.sergeevna@example.com', 2000, TRUE, 'Я супер крутая бабуля!!!', 1);
