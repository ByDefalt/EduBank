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
    state VARCHAR(20) NOT NULL DEFAULT 'PENDING'
       CHECK (state IN ('COMPLETED', 'FAILED', 'CANCELLED', 'PENDING')),
    iban_target VARCHAR(34) NOT NULL,
    amount DOUBLE NOT NULL,
    date TIMESTAMP NOT NULL
);

INSERT INTO beneficiary (account_source_id, iban_target, name) VALUES
    ('1', 'FR7611111111111111111111111', 'Alice Martin'),
    ('1', 'FR7611111111111111111111112', 'Bob Dupont'),
    ('1', 'FR7630004000031234567890143', 'Electricité de France'),
    ('1', 'FR7614508059405932176753440', 'Loyer Agence Immobilière');

INSERT INTO beneficiary (account_source_id, iban_target, name) VALUES
    ('2', 'FR7612345678901234567890123', 'Jean Lefèvre'),
    ('2', 'FR7612345678901234567890124', 'Claire Morin'),
    ('2', 'FR7610096000502788345116036', 'Assurance Santé Plus'),
    ('2', 'FR7630003000503878393242590', 'Mutuelle Générale');


INSERT INTO operation (account_source_id, label, state, iban_target, amount, date) VALUES
    ('1', 'Virement loyer janvier',         'COMPLETED', 'FR7614508059405932176753440', 850.00,  '2025-01-05 09:15:00'),
    ('1', 'Paiement EDF',                   'COMPLETED', 'FR7630004000031234567890143', 120.50,  '2025-01-10 11:30:00'),
    ('1', 'Virement Alice Martin',          'COMPLETED', 'FR7611111111111111111111111', 200.00,  '2025-01-18 14:00:00'),
    ('1', 'Virement loyer février',         'COMPLETED', 'FR7614508059405932176753440', 850.00,  '2025-02-05 09:10:00'),
    ('1', 'Paiement EDF',                   'COMPLETED', 'FR7630004000031234567890143', 98.75,   '2025-02-12 10:45:00'),
    ('1', 'Virement Bob Dupont',            'COMPLETED', 'FR7611111111111111111111112', 500.00,  '2025-02-20 16:00:00'),
    ('1', 'Virement loyer mars',            'COMPLETED', 'FR7614508059405932176753440', 850.00,  '2025-03-05 09:05:00'),
    ('1', 'Tentative virement insuffisant', 'FAILED',    'FR7611111111111111111111112', 3000.00, '2025-03-15 08:30:00'),
    ('1', 'Virement Alice Martin annulé',   'CANCELLED', 'FR7611111111111111111111111', 150.00,  '2025-03-22 13:20:00'),
    ('1', 'Paiement EDF mars',              'COMPLETED', 'FR7630004000031234567890143', 110.00,  '2025-03-28 11:00:00'),
    ('1', 'Virement loyer avril',           'COMPLETED', 'FR7614508059405932176753440', 850.00,  '2025-04-05 09:00:00'),
    ('1', 'Virement en attente',            'PENDING',   'FR7611111111111111111111112', 250.00,  '2025-04-25 17:45:00');

INSERT INTO operation (account_source_id, label, state, iban_target, amount, date) VALUES
   ('2', 'Cotisation assurance santé',     'COMPLETED', 'FR7610096000502788345116036', 45.00,   '2025-01-08 10:00:00'),
   ('2', 'Cotisation mutuelle',            'COMPLETED', 'FR7630003000503878393242590', 32.50,   '2025-01-08 10:05:00'),
   ('2', 'Virement Jean Lefèvre',          'COMPLETED', 'FR7612345678901234567890123', 300.00,  '2025-01-20 15:30:00'),
   ('2', 'Cotisation assurance santé',     'COMPLETED', 'FR7610096000502788345116036', 45.00,   '2025-02-08 10:00:00'),
   ('2', 'Cotisation mutuelle',            'COMPLETED', 'FR7630003000503878393242590', 32.50,   '2025-02-08 10:05:00'),
   ('2', 'Virement raté - IBAN invalide',  'FAILED',    'FR7612345678901234567890124', 1200.00, '2025-02-14 09:00:00'),
   ('2', 'Cotisation assurance santé',     'COMPLETED', 'FR7610096000502788345116036', 45.00,   '2025-03-08 10:00:00'),
   ('2', 'Cotisation mutuelle',            'COMPLETED', 'FR7630003000503878393242590', 32.50,   '2025-03-08 10:05:00'),
   ('2', 'Virement Claire Morin',          'CANCELLED', 'FR7612345678901234567890124', 75.00,   '2025-03-19 11:00:00'),
   ('2', 'Cotisation assurance santé',     'COMPLETED', 'FR7610096000502788345116036', 45.00,   '2025-04-08 10:00:00'),
   ('2', 'Virement en cours traitement',   'PENDING',   'FR7612345678901234567890123', 500.00,  '2025-04-26 16:00:00');