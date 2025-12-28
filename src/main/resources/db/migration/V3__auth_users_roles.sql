CREATE TABLE IF NOT EXISTS mvp_clean.app_user (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    full_name VARCHAR(120),
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS mvp_clean.app_role (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS mvp_clean.app_user_role (
    user_id BIGINT NOT NULL REFERENCES mvp_clean.app_user(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES mvp_clean.app_role(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

INSERT INTO mvp_clean.app_role (name) VALUES
('GERENTE'),
('RH'),
('LOGISTICA'),
('SUPERVISAO'),
('OPERADOR')
ON CONFLICT DO NOTHING;
