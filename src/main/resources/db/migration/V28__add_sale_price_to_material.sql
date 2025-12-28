ALTER TABLE mvp_clean.material
ADD COLUMN sale_price NUMERIC(12,2);

CREATE INDEX IF NOT EXISTS idx_material_sale_price
ON mvp_clean.material (sale_price);
