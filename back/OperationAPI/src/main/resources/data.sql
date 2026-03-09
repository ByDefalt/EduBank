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
   state VARCHAR(20) NOT NULL DEFAULT 'pending'CHECK (state IN ('completed', 'failed', 'cancelled', 'pending')),
   iban_target VARCHAR(34) NOT NULL,
   amount DOUBLE NOT NULL,
   date TIMESTAMP NOT NULL
);

INSERT INTO beneficiary (account_source_id, iban_target, name) VALUES
   ('1', 'FR7611111111111111111111111', 'Compte PEL - Compte 2'),
   ('1', 'FR7699999999999999999999999', 'Marie Dupont'),
   ('1', 'FR7677777777777777777777777', 'Électricité EDF'),
   ('1', 'FR7655555555555555555555555', 'Loyer Agence Immo'),

   ('2', 'FR7612345678901234567890123', 'Compte Courant - Compte 1'),
   ('2', 'FR7698765432109876543210987', 'Livret A - Compte 1'),
   ('2', 'FR7644444444444444444444444', 'Jean Martin'),
   ('2', 'FR7622222222222222222222222', 'Orange Mobile');

INSERT INTO operation (account_source_id, label, state, iban_target, amount, date) VALUES
   ('1', 'Virement loyer mars 2025',       'completed', 'FR7655555555555555555555555',  850.00, '2025-03-01 09:15:00'),
   ('1', 'Facture EDF février 2025',       'completed', 'FR7677777777777777777777777',   73.40, '2025-02-05 10:30:00'),
   ('1', 'Virement Marie Dupont',          'completed', 'FR7699999999999999999999999',  200.00, '2025-02-14 14:00:00'),
   ('1', 'Virement loyer avril 2025',      'completed', 'FR7655555555555555555555555',  850.00, '2025-04-01 09:00:00'),
   ('1', 'Facture EDF mars 2025',          'completed', 'FR7677777777777777777777777',   68.90, '2025-03-06 11:00:00'),
   ('1', 'Virement épargne PEL',           'completed', 'FR7611111111111111111111111',  300.00, '2025-03-15 08:45:00'),
   ('1', 'Tentative virement insuffisant', 'failed',    'FR7699999999999999999999999', 5000.00, '2025-03-20 16:30:00'),
   ('1', 'Virement annulé',                'cancelled', 'FR7677777777777777777777777',  150.00, '2025-04-10 13:00:00'),
   ('1', 'Virement en attente mai 2025',   'pending',   'FR7655555555555555555555555',  850.00, '2025-05-01 09:00:00'),
   ('1', 'Facture EDF avril 2025',         'pending',   'FR7677777777777777777777777',   71.20, '2025-04-06 10:00:00'),

   ('2', 'Remboursement Jean Martin',      'completed', 'FR7644444444444444444444444',   50.00, '2025-02-20 12:00:00'),
   ('2', 'Abonnement Orange Mobile',       'completed', 'FR7622222222222222222222222',   19.99, '2025-03-03 08:00:00'),
   ('2', 'Virement vers Livret A',         'completed', 'FR7698765432109876543210987',  500.00, '2025-03-10 09:30:00'),
   ('2', 'Abonnement Orange Mobile',       'completed', 'FR7622222222222222222222222',   19.99, '2025-04-03 08:00:00'),
   ('2', 'Virement vers compte courant',   'completed', 'FR7612345678901234567890123',  200.00, '2025-04-12 14:15:00'),
   ('2', 'Tentative virement échouée',     'failed',    'FR7644444444444444444444444', 2000.00, '2025-03-25 17:00:00'),
   ('2', 'Abonnement Orange Mobile',       'pending',   'FR7622222222222222222222222',   19.99, '2025-05-03 08:00:00'),
   ('2', 'Virement épargne programmé',     'pending',   'FR7698765432109876543210987',  300.00, '2025-05-15 09:00:00');