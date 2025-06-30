-- Payment terms
CREATE TABLE IF NOT EXISTS payment_terms (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT NOT NULL
);

-- Create contact table
CREATE TABLE IF NOT EXISTS contact (
    id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email_address VARCHAR(255) NOT NULL,
    contact_number VARCHAR(50) NOT NULL,
    account_number VARCHAR(50),
    company_number VARCHAR(50),
    tax_number VARCHAR(50),
    contact_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    is_supplier BOOLEAN NOT NULL,
    is_customer BOOLEAN NOT NULL,
    address_type VARCHAR(100) NOT NULL,
    city VARCHAR(50) NOT NULL,
    region VARCHAR(50),
    country VARCHAR(50) NOT NULL,
    postal_code VARCHAR(20),
    attention_to VARCHAR(100),
    payment_terms UUID REFERENCES payment_terms(id),
    updated_date_utc timestamptz
);