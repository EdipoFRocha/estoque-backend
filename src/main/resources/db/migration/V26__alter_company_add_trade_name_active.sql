ALTER TABLE company
ADD COLUMN IF NOT EXISTS trade_name VARCHAR(255);

ALTER TABLE company
ADD COLUMN IF NOT EXISTS active BOOLEAN NOT NULL DEFAULT TRUE;

UPDATE company
SET active = TRUE
WHERE active IS NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uk_company_name'
    ) THEN
        ALTER TABLE company
        ADD CONSTRAINT uk_company_name UNIQUE (name);
    END IF;
END$$;
