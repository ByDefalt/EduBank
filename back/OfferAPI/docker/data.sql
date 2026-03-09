-- Ensure user has all privileges on the database
GRANT ALL PRIVILEGES ON offerapi.* TO 'offeruser'@'%' IDENTIFIED BY 'offerpassword';
FLUSH PRIVILEGES;

DROP TABLE IF EXISTS offer;

CREATE TABLE offer (
    id INT AUTO_INCREMENT PRIMARY KEY,
    picture_path VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    state VARCHAR(10) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

INSERT INTO offer (picture_path, title, description, state, start_date, end_date) VALUES
    ('/images/promo-ete.jpg',      'Soldes ete 2026',              'Profitez de -50% sur toute la collection ete',          'active',   '2026-06-01', '2026-08-31'),
    ('/images/black-friday.jpg',   'Black Friday 2026',            'Reductions exceptionnelles jusqu a -70%',               'inactive', '2026-11-27', '2026-11-30'),
    ('/images/noel.jpg',           'Offre de Noel 2026',           'Idees cadeaux a prix reduits pour les fetes',           'inactive', '2026-12-15', '2026-12-25'),
    ('/images/rentree.jpg',        'Promo rentree scolaire 2025',  'Fournitures et equipements a -30%',                     'expired',  '2025-09-01', '2025-09-15'),
    ('/images/printemps.jpg',      'Collection Printemps 2026',    'Nouvelle collection avec 20% de remise',                'active',   '2026-03-20', '2026-05-31'),
    ('/images/flash-sale.jpg',     'Vente Flash 24h',              'Offre limitee : -40% sur une selection de produits',    'active',   '2026-03-06', '2026-03-07'),
    ('/images/fidelite.jpg',       'Offre Fidelite',               'Recompense exclusive pour nos clients fideles',         'active',   '2026-01-01', '2026-12-31'),
    ('/images/pack-famille.jpg',   'Pack Famille',                 'Offre groupee avec 25% de remise pour 3 achats',        'inactive', '2026-04-01', '2026-04-30'),
    ('/images/liquidation.jpg',    'Liquidation Stock',            'Dernieres pieces disponibles a -60%',                   'expired',  '2025-12-01', '2025-12-31'),
    ('/images/welcome.jpg',        'Offre Bienvenue',              '10% de reduction sur votre premiere commande',          'active',   '2026-01-01', '2026-12-31');