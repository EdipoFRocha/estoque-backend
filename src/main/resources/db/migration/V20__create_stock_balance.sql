CREATE TABLE IF NOT EXISTS mvp_clean.stock_balance (
    id          BIGSERIAL PRIMARY KEY,
    company_id  BIGINT NOT NULL,
    material_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    qty         NUMERIC(18,3) NOT NULL DEFAULT 0,
    updated_at  TIMESTAMP NOT NULL DEFAULT now()
);

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_balance_company') THEN
        ALTER TABLE mvp_clean.stock_balance
        ADD CONSTRAINT fk_balance_company
        FOREIGN KEY (company_id)
        REFERENCES mvp_clean.company(id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_balance_material') THEN
        ALTER TABLE mvp_clean.stock_balance
        ADD CONSTRAINT fk_balance_material
        FOREIGN KEY (material_id)
        REFERENCES mvp_clean.material(id);
    END IF;
END $$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_constraint WHERE conname = 'fk_balance_location') THEN
        ALTER TABLE mvp_clean.stock_balance
        ADD CONSTRAINT fk_balance_location
        FOREIGN KEY (location_id)
        REFERENCES mvp_clean.location(id);
    END IF;
END $$;

CREATE UNIQUE INDEX IF NOT EXISTS ux_balance_company_material_location
ON mvp_clean.stock_balance(company_id, material_id, location_id);

CREATE INDEX IF NOT EXISTS ix_balance_company_material
ON mvp_clean.stock_balance(company_id, material_id);

CREATE INDEX IF NOT EXISTS ix_balance_company_location
ON mvp_clean.stock_balance(company_id, location_id);
