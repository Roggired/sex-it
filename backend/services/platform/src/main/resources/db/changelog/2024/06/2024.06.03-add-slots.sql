CREATE TABLE slots
(
    id        BIGSERIAL PRIMARY KEY,
    "time"    time                                                     NOT NULL,
    year_id   INT                                                      NOT NULL,
    month_id  INT                                                      NOT NULL,
    day_id    INT                                                      NOT NULL,
    psycho_id BIGINT REFERENCES psycho_profiles (id) ON DELETE CASCADE NOT NULL
);
