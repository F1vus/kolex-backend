ALTER TABLE backend.ticket ADD COLUMN seat_id BIGINT;
ALTER TABLE backend.ticket ADD CONSTRAINT fk_ticket_seat FOREIGN KEY (seat_id) REFERENCES backend.seat(seat_id);


-- Stacje
INSERT INTO backend.station (station_name, station_city) VALUES
                                                             ('Warszawa Centralna', 'Warsaw'), ('Poznań Główny', 'Poznan'),
                                                             ('Berlin Hauptbahnhof', 'Berlin'), ('Kraków Główny', 'Krakow'),
                                                             ('Wrocław Główny', 'Wroclaw'), ('Gdańsk Główny', 'Gdansk'),
                                                             ('Szczecin Główny', 'Szczecin'), ('Bochnia', 'Bochnia'),
                                                             ('Brzesko Okocim', 'Brzesko'), ('Tarnów', 'Tarnów');

-- Podróże (Pociągi)
INSERT INTO backend.travel (travel_departure, travel_duration, travel_train) VALUES
                                                                                 ('2026-06-15 08:00:00', '6 hours 30 minutes', 'InterCity Express 45'),
                                                                                 ('2026-06-16 14:15:00', '3 hours 10 minutes', 'Regional Express RE5'),
                                                                                 ('2026-06-17 15:30:00', '0 hours 55 minutes', 'Koleje Małopolskie SKA3');

-- Trasy (Routes)
-- Travel 1: Warszawa -> Poznań -> Berlin
INSERT INTO backend.travel_route VALUES (1, 1, 1, 0, 0.00, '0s', '0s');
INSERT INTO backend.travel_route VALUES (1, 2, 2, 300, 45.50, '2h 30m', '2h 40m');
INSERT INTO backend.travel_route VALUES (1, 3, 3, 570, 89.99, '6h 15m', '6h 30m');

-- Travel 2: Kraków -> Poznań -> Wrocław
INSERT INTO backend.travel_route VALUES (2, 4, 1, 0, 0.00, '0s', '0s');
INSERT INTO backend.travel_route VALUES (2, 2, 2, 260, 35.00, '2h 10m', '2h 20m');
INSERT INTO backend.travel_route VALUES (2, 5, 3, 440, 55.50, '3h 0m', '3h 10m');

-- Travel 3: Kraków -> Bochnia -> Brzesko -> Tarnów
INSERT INTO backend.travel_route VALUES (3, 4, 1, 0, 0.00, '0s', '0s');
INSERT INTO backend.travel_route VALUES (3, 8, 2, 38, 12.00, '25m', '27m');
INSERT INTO backend.travel_route VALUES (3, 9, 3, 50, 15.50, '35m', '37m');
INSERT INTO backend.travel_route VALUES (3, 10, 4, 78, 21.00, '52m', '55m');

-- Seats
INSERT INTO backend.seat (travel_id, seat_number) VALUES
                                                      (1, 10), (1, 11), (1, 12),
                                                      (2, 1), (2, 2),
                                                      (3, 101), (3, 102);