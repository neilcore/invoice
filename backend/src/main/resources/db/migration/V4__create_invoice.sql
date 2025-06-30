-- Invoice Types
CREATE TABLE IF NOT EXISTS invoice_type (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);

-- INSERT VALUES TO invoice_type TABLE
INSERT INTO invoice_type (id, name, description) VALUES
    (gen_random_uuid(), 'ACCREC', 'A bill – commonly known as an Accounts Payable or supplier invoice'),
    (gen_random_uuid(), 'ACCREC', '	A sales invoice – commonly known as an Accounts Receivable or customer invoice');
    
-- Create Invoice Table
CREATE TABLE IF NOT EXISTS invoice (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    invoice_type uuid NOT NULL REFERENCES invoice_type(id),
    contact uuid NOT NULL REFERENCES contact(id),
    date DATE NOT NULL,
    due_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    reference VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);