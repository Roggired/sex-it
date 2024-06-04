CREATE TABLE applications
(
    id            BIGSERIAL PRIMARY KEY,
    slot_id       BIGINT REFERENCES slots (id) NOT NULL,
    creation_time TIMESTAMP                    NOT NULL,
    anon_type     TEXT,
    visit_type    TEXT,
    status        TEXT,
    description   TEXT,
    link          VARCHAR(255),
    address       VARCHAR(255),
    results       TEXT
);