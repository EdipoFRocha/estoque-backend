ALTER TABLE mvp_clean.app_user
ADD COLUMN company_id BIGINT;

INSERT INTO mvp_clean.company (name, document)
VALUES ('Empresa Local', '00000000000000');

UPDATE mvp_clean.app_user u
SET company_id = c.id
FROM mvp_clean.company c
WHERE c.name = 'Empresa Local'
  AND u.company_id IS NULL;

ALTER TABLE mvp_clean.app_user
ALTER COLUMN company_id SET NOT NULL;

ALTER TABLE mvp_clean.app_user
ADD CONSTRAINT fk_app_user_company
FOREIGN KEY (company_id) REFERENCES mvp_clean.company (id);

CREATE UNIQUE INDEX uk_app_user_company_username
ON mvp_clean.app_user (company_id, username);
