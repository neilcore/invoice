-- Create tax_type table
CREATE TABLE IF NOT EXISTS tax_type (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    type VARCHAR(255) NOT NULL UNIQUE,
    rate DECIMAL(5, 2) NOT NULL DEFAULT 0.00,
    name VARCHAR(255) NOT NULL,
    system_defined VARCHAR(100)
);
-- Create Line Items Table
CREATE TABLE IF NOT EXISTS line_items (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    invoice_id uuid NOT NULL REFERENCES invoice(id),
    description VARCHAR(255) NOT NULL,
    line_item_id VARCHAR(100) NOT NULL UNIQUE,
    quantity DECIMAL(10, 2) NOT NULL,
    unit_amount DECIMAL(10, 2) NOT NULL,
    account_code VARCHAR(100) NOT NULL,
    line_amount_type VARCHAR(100) NOT NULL,
    line_amount DECIMAL(10, 2) NOT NULL,
    tax_type uuid NOT NULL REFERENCES tax_type(id),
    tax_amount DECIMAL(10, 2) NOT NULL
);