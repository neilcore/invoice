CREATE TABLE IF NOT EXISTS line_items (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	invoice_id uuid NOT NULL REFERENCES organization(id),
	lineitem_description TEXT NOT NULL,
	line_item_id VARCHAR(255) NOT NULL UNIQUE,
	quantity NUMERIC(7, 2),
	unit_amount NUMERIC(7, 2),
	account_code VARCHAR(255) NOT NULL,
	line_amount NUMERIC(7, 2) NOT NULL,
	tax_amount NUMERIC(7, 2) NOT NULL,
	discount_rate INTEGER
);