CREATE TABLE mvp_clean.company (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    document   VARCHAR(50),
    created_at TIMESTAMP NOT NULL DEFAULT now()
);
