BEGIN;
-- Drop organisation_addresses table
DROP TABLE IF EXISTS organisation_addresses;

-- Add address column to organization table
ALTER TABLE organization
ADD COLUMN address JSONB NOT NULL;

COMMIT;