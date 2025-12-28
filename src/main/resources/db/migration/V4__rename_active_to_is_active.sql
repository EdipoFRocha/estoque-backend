ALTER TABLE mvp_clean.app_user
RENAME COLUMN active TO is_active;

ALTER TABLE mvp_clean.app_user
ALTER COLUMN is_active SET DEFAULT TRUE;

UPDATE mvp_clean.app_user
SET is_active = TRUE
WHERE is_active IS NULL;
