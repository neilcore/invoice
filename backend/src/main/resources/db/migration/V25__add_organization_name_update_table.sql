CREATE TABLE IF NOT EXISTS organization_name_update (
	id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	organization uuid NOT NULL REFERENCES organization(id),
	organization_created_date DATE NOT NULL DEFAULT CURRENT_DATE,
	updated_date DATE,
	is_updatable BOOLEAN NOT NULL DEFAULT true,
	note TEXT
);