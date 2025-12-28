CREATE TABLE IF NOT EXISTS mvp_clean.stock_movement (
  id            BIGSERIAL PRIMARY KEY,
  product_id    BIGINT,
  movement_type VARCHAR(40) NOT NULL,
  quantity      NUMERIC(18,3) NOT NULL,
  created_at    TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
