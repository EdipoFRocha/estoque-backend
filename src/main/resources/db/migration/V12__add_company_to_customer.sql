ALTER TABLE mvp_clean.customer
ADD COLUMN company_id BIGINT;

UPDATE mvp_clean.customer c
SET company_id = co.id
FROM mvp_clean.company co
WHERE co.name = 'Empresa Local'
  AND c.company_id IS NULL;

ALTER TABLE mvp_clean.customer
ALTER COLUMN company_id SET NOT NULL;

ALTER TABLE mvp_clean.customer
ADD CONSTRAINT fk_customer_company
FOREIGN KEY (company_id) REFERENCES mvp_clean.company (id);

CREATE INDEX idx_customer_company
ON mvp_clean.customer (company_id);
