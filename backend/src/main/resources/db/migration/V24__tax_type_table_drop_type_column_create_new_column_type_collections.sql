ALTER TABLE tax_type
DROP COLUMN type,
ADD COLUMN type_collections JSONB NOT NULL;