CREATE TABLE files
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    path VARCHAR NOT NULL UNIQUE
);

CREATE TABLE genres
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE films
(
    id                  SERIAL PRIMARY KEY,
    name                VARCHAR                    NOT NULL,
    description         VARCHAR                    NOT NULL,
    "year"              INT                        NOT NULL,
    genre_id            INT REFERENCES genres (id) NOT NULL,
    minimal_age         INT                        NOT NULL,
    duration_in_minutes INT                        NOT NULL,
    file_id             INT REFERENCES files (id)  NOT NULL
);

CREATE TABLE halls
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR NOT NULL,
    row_count   INT     NOT NULL,
    place_count INT     NOT NULL,
    description VARCHAR NOT NULL
);

CREATE TABLE film_sessions
(
    id         SERIAL PRIMARY KEY,
    film_id    INT REFERENCES films (id) NOT NULL,
    halls_id   INT REFERENCES halls (id) NOT NULL,
    start_time TIMESTAMP                 NOT NULL,
    end_time   TIMESTAMP                 NOT NULL,
    price      INT                       NOT NULL
);

CREATE TABLE users
(
    id        SERIAL PRIMARY KEY,
    full_name VARCHAR        NOT NULL,
    email     VARCHAR UNIQUE NOT NULL,
    password  VARCHAR        NOT NULL
);

CREATE TABLE tickets
(
    id           SERIAL PRIMARY KEY,
    session_id   INT REFERENCES film_sessions (id) NOT NULL,
    row_number   INT                               NOT NULL,
    place_number INT                               NOT NULL,
    user_id      INT                               NOT NULL,
    UNIQUE (session_id, row_number, place_number)
);