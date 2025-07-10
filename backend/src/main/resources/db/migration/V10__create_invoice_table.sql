CREATE TABLE IF NOT EXISTS invoice (
	invoice_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	invoice_type VARCHAR(50) NOT NULL,
	line_amount_type VARCHAR(200),
	invoice_date DATE,
	due_date DATE,
	invoice_status VARCHAR(100) NOT NULL DEFAULT 'DRAFT',
	reference VARCHAR(255),
	sub_total NUMERIC(7,2),
	grand_total numeric(7, 2),
	total_tax NUMERIC(7, 2)
);