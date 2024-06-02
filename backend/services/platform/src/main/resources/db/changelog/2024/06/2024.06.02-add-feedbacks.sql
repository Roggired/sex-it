CREATE TABLE feedbacks
(
    id            BIGSERIAL PRIMARY KEY,
    creation_time TIMESTAMP                                                NOT NULL,
    rating        INT                                                      NOT NULL,
    "text"        TEXT                                                     NOT NULL,
    psycho_id     BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL
);
