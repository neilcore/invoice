ALTER TABLE invoice
ADD COLUMN organization uuid NOT NULL REFERENCES organization(id) ON DELETE CASCADE;