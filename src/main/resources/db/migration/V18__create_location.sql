CREATE TABLE IF NOT EXISTS mvp_clean.location (
    id          BIGSERIAL PRIMARY KEY,
    company_id  BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    code        VARCHAR(50) NOT NULL,
    name        VARCHAR(160) NOT NULL,
    active      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMP NOT NULL DEFAULT now()
);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_location_company'
    ) THEN
        ALTER TABLE mvp_clean.location
        ADD CONSTRAINT fk_location_company
        FOREIGN KEY (company_id)
        REFERENCES mvp_clean.company(id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'fk_location_warehouse'
    ) THEN
        ALTER TABLE mvp_clean.location
        ADD CONSTRAINT fk_location_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES mvp_clean.warehouse(id);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS ux_location_company_wh_code
ON mvp_clean.location (company_id, warehouse_id, lower(code));

CREATE INDEX IF NOT EXISTS ix_location_company_wh
ON mvp_clean.location (company_id, warehouse_id);
