CREATE TABLE IF NOT EXISTS organization_tax_details (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	organization uuid NOT NULL UNIQUE REFERENCES organization(id) ON DELETE CASCADE,
	default_tax_purchases varchar(200),
	pays_tax BOOLEAN,
	tax_number JSONB,
	tax_basis VARCHAR(100),
	tax_period VARCHAR(100),
	sales_tax VARCHAR(100)
);