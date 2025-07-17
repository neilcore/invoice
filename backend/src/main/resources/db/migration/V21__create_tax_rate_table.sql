CREATE TABLE IF NOT EXISTS tax_rate (
	id uuid NOT NULL PRIMARY KEY DEFAULT gen_randon_uuid(),
	organization uuid NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
	name VARCHAR(200) NOT NULL UNIQUE,
	tax_type uuid NOT NULL REFERENCES tax_type(id) ON DELETE SET NULL,
	tax_rate_display NUMERIC(7, 4) NOT NULL,
	effective_rate NUMERIC(7, 4) NOT NULL,
	apply_to_asset_account BOOLEAN,
	apply_to_equity_account BOOLEAN,
	apply_to_expenses_account BOOLEAN,
	apply_to_liabilities_account BOOLEAN,
	apply_to_revenue_account BOOLEAN,
	status VARCHAR(100) NOT NULL,
	created_date DATE NOT NULL DEFAULT CURRENT_DATE
);