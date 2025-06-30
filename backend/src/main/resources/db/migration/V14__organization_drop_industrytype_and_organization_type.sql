BEGIN;

ALTER TABLE organization
DROP COLUMN industry_type;

ALTER TABLE organization
ADD COLUMN organization_type uuid references organization_type(id) NOT NULL;

COMMIT;