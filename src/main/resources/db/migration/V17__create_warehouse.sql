CREATE TABLE IF NOT EXISTS mvp_clean.warehouse (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(160) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE mvp_clean.warehouse
ADD COLUMN IF NOT EXISTS company_id BIGINT;

UPDATE mvp_clean.warehouse
SET company_id = (SELECT id FROM mvp_clean.company ORDER BY id LIMIT 1)
WHERE company_id IS NULL;

ALTER TABLE mvp_clean.warehouse
ALTER COLUMN company_id SET NOT NULL;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_warehouse_company'
    ) THEN
        ALTER TABLE mvp_clean.warehouse
        ADD CONSTRAINT fk_warehouse_company
        FOREIGN KEY (company_id)
        REFERENCES mvp_clean.company(id);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS ux_warehouse_company_code
ON mvp_clean.warehouse (company_id, lower(code));
