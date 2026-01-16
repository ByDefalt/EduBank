DROP TABLE IF EXISTS BankAccountParameter
DROP TABLE IF EXISTS BankAccount
DROP TABLE IF EXISTS BankAccountPivot
DROP TABLE IF EXISTS Types
-- Table BankAccountParameter
CREATE TABLE BankAccountParameter (
                                      id INT AUTO_INCREMENT PRIMARY KEY,
                                      overdraft_limit DOUBLE NOT NULL DEFAULT 0.00,
                                      state ENUM('active', 'inactive','bloqued','closed') NOT NULL DEFAULT 'active'
);

-- Table BankAccount
CREATE TABLE BankAccount (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             parameter_id INT NOT NULL,
                             type_id INT NOT NULL,
                             sold DOUBLE NOT NULL DEFAULT 0.00,
                             iban VARCHAR(34) NOT NULL UNIQUE,
                             FOREIGN KEY (parameter_id) REFERENCES BankAccountParameter(id) ,
                             FOREIGN KEY (type_id) REFERENCES Types(id)
);

-- Table BankAccountPivot (relation many-to-many entre Account et BankAccount)
CREATE TABLE BankAccountPivot (
                                  bank_account_id INT NOT NULL,
                                  account_id INT NOT NULL,
                                  PRIMARY KEY (bank_account_id, account_id),
                                  FOREIGN KEY (bank_account_id) REFERENCES BankAccount(id)

);
-- Table Type (types de comptes bancaires)
CREATE TABLE Types (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL UNIQUE
);