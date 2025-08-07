-- Create account_types table
CREATE TABLE IF NOT EXISTS account_types (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(100) NOT NULL UNIQUE,
	description TEXT
);

-- Insert into table

INSERT INTO account_types (name, description)
VALUES
('ASSET', ''),
('EQUITY', ''),
('EXPENSE', ''),
('LIABILITY', ''),
('REVENUE', '');