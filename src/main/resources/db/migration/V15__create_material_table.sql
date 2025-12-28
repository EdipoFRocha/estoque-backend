CREATE TABLE IF NOT EXISTS mvp_clean.material (
  id           BIGSERIAL PRIMARY KEY,
  company_id   BIGINT NOT NULL,
  code         VARCHAR(60) NOT NULL,
  name         VARCHAR(160) NOT NULL,
  unit         VARCHAR(10) NOT NULL,
  description  VARCHAR(255),

  active       BOOLEAN NOT NULL DEFAULT TRUE,
  created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_material_company
    FOREIGN KEY (company_id)
    REFERENCES mvp_clean.company(id)
);

-- Evitar duplicidade por (empresa + código)
CREATE UNIQUE INDEX IF NOT EXISTS ux_material_company_code
  ON mvp_clean.material (company_id, code);

-- Ajuda nas listagens/pesquisas
CREATE INDEX IF NOT EXISTS ix_material_company_name
  ON mvp_clean.material (company_id, name);
