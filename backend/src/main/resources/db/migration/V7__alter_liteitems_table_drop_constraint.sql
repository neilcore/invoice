-- TRANSACTION FOR CHANGING CONSTRAINST IN line_items TABLE
BEGIN;

-- DROP THE line_items_invoice_id_fkey CONSTRAINT FROM line_items TABLE
ALTER TABLE line_items DROP CONSTRAINT IF EXISTS line_items_invoice_id_fkey;
-- drop the line_items_tax_type_fkey constraint from line_items table
ALTER TABLE line_items DROP CONSTRAINT IF EXISTS line_items_tax_type_fkey;

ALTER TABLE line_items
ADD CONSTRAINT line_items_invoice_id_fkey FOREIGN KEY (invoice_id)
REFERENCES invoice(id)
ON DELETE CASCADE;

ALTER TABLE line_items
ADD CONSTRAINT line_items_tax_type_fkey FOREIGN KEY (tax_type)
REFERENCES tax_type(id)
ON DELETE SET NULL;

COMMIT;