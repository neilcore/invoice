-- Create user_account_settings table
-- This will serve as the settings of every user account
CREATE TABLE IF NOT EXISTS user_account_settings (
	user_setting_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	user_account uuid NOT NULL REFERENCES user_account(id) ON DELETE CASCADE,
	auto_accept_invitation BOOLEAN NOT NULL DEFAULT true
);