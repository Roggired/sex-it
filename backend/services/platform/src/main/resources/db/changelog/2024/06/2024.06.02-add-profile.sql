CREATE TABLE psycho_profiles
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    price         INT          NOT NULL CHECK ( price > 0 ),
    is_first_free BOOLEAN      NOT NULL,
    bio           TEXT         NOT NULL
);
