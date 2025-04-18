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



