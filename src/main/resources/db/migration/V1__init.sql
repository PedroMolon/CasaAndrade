CREATE TABLE flyway_healthcheck (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP DEFAULT now()
);