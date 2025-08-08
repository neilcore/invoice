ALTER TABLE accounts
ADD COLUMN category_id uuid NOT NULL REFERENCES account_category(id) ON DELETE SET NULL;