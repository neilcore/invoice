CREATE TABLE IF NOT EXISTS organization_users (
	organization_id uuid NOT NULL REFERENCES organization(id),
	user_id uuid NOT NULL REFERENCES user_account(user_id),
	user_role VARCHAR(255) NOT NULL,
	user_joined DATE NOT NULL DEFAULT CURRENT_DATE,
	organization_status VARCHAR(200) NOT NULL,
	CONSTRAINT uq_organization_user UNIQUE (organization_id, user_id)
);