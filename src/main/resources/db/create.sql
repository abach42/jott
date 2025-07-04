CREATE EXTENSION pgcrypto;

CREATE TABLE application_user (
    id BIGSERIAL PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    customer_user_id VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

INSERT INTO application_user (user_name, customer_user_id, password, email)
VALUES 
('john_doe', 'JD123', '{noop}password', 'john.doe@example.com'),
('jane_smith', 'JS456', '{noop}password', 'jane.smith@example.com');
