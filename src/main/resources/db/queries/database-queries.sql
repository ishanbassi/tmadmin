ALTER TABLE tm_agent ADD   company_name varchar(255);
ALTER TABLE tm_agent ALTER COLUMN agent_code TYPE varchar(255);
ALTER TABLE tm_agent ADD deleted boolean;
ALTER TABLE tm_agent ADD COLUMN email varchar(255);

CREATE TABLE user_profile(
 id BIGINT NOT NULL,
    created_date TIMESTAMP NULL,
    modified_date TIMESTAMP NULL,
    deleted BOOLEAN NULL,
    user_id BIGINT NULL,
    PRIMARY KEY (id),
	CONSTRAINT fk_user_profile__user_id FOREIGN KEY (user_id)
        REFERENCES public.jhi_user (id)
);


ALTER TABLE tm_agent ADD COLUMN full_name VARCHAR(255);
ALTER TABLE trademark ALTER COLUMN details  TYPE text;
ALTER TABLE trademark ALTER COLUMN agent_name   TYPE VARCHAR(1000);
ALTER TABLE trademark ALTER COLUMN agent_address   TYPE text;
ALTER TABLE trademark ALTER COLUMN proprietor_address   TYPE text;


ALTER TABLE published_tm ADD COLUMN tm_agent_id BIGINT;
ALTER TABLE published_tm ADD CONSTRAINT fk_tm_agent FOREIGN KEY (tm_agent_id) REFERENCES tm_agent(id);
ALTER TABLE trademark ADD COLUMN tm_agent_id BIGINT;
ALTER TABLE trademark ADD CONSTRAINT fk_user_creation FOREIGN KEY (user_profile_id) REFERENCES user_profile(id);
ALTER TABLE trademark DROP CONSTRAINT fk_trademark__tm_agent_id;
ALTER TABLE published_tm ADD COLUMN renewal_date date ;
ALTER TABLE published_tm ADD COLUMN type VARCHAR(255) ;



CREATE TABLE employee(
id BIGINT PRIMARY KEY NOT NULL,
    full_name VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
	created_date TIMESTAMP,
    modified_date TIMESTAMP,
    deleted BOOLEAN,
    designation VARCHAR(255),
    joining_date date
);

CREATE TABLE lead (
    id BIGINT PRIMARY KEY NOT NULL,
    full_name VARCHAR(255),
    phone_number VARCHAR(255),
    email VARCHAR(255),
    city VARCHAR(255),
    brand_name VARCHAR(255),
    selected_package VARCHAR(255),
    tm_class INTEGER,
    comments VARCHAR(255),
    contact_method VARCHAR(255),
    created_date TIMESTAMP,
    modified_date TIMESTAMP,
    deleted BOOLEAN,
    status VARCHAR(255),
    lead_source VARCHAR(255),
    assigned_to_id BIGINT,
	employee_id BIGINT,
	CONSTRAINT fk_lead__employee_id FOREIGN KEY (employee_id)
	REFERENCES public.employee (id)
);



ALTER table  trademark ALTER COLUMN proprietor_address  TYPE TEXT;
ALTER table  trademark ALTER COLUMN agent_address TYPE TEXT;
ALTER table  trademark ALTER COLUMN details TYPE TEXT;


CREATE INDEX idx_tm_name_len
ON trademark (name);

CREATE INDEX idx_tm_class ON trademark (tm_class);
CREATE INDEX idx_tm_source ON trademark (source);
CREATE INDEX idx_tm_application_no ON trademark (application_no);

CREATE INDEX idx_token_type ON trademark_token (token_type);
CREATE INDEX idx_token_tm ON trademark_token (trademark_id);

CREATE INDEX idx_phonetic_code ON token_phonetic (phonetic_code);
CREATE INDEX idx_tm_journal_no ON trademark (journal_no);

CREATE INDEX idx_normalized_name_prefix
ON trademark (normalized_name text_pattern_ops);

CREATE INDEX idx_tp_phonetic_token ON token_phonetic(phonetic_code, trademark_token_id);
CREATE INDEX idx_tm_journal_source_class  ON trademark(journal_no, source, tm_class)  WHERE source = 'JOURNAL_PUBLICATION';

CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_tm_normalized_name_trgm ON trademark USING GIN(normalized_name gin_trgm_ops);

ALTER TABLE trademark ADD COLUMN normalized_name VARCHAR(255);
ALTER TABLE trademark ADD COLUMN tm_agent_id BIGINT;
ALTER TABLE trademark ADD CONSTRAINT fk_tm_agent FOREIGN KEY (tm_agent_id) REFERENCES tm_agent(id);


-- drop these indexes on trademark_token & token_phonetic because of no usage of them
-- trademark_token: remove 2 dead indexes
DROP INDEX idx_tt_trademark_type;  -- only 8 uses, idx_token_tm covers trademark_id queries
DROP INDEX idx_token_type;         -- 0 uses, completely dead

-- token_phonetic: remove 3 dead indexes  
DROP INDEX idx_phonetic_code;      -- 0 uses
DROP INDEX idx_tp_phonetic_code;   -- 0 uses, exact duplicate
DROP INDEX idx_tp_phonetic_token;  -- 0 uses