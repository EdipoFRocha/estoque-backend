ALTER TABLE mvp_clean.warehouse
ADD COLUMN IF NOT EXISTS active BOOLEAN;

UPDATE mvp_clean.warehouse
SET active = TRUE
WHERE active IS NULL;

ALTER TABLE mvp_clean.warehouse
ALTER COLUMN active SET DEFAULT TRUE;

ALTER TABLE mvp_clean.warehouse
ALTER COLUMN active SET NOT NULL;
