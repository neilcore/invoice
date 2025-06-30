ALTER TABLE user
ADD COLUMN is_owner BOOLEAN DEFAULT false,
ADD COLUMN part_of_organization BOOLEAN DEFAULT false;