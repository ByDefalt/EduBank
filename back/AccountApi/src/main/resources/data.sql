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
    id VARCHAR(12) PRIMARY KEY,
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