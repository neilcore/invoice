CREATE TABLE IF NOT EXISTS contacts (
	contact_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(255) NOT NULL UNIQUE,
	first_name VARCHAR(100),
	last_name VARCHAR(100),
	email_address varchar(100),
	contact_number VARCHAR(100),
	account_number VARCHAR(100),
	company_number VARCHAR(100),
	default_discount INTEGER,
	tax_number VARCHAR(100),
	contact_status VARCHAR(100) DEFAULT 'ACTIVE',
	is_supplier BOOLEAN NOT NULL DEFAULT false,
	is_customer BOOLEAN NOT NULL DEFAULT false,
	address JSONB,
	phone JSONB,
	payment_terms_id uuid REFERENCES payment_terms(payment_term_id),
	updated_date_utc timestamptz
);