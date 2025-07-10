CREATE TABLE IF NOT EXISTS organization_name_update (
	organization_id uuid NOT NULL REFERENCES organization(id),
	is_updatable BOOLEAN NOT NULL DEFAULT true,
	note TEXT,
	updated_date DATE,
	former_name JSONB,
	current_name JSONB
);