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
    account_source_id INT NOT NULL,
    label VARCHAR(255) NOT NULL,
    state ENUM('completed', 'failed', 'cancelled') NOT NULL DEFAULT 'pending',
    iban_target VARCHAR(34) NOT NULL,
    amount DOUBLE NOT NULL,
    date DATETIME NOT NULL,
);

INSERT INTO beneficiary (account_source_id, iban_target, name)
VALUES
    ('1', 'FR7630006000011234567890123', 'EDF Electricité'),
    ('1', 'FR7630006000019876543210987', 'Agence Immobilière');

INSERT INTO operation (account_source_id, label, state, iban_target, amount, date)
VALUES
    ('1', 'Virement Loyer Janvier', 'completed', 'FR7630006000019876543210987', 850.00, '2026-01-15 14:30:00'),
    ('1', 'Facture Electricité', 'completed', 'FR7630006000011234567890123', 120.50, '2026-01-10 10:00:00'),
    ('1', 'Abonnement Internet', 'cancelled', 'FR7699999999999999999999999', 39.99, '2026-01-05 09:00:00');