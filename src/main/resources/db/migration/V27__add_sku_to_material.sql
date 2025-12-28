ALTER TABLE mvp_clean.material
ADD COLUMN sku VARCHAR(40);

UPDATE mvp_clean.material
SET sku = 'SKU-' || company_id || '-' || LPAD(id::text, 6, '0')
WHERE sku IS NULL;

ALTER TABLE mvp_clean.material
ALTER COLUMN sku SET NOT NULL;

CREATE UNIQUE INDEX IF NOT EXISTS ux_material_sku
ON mvp_clean.material(sku);
