ALTER TABLE mvp_clean.stock_movement
ADD COLUMN IF NOT EXISTS lot_id BIGINT;
