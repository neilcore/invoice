CREATE TABLE IF NOT EXISTS organization_setting (
	setting_id uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
	organization_id uuid NOT NULL UNIQUE REFERENCES organization(id) ON DELETE CASCADE,
	invoice_settings JSONB,
	line_item_settings JSONB
);