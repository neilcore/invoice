BEGIN;
-- Delete existing data from invoice_type table
DELETE FROM invoice_type;
-- Insert new data to invoice_type table
INSERT INTO invoice_type (id, name, description)
VALUES (gen_random_uuid(), 'ACR', 'A bill – commonly known as an Accounts Payable or supplier invoice'),
VALUES (gen_random_uuid(), 'ACP', 'A sales invoice – commonly known as an Accounts Receivable or customer invoice');
COMMIT;