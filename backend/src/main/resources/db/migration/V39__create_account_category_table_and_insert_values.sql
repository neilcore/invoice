CREATE TABLE IF NOT EXISTS account_category (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO account_category (name)
VALUES
('ASSET'),
('LIABILITY'),
('EQUITY'),
('EXPENSE'),
('REVENUE');