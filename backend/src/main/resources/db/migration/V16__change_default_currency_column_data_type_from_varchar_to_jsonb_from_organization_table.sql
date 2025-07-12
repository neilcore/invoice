ALTER TABLE organization
ALTER COLUMN default_currency TYPE JSONB USING default_currency::jsonb;