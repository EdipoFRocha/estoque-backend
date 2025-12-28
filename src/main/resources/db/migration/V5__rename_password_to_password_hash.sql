ALTER TABLE mvp_clean.app_user
RENAME COLUMN password TO password_hash;
ALTER TABLE mvp_clean.app_user
ALTER COLUMN password_hash TYPE VARCHAR(100);
