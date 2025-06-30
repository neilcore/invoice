-- Organization table: ADD COLUMNS: active_subscription, status
-- Organization table: remove column owner and add name column
-- Related to Organization: create separate table for organization users: organization_users

BEGIN;

ALTER TABLE organization
DROP COLUMN owner,
ADD COLUMN name VARCHAR(255) NOT NULL,
ADD COLUMN active_subscription BOOLEAN NOT NULL DEFAULT false,
ADD COLUMN status VARCHAR(100) NOT NULL DEFAULT "ACTIVE_ACCOUNT";

CREATE TABLE IF NOT EXISTS organization_users (
	organization_id uuid REFERENCES organization(id) NOT NULL ON DELETE CASCADE,
	organization_status VARCHAR(100) REFERENCES organization(status) NOT NULL ON DELETE CASCADE,
	user_id uuid REFERENCES user(id) NOT NULL UNIQUE ON DELETE CASCADE,
	user_role VARCHAR(200) NOT NULL,
	user_joined DATE DEFAULT CURRENT_DATE
);

-- organization_users table: Add a partial unique index to enforce the constraint for user_role
CREATE UNIQUE INDEX idx_one_user_subscriber
ON organization_users (user_role)
WHERE user_role = "SUBSCRIBER";

-- Create organization_user_invites table
CREATE TABLE IF NOT EXISTS organization_user_invites (
	organization_id uuid references organization(id) NOT NULL ON DELETE CASCADE,
	invitation_for uuid references user(id) NOT NULL ON DELETE CASCADE,
	invitation_by uuid references user(id) NOT NULL ON DELETE CASCADE,
	invitation_role VARCHAR(100) NOT NULL,
	invite_date DATE NOT NULL DEFAULT CURRENT_DATE,
	invitation_status VARCHAR(100) NOT NULL DEFAULT "PENDING"
);

ALTER TABLE organization_user_invites
ADD CONSTRAINT chk_invite_not_self CHECK (invitation_for <> invitation_by);

COMMIT;