ALTER TABLE organization_tax_details
DROP COLUMN tax_basis,
ADD COLUMN sales_tax_basis uuid REFERENCES sales_tax_basis(id) ON DELETE SET NULL;