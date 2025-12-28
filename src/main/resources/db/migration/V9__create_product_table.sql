CREATE TABLE IF NOT EXISTS mvp_clean.product (
  id         BIGSERIAL PRIMARY KEY,
  code       VARCHAR(60) NOT NULL,
  name       VARCHAR(160) NOT NULL,
  unit       VARCHAR(10),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_active  BOOLEAN NOT NULL DEFAULT TRUE
);
