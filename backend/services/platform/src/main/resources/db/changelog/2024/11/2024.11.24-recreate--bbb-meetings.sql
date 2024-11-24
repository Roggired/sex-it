DROP TABLE IF EXISTS bbb_meetings;
CREATE TABLE bbb_meetings
(
    id                BIGSERIAL PRIMARY KEY,
    "uuid"            uuid                                                     NOT NULL,
    psycho_profile_id BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL,
    application_id    BIGINT REFERENCES applications (id) ON DELETE CASCADE    NOT NULL
)
