CREATE TABLE IF NOT EXISTS external_links (
	organization_id uuid NOT NULL references organization(id),
	link_type VARCHAR(100),
	url VARCHAR(200) 
);