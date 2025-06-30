BEGIN;

-- Drop addresses columns in organization table
ALTER TABLE organization
DROP COLUMN physical_address_street,
DROP COLUMN physical_address_city,
DROP COLUMN physical_address_state,
DROP COLUMN physical_address_postal_code,
DROP COLUMN postal_address_street,
DROP COLUMN postal_address_city,
DROP COLUMN postal_address_state,
DROP COLUMN postal_address_postal_code;

-- Create table organisation_addresses
CREATE TABLE IF NOT EXISTS organisation_addresses (
	org_id uuid NOT NULL references organization(id),
	physical_address_street VARCHAR(255) NOT NULL,
	physical_address_city VARCHAR(255) NOT NULL,
	physical_address_state VARCHAR(255) NOT NULL,
	physical_address_postal_code VARCHAR(255) NOT NULL,
	postal_address_street VARCHAR(255),
	postal_address_city VARCHAR(200),
	postal_address_state VARCHAR(200),
	postal_address_postal_code VARCHAR(100)
);
COMMIT;