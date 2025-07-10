-- Create payment_terms table
CREATE TABLE IF NOT EXISTS payment_terms (
	payment_term_id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
	label VARCHAR(100) NOT NULL UNIQUE,
	description TEXT
);
-- Insert into payment_terms table
INSERT INTO payment_terms (payment_term_id, label, description)
VALUES
(gen_random_uuid(), 'DAYSAFTERBILLDATE', 'day(s) after bill date'),
(gen_random_uuid(), 'DAYSAFTERBILLMONTH', 'day(s) after bill month'),
(gen_random_uuid(), 'OFCURRENTMONTH', 'of the current month'),
(gen_random_uuid(), 'OFFOLLOWINGMONTH', 'of the following month');