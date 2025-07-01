ALTER TABLE organization
ADD COLUMN owner uuid references user_account(id) NOT NULL;