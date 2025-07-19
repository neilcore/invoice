CREATE TABLE IF NOT EXISTS tax_component (
	id uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
	taxt_rate_id uuid NOT NULL REFERENCES tax_rate(id),
	name VARCHAR(100) NOT NULL,
	rate NUMERIC(7, 4) NOT NULL,
	is_compound BOOLEAN,
	non_recoverable BOOLEAN
);