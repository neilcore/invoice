BEGIN;
-- Alter organization table
-- Rename column "name" into display_name
-- Remove trading_name column
-- Add column financial_year JSOB
ALTER TABLE organization
ADD COLUMN financial_year JSONB;

ALTER TABLE organization
RENAME COLUMN name TO display_name;

ALTER TABLE organization
DROP COLUMN trading_name;

COMMIT;