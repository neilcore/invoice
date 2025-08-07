CREATE TABLE IF NOT EXISTS account_category (
	account_category_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	category_name VARCHAR(100) NOT NULL UNIQUE
);