ALTER TABLE organization
ADD COLUMN owner uuid references user(id) NOT NULL;