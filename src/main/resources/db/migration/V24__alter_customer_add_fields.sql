ALTER TABLE mvp_clean.customer
  ADD COLUMN IF NOT EXISTS type varchar(2),
  ADD COLUMN IF NOT EXISTS state_registration varchar(50),
  ADD COLUMN IF NOT EXISTS email varchar(200),
  ADD COLUMN IF NOT EXISTS phone varchar(50),

  ADD COLUMN IF NOT EXISTS address_line varchar(255),
  ADD COLUMN IF NOT EXISTS number varchar(20),
  ADD COLUMN IF NOT EXISTS complement varchar(100),
  ADD COLUMN IF NOT EXISTS district varchar(100),
  ADD COLUMN IF NOT EXISTS city varchar(100),
  ADD COLUMN IF NOT EXISTS state varchar(2),
  ADD COLUMN IF NOT EXISTS zip_code varchar(15),

  ADD COLUMN IF NOT EXISTS updated_at timestamp with time zone,
  ADD COLUMN IF NOT EXISTS is_active boolean;

UPDATE mvp_clean.customer
SET
  type = COALESCE(type, 'PJ'),
  is_active = COALESCE(is_active, true),
  updated_at = COALESCE(updated_at, created_at);

ALTER TABLE mvp_clean.customer
  ALTER COLUMN type SET DEFAULT 'PJ',
  ALTER COLUMN is_active SET DEFAULT true,
  ALTER COLUMN updated_at SET DEFAULT now();
