BEGIN;
-- Add payment terms for the organization
ALTER TABLE organization
ADD COLUMN payment_terms JSONB;

-- Create payment_terms table
CREATE TABLE IF NOT EXISTS payment_terms (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(100) NOT NULL UNIQUE,
	description TEXT NOT NULL
);

INSERT INTO payment_terms (id, name, description) VALUES
	(gen_random_uuid(), 'BILLDATE-DAYS-AFTER', 'day(s) after bill date'),
	(gen_random_uuid(), 'BILLMONTH-DAYS-AFTER', 'day(s) after bill month'),
	(gen_random_uuid(), 'CURRENT-MONTH', '	of the current month'),
	(gen_random_uuid(), 'FOLLOWING-MONTH', 'of the following month');
	
COMMIT;