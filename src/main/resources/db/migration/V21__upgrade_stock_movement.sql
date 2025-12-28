ALTER TABLE mvp_clean.stock_movement
  ADD COLUMN IF NOT EXISTS company_id  BIGINT,
  ADD COLUMN IF NOT EXISTS material_id BIGINT,
  ADD COLUMN IF NOT EXISTS location_id BIGINT,
  ADD COLUMN IF NOT EXISTS document_ref VARCHAR(120),
  ADD COLUMN IF NOT EXISTS notes        VARCHAR(255);

UPDATE mvp_clean.stock_movement
SET company_id = (SELECT id FROM mvp_clean.company ORDER BY id LIMIT 1)
WHERE company_id IS NULL;

ALTER TABLE mvp_clean.stock_movement
  ALTER COLUMN company_id SET NOT NULL;

DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_movement_company') THEN
    ALTER TABLE mvp_clean.stock_movement
      ADD CONSTRAINT fk_movement_company
      FOREIGN KEY (company_id) REFERENCES mvp_clean.company(id);
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_movement_material') THEN
    ALTER TABLE mvp_clean.stock_movement
      ADD CONSTRAINT fk_movement_material
      FOREIGN KEY (material_id) REFERENCES mvp_clean.material(id);
  END IF;

  IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_movement_location') THEN
    ALTER TABLE mvp_clean.stock_movement
      ADD CONSTRAINT fk_movement_location
      FOREIGN KEY (location_id) REFERENCES mvp_clean.location(id);
  END IF;
END $$;

CREATE INDEX IF NOT EXISTS ix_movement_company_created
  ON mvp_clean.stock_movement(company_id, created_at DESC);

CREATE INDEX IF NOT EXISTS ix_movement_company_material
  ON mvp_clean.stock_movement(company_id, material_id);

CREATE INDEX IF NOT EXISTS ix_movement_company_location
  ON mvp_clean.stock_movement(company_id, location_id);
