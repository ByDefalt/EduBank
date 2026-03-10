CREATE TABLE Role (
    id INT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE PersonalInformation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    firstname VARCHAR(255) NOT NULL,
    lastname VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    phone_number VARCHAR(50)
);

CREATE TABLE Account (
    id VARCHAR(13) PRIMARY KEY UNIQUE,
    personal_info_id INT NOT NULL,
    role_id INT NOT NULL,
    password VARCHAR(255) NOT NULL,
    state VARCHAR(50),

    CONSTRAINT fk_account_role FOREIGN KEY (role_id) REFERENCES Role(id),
    CONSTRAINT fk_account_personal_info FOREIGN KEY (personal_info_id) REFERENCES PersonalInformation(id),
    CONSTRAINT uq_account_personal_info UNIQUE (personal_info_id)
);

INSERT INTO Role (id, name) VALUES
    (1, 'ADMIN'),
    (2, 'CUSTOMER');

INSERT INTO PersonalInformation (firstname, lastname, email, address, phone_number) VALUES
    ('John', 'Doe', 'john.doe@localhost.com', '123 Main St', '0644054058'),
    ('Jane', 'Smith', 'jane.smith@localhost.com', '456 Elm St', '0634054059'),
    ('Alice', 'Johnson', 'alice.johnson@localhost.com', '789 Oak St', '0624054060'),
    ('Bob', 'Brown', 'bob.brown@localhost.com', '321 Pine St', '0614054061');

INSERT INTO Account (id, personal_info_id, role_id, password, state) VALUES
    ('admin', 1, 1, 'admin', 'ACTIVE'),
    ('1', 2, 2, '1', 'ACTIVE'),
    ('2', 3, 2, '2', 'ACTIVE'),
    ('3', 4, 2, '3', 'ACTIVE');