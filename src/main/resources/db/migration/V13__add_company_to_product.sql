ALTER TABLE mvp_clean.stock_movement
ADD COLUMN IF NOT EXISTS company_id BIGINT;

UPDATE mvp_clean.stock_movement sm
SET company_id = c.id
FROM mvp_clean.company c
WHERE c.id = (SELECT id FROM mvp_clean.company ORDER BY id LIMIT 1)
  AND sm.company_id IS NULL;

ALTER TABLE mvp_clean.stock_movement
ALTER COLUMN company_id SET NOT NULL;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_constraint WHERE conname = 'fk_stock_movement_company'
  ) THEN
    ALTER TABLE mvp_clean.stock_movement
      ADD CONSTRAINT fk_stock_movement_company
      FOREIGN KEY (company_id) REFERENCES mvp_clean.company (id);
  END IF;
END $$;

CREATE INDEX IF NOT EXISTS idx_stock_movement_company
ON mvp_clean.stock_movement (company_id);

