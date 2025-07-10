CREATE TABLE IF NOT EXISTS organization_user_invites (
    organization_id UUID NOT NULL REFERENCES organization(id),
    invitation_for UUID NOT NULL REFERENCES user_account(user_id),
    invitation_by UUID NOT NULL REFERENCES user_account(user_id),
    invitation_role VARCHAR(200) NOT NULL,
    invite_date DATE NOT NULL DEFAULT CURRENT_DATE,
    invitation_status VARCHAR(100) NOT NULL DEFAULT 'PENDING',
    CONSTRAINT chk_no_self_invite CHECK (invitation_for <> invitation_by),
    CONSTRAINT uq_organization_user_invite UNIQUE (organization_id, invitation_for)
);