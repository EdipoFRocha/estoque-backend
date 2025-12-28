DROP INDEX IF EXISTS mvp_clean.ux_material_sku;

CREATE UNIQUE INDEX IF NOT EXISTS ux_material_company_sku
ON mvp_clean.material (company_id, sku);
