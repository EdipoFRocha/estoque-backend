ALTER TABLE mvp_clean.stock_movement
  RENAME COLUMN movement_type TO type;

ALTER TABLE mvp_clean.stock_movement
  RENAME COLUMN product_id TO material_id;

ALTER TABLE mvp_clean.stock_movement
  RENAME COLUMN quantity TO qty;

ALTER TABLE mvp_clean.stock_movement
  ADD COLUMN IF NOT EXISTS warehouse_id BIGINT,
  ADD COLUMN IF NOT EXISTS location_id  BIGINT,
  ADD COLUMN IF NOT EXISTS reason       VARCHAR(120),
  ADD COLUMN IF NOT EXISTS note         VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_stock_movement_company_created
  ON mvp_clean.stock_movement(company_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_stock_movement_company_type
  ON mvp_clean.stock_movement(company_id, type);

CREATE INDEX IF NOT EXISTS idx_stock_movement_company_material
  ON mvp_clean.stock_movement(company_id, material_id);
