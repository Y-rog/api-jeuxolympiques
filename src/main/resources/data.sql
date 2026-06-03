-- Insertion des catégories d'offres
INSERT INTO offer_category (title, places_per_offer) VALUES
('Solo', 1),
('Duo', 2),
('Familiale', 4);

-- Insertion des événements
INSERT INTO event (event_title, event_description, event_location, event_places_number, event_date_time) VALUES
('Finale Football', 'Match pour la médaille d''or', 'Stade de France', 4, '2026-07-30 20:00:00'),
('Demi-finale Basketball', 'Les meilleurs s''affrontent', 'Stade de Lille', 15, '2026-07-28 18:00:00'),
('Finale Natation', 'Courses finales', 'Centre Aquatique', 50, '2026-07-25 19:00:00'),
('Match de poule A Escrime par équipe', 'Phase finale', 'Grand Palais', 3000, '2026-07-26 17:00:00'),
('Demi-finale Gymnastique', 'Compétition finale', 'Bercy Arena', 10000, '2026-07-24 16:00:00'),
('Demi-finale Volley Ball', 'Quart de finale', 'Stade Pierre Mauroy', 12000, '2026-07-23 20:00:00'),
('Match de poule A Handball', 'Demi-finale femmes', 'Stade de Lyon', 14000, '2026-07-27 18:30:00'),
('Finale Athlétisme 100m', 'Grande finale du 100m', 'Stade de France', 60000, '2026-07-29 21:00:00'),
('Quart de finale Tennis', 'Finale simple hommes', 'Roland Garros', 15000, '2026-07-31 15:00:00'),
('Finale Boxe', 'Finale poids lourds', 'Zénith de Paris', 7000, '2026-08-01 20:30:00');

INSERT INTO offer (availability, price, event_id, offer_category_id, is_active) VALUES
(true, 20.00, 1, 1, true),
(true, 38.00, 1, 2, true),
(true, 75.00, 1, 3, true),

(true, 22.00, 2, 1, true),
(true, 40.00, 2, 2, true),
(true, 78.00, 2, 3, true),

(true, 18.00, 3, 1, true),
(true, 36.00, 3, 2, true),
(true, 70.00, 3, 3, true),

(true, 16.00, 4, 1, true),
(true, 35.00, 4, 2, true),
(true, 69.00, 4, 3, true),

(true, 25.00, 5, 1, true),
(true, 43.00, 5, 2, true),
(true, 85.00, 5, 3, true),

(true, 20.00, 6, 1, true),
(true, 38.00, 6, 2, true),
(true, 75.00, 6, 3, true),

(true, 21.00, 7, 1, true),
(true, 39.00, 7, 2, true),
(true, 77.00, 7, 3, true),

(true, 24.00, 8, 1, true),
(true, 41.00, 8, 2, true),
(true, 80.00, 8, 3, true),

(true, 19.00, 9, 1, true),
(true, 37.00, 9, 2, true),
(true, 74.00, 9, 3, true),

(true, 23.00, 10, 1, true),
(true, 42.00, 10, 2, true),
(true, 79.00, 10, 3, true);