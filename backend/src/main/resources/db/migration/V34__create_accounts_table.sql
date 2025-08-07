CREATE TABLE IF NOT EXISTS accounts(
	account_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	organization uuid NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
	code VARCHAR(50) NOT NULL,
	account_name VARCHAR(255) NOT NULL,
	class_type VARCHAR(100) NOT NULL,
	account_type uuid NOT NULL REFERENCES account_types(id) ON DELETE SET NULL,
	status VARCHAR(100),
	description TEXT,
	tax_type VARCHAR(200),
	enable_payments_account boolean,
	updated_date DATE,
	add_to_watch_list BOOLEAN,
	archived BOOLEAN DEFAULT false,
	UNIQUE(organization, code, account_name)
);