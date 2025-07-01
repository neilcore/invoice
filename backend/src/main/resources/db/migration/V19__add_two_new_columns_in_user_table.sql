ALTER TABLE user_account
ADD COLUMN is_owner BOOLEAN DEFAULT false,
ADD COLUMN part_of_organization BOOLEAN DEFAULT false;