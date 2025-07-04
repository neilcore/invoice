BEGIN;
-- Alter organization table
-- Rename column "name" into display_name
-- Remove trading_name column
-- Add column financial_year JSOB
ALTER TABLE organization
ADD COLUMN financial_year JSONB,
RENAME COLUMN name TO display_name,
REMOVE COLUMN trading_name;

COMMIT;