ALTER TABLE tm_agent ADD   company_name varchar(255);
ALTER TABLE tm_agent ALTER COLUMN agent_code TYPE varchar(255);
ALTER TABLE tm_agent ADD deleted boolean;