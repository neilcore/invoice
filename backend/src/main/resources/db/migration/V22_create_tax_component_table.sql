CREATE TABLE IF NOT EXISTS tax_component (
	owner_id uuid NOT NULL REFERENCES tax_rate(id),
	name VARCHAR(100) NOT NULL,
	rate NUMERIC(7, 4) NOT NULL,
	is_compound BOOLEAN,
	non_recoverable BOOLEAN
);