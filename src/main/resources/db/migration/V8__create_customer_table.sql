CREATE TABLE IF NOT EXISTS mvp_clean.customer (
  id         BIGSERIAL PRIMARY KEY,
  name       VARCHAR(160) NOT NULL,
  document   VARCHAR(50),
  created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  is_active  BOOLEAN NOT NULL DEFAULT TRUE
);
