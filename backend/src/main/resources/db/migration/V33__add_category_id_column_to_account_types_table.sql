ALTER TABLE account_types
ADD COLUMN category_id uuid NOT NULL REFERENCES account_category(account_category_id) ON DELETE CASCADE;