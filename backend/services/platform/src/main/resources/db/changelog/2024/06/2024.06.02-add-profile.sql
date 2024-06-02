CREATE TABLE psycho_profiles
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    price         INT          NOT NULL CHECK ( price > 0 ),
    is_first_free BOOLEAN      NOT NULL,
    bio           TEXT         NOT NULL
);

INSERT INTO psycho_profiles(name, email, price, is_first_free, bio) VALUES ('Алла Сергеевна', 'alla.sergeevna@example.com', 2000, TRUE, 'Я супер крутая бабуля!!!');
