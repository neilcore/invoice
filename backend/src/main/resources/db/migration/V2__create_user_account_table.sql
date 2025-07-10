-- "user" is a reserved keyword in postgresql
-- will result to syntax error: ERROR: syntax error at or near "user"
CREATE TABLE IF NOT EXISTS user_account (
	user_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	email VARCHAR(255) NOT NULL UNIQUE,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	account_password VARCHAR(255) NOT NULL,
	contact_number JSONB,
	roles VARCHAR(50) NOT NULL DEFAULT 'NONE',
	part_of_organization BOOLEAN NOT NULL DEFAULT false,
	is_owner BOOLEAN NOT NULL DEFAULT false
);