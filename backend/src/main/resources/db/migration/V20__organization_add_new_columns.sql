-- Organization table: ADD COLUMNS: active_subscription, status
-- Organization table: remove column owner
-- Related to Organization: create separate table for organization users: organization_users

BEGIN;

ALTER TABLE organization
DROP COLUMN owner,
ADD COLUMN active_subscription BOOLEAN NOT NULL DEFAULT 'f',
ADD COLUMN status VARCHAR(100) NOT NULL DEFAULT 'ACTIVE_ACCOUNT',
ADD CONSTRAINT check_all_caps
CHECK (status = UPPER(status) COLLATE "C");

-- Create organization_users table
CREATE TABLE IF NOT EXISTS organization_users (
	organization_id uuid NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
	organization_status VARCHAR(100) NOT NULL,
	user_id uuid NOT NULL UNIQUE REFERENCES user_account(id) ON DELETE CASCADE,
	user_role VARCHAR(200) NOT NULL,
	user_joined DATE DEFAULT CURRENT_DATE
);

-- organization_users table: Add a partial unique index to enforce the constraint for user_role
CREATE UNIQUE INDEX idx_one_user_subscriber
ON organization_users(user_role)
WHERE user_role = 'SUBSCRIBER';

-- Create organization_user_invites table
CREATE TABLE IF NOT EXISTS organization_user_invites (
	organization_id uuid NOT NULL REFERENCES organization(id) ON DELETE CASCADE,
	invitation_for uuid NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
	invitation_by uuid NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
	invitation_role VARCHAR(100) NOT NULL,
	invite_date DATE DEFAULT CURRENT_DATE,
	invitation_status VARCHAR(100) NOT NULL DEFAULT 'PENDING' CONSTRAINT capitalize_status CHECK (invitation_status = UPPER(invitation_status) COLLATE "C")
);

ALTER TABLE organization_user_invites
ADD CONSTRAINT chk_invite_not_self CHECK (invitation_for <> invitation_by);

COMMIT;