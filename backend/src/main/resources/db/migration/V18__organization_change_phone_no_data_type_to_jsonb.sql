-- Typecasting: phone_no::jsonb
ALTER TABLE organization
ALTER COLUMN phone_no TYPE JSONB USING phone_no::jsonb;