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