CREATE TABLE IF NOT EXISTS user_account_settings (
	user_setting_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	user_account uuid NOT NULL UNIQUE REFERENCES user_account(user_id),
	auto_accept_invitation BOOLEAN NOT NULL DEFAULT true
);