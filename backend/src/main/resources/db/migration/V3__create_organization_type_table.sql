CREATE TABLE IF NOT EXISTS organization_type (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(200) NOT NULL UNIQUE
);