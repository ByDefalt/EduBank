DROP TABLE IF EXISTS account;

CREATE TABLE account (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         nom VARCHAR(100) NOT NULL,
                         prenom VARCHAR(100) NOT NULL,
                         email VARCHAR(150) NOT NULL UNIQUE
);

INSERT INTO account (username, password, nom, prenom, email)
VALUES
    ('alice', 'passwordAlice', 'Dupont', 'Alice', 'alice.dupont@example.com'),
    ('bob', 'passwordBob', 'Martin', 'Bob', 'bob.martin@example.com'),
    ('charlie', 'passwordCharlie', 'Durand', 'Charlie', 'charlie.durand@example.com');
