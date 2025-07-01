-- "user" is a reserved keyword in postgresql
-- will result to syntax error: ERROR: syntax error at or near "user"
CREATE TABLE IF NOT EXISTS user_account (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	email VARCHAR(255) NOT NULL UNIQUE,
	first_name VARCHAR(255) NOT NULL,
	last_name VARCHAR(255) NOT NULL,
	password VARCHAR(255) NOT NULL,
	phone_number VARCHAR(20),
	roles VARCHAR(50) NOT NULL
);