CREATE TABLE IF NOT EXISTS sales_tax_basis (
	id uuid NOT NULL PRIMARY KEY DEFAULT gen_random_uuid(),
	name VARCHAR(100) NOT NULL UNIQUE,
	description TEXT NOT NULL
);

INSERT INTO sales_tax_basis(name, description)
VALUES
(
	'ACCRUAL',
	'''Under the accrual basis, sales tax is recognized (becomes a liability) when the invoice is issued,
	regardless of whether the customer has actually paid it yet.
	Similarly, sales tax on purchases is recognized when the bill is received, even if not yet paid.
	'''
),
(
	'CASH',
	'''sales tax is recognized only when the payment is actually received or made.
	So, sales tax on a sale becomes a liability only when the customer pays the invoice,
	and sales tax on a purchase is only considered paid when the business pays the bill.
	'''
),
(
	'NO_TAX',
	'''This typically means the organization is not registered for sales tax (or VAT/GST)
	and therefore does not have a basis for reporting it. This might apply to very small businesses
	under a certain threshold, or businesses in jurisdictions where sales tax is not applicable to
	their activities.
	'''
);