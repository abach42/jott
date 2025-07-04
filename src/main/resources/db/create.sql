CREATE EXTENSION pgcrypto;

CREATE TABLE application_user (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    identifier VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    role smallint NOT NULL
);

INSERT INTO application_user (user_name, identifier, password, email, role)
VALUES
('john_doe', 'ADM1', '{noop}password', 'john.doe@example.com', 0),
('jane_smith', 'USR1', '{noop}password', 'jane.smith@example.com', 1);
