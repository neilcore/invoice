CREATE TABLE IF NOT EXISTS organization_user_invites (
	organization_id uuid NOT NULL REFERENCES organization(id),
	invitation_for uuid NOT NULL REFERENCES user(id),
	invitation_by uuid NOT NULL REFERENCES user(id),
	invitation_role VARCHAR(200) NOT NULL,
	invite_date date NOT NULL DEFAULT CURRENT_DATE,
	invitation_status VARCHAR(100) NOT NULL DEFAULT 'PENDING',
	CONSTRAINT chk_distinct_inviters CHECK (invitation_for <> invitation_by)
);