CREATE SEQUENCE passport_id_seq;
CREATE TABLE IF NOT EXISTS passport (
                          passport_id int8 NOT NULL DEFAULT nextval('passport_id_seq') PRIMARY KEY,
                          series VARCHAR (4) ,
                          number VARCHAR (6) ,
                          issue_branch VARCHAR (50) ,
                          issue_date DATE
);
ALTER SEQUENCE passport_id_seq
    OWNED BY passport.passport_id;


CREATE SEQUENCE employment_id_seq;
CREATE TABLE IF NOT EXISTS employment (
                            employment_id int8 NOT NULL DEFAULT nextval('employment_id_seq') PRIMARY KEY,
                            status VARCHAR (10) ,
                            employer_inn VARCHAR (20) ,
                            salary DECIMAL,
                            position VARCHAR (20),
                            work_experience_total INTEGER,
                            work_experience_current INTEGER
);
ALTER SEQUENCE employment_id_seq
    OWNED BY employment.employment_id;


CREATE SEQUENCE client_id_seq;
CREATE TABLE IF NOT EXISTS client (
                        client_id int8 NOT NULL DEFAULT nextval('client_id_seq') PRIMARY KEY,
                        last_name VARCHAR (30) ,
                        first_name VARCHAR (30) ,
                        middle_name VARCHAR (30) ,
                        birth_date DATE ,
                        email VARCHAR (100),
                        gender VARCHAR (50),
                        marital_status VARCHAR (50),
                        dependent_amount INTEGER,
                        passport_id int8,
                        employment_id int8,
                        account VARCHAR (50),

                        FOREIGN KEY (passport_id) REFERENCES passport (passport_id) ON UPDATE CASCADE ON DELETE CASCADE,
                        FOREIGN KEY (employment_id) REFERENCES employment (employment_id) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER SEQUENCE client_id_seq
    OWNED BY client.client_id;


CREATE SEQUENCE credit_id_seq;
CREATE TABLE IF NOT EXISTS credit (
                        credit_id int8 NOT NULL DEFAULT nextval('credit_id_seq') PRIMARY KEY,
                        amount DECIMAL,
                        term INTEGER,
                        monthly_payment DECIMAL,
                        rate DECIMAL,
                        psk DECIMAL,
                        payment_schedule JSONB,
                        insurance_enable boolean,
                        salary_client boolean,
                        credit_status VARCHAR(15)
);
ALTER SEQUENCE credit_id_seq
    OWNED BY credit.credit_id;



CREATE SEQUENCE application_id_seq;
CREATE TABLE IF NOT EXISTS application (
                             application_id int8 NOT NULL DEFAULT nextval('application_id_seq') PRIMARY KEY,
                             client_id int8,
                             credit_id int8,
                             status VARCHAR (50),
                             creation_date TIMESTAMP,
                             applied_offer JSONB,
                             sign_date TIMESTAMP,
                             ses_code BIGINT,
                             status_history JSONB,

                             FOREIGN KEY (client_id) REFERENCES client (client_id) ON UPDATE CASCADE ON DELETE CASCADE,
                             FOREIGN KEY (credit_id) REFERENCES credit (credit_id) ON UPDATE CASCADE ON DELETE CASCADE
);
ALTER SEQUENCE application_id_seq
    OWNED BY application.application_id;