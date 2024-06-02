CREATE TABLE bbb_meetings
(
    id             BIGSERIAL PRIMARY KEY,
    uuid           uuid                                                     NOT NULL,
    psycho_id      BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    client_id      BIGINT                                                   NOT NULL,
    application_id BIGINT                                                   NOT NULL
)
