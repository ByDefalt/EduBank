DROP TABLE IF EXISTS operation;
DROP TABLE IF EXISTS beneficiary;

CREATE TABLE beneficiary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_source_id VARCHAR(12) NOT NULL,
    iban_target VARCHAR(34) NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE operation (
    id INT AUTO_INCREMENT PRIMARY KEY,
    account_source_id VARCHAR(12) NOT NULL,
    label VARCHAR(255) NOT NULL,
    state VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    iban_target VARCHAR(34) NOT NULL,
    amount DOUBLE NOT NULL,
    date TIMESTAMP NOT NULL
);

