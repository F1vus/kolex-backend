-- USERS
INSERT INTO backend.users (user_password, user_email)
VALUES
    ('hashed_password_1', 'jan.kowalski@example.com'),
    ('hashed_password_2', 'maria.nowak@example.com'),
    ('hashed_password_3', 'piotr.wisniewski@example.com');
-- PROFILES
INSERT INTO backend.profiles (profile_first_name, profile_last_name, user_id)
VALUES
    ('Jan', 'Kowalski', 1),
    ('Maria', 'Nowak', 2),
    ('Piotr', 'Wiśniewski', 3);
-- STATIONS
INSERT INTO backend.stations (station_name, station_city)
VALUES
    ('Warszawa Centralna', 'Warszawa'),
    ('Łódź Fabryczna', 'Łódź'),
    ('Poznań Główny', 'Poznań'),
    ('Wrocław Główny', 'Wrocław'),
    ('Kraków Główny', 'Kraków'),
    ('Gdańsk Główny', 'Gdańsk');
-- TRAVELS (Różne połączenia)
-- Podróż 1: Warszawa -> Łódź -> Poznań -> Wrocław
INSERT INTO backend.travels (travel_departure, travel_duration, travel_train)
VALUES
    ('2026-05-01 08:00:00', INTERVAL '3 hours 30 minutes', 'IC 123'),
    ('2026-05-02 14:30:00', INTERVAL '4 hours 45 minutes', 'TLK 456'),
    ('2026-05-03 06:15:00', INTERVAL '5 hours 15 minutes', 'EC 789'),
    ('2026-05-05 10:00:00', INTERVAL '6 hours', 'IC 234'),
    ('2026-05-07 16:45:00', INTERVAL '7 hours 30 minutes', 'TLK 567'),
    ('2026-05-10 09:30:00', INTERVAL '4 hours', 'EC 890');
-- TRAVEL ROUTE - Połączenie 1: Warszawa -> Łódź -> Poznań -> Wrocław
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (1, 1, 1, 0,   0.00, NULL,                 INTERVAL '08:00:00'),
    (1, 2, 2, 140, 29.90, INTERVAL '09:00:00', INTERVAL '09:05:00'),
    (1, 3, 3, 310, 59.90, INTERVAL '10:40:00', INTERVAL '10:45:00'),
    (1, 4, 4, 360, 69.90, INTERVAL '11:30:00', NULL);
-- TRAVEL ROUTE - Połączenie 2: Warszawa -> Kraków -> Wrocław
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (2, 1, 1, 0,   0.00, NULL,                 INTERVAL '14:30:00'),
    (2, 5, 2, 290, 45.00, INTERVAL '16:00:00', INTERVAL '16:10:00'),
    (2, 4, 3, 450, 85.00, INTERVAL '19:15:00', NULL);
-- TRAVEL ROUTE - Połączenie 3: Gdańsk -> Poznań -> Warszawa
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (3, 6, 1, 0,   0.00, NULL,                 INTERVAL '06:15:00'),
    (3, 3, 2, 380, 55.00, INTERVAL '09:30:00', INTERVAL '09:40:00'),
    (3, 1, 3, 520, 79.90, INTERVAL '11:30:00', NULL);
-- TRAVEL ROUTE - Połączenie 4: Warszawa -> Łódź -> Kraków
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (4, 1, 1, 0,   0.00, NULL,                 INTERVAL '10:00:00'),
    (4, 2, 2, 140, 25.00, INTERVAL '10:50:00', INTERVAL '10:55:00'),
    (4, 5, 3, 360, 65.00, INTERVAL '13:00:00', NULL);
-- TRAVEL ROUTE - Połączenie 5: Wrocław -> Poznań -> Gdańsk
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (5, 4, 1, 0,   0.00, NULL,                 INTERVAL '16:45:00'),
    (5, 3, 2, 180, 35.00, INTERVAL '18:00:00', INTERVAL '18:10:00'),
    (5, 6, 3, 550, 89.90, INTERVAL '22:30:00', NULL);
-- TRAVEL ROUTE - Połączenie 6: Kraków -> Łódź -> Warszawa -> Gdańsk
INSERT INTO backend.travel_route (
    travel_id, travel_station_stop_id, travel_stop_number,
    travel_distance, travel_price,
    arrival_offset, departure_offset
)
VALUES
    (6, 5, 1, 0,   0.00, NULL,                 INTERVAL '09:30:00'),
    (6, 2, 2, 220, 40.00, INTERVAL '11:15:00', INTERVAL '11:20:00'),
    (6, 1, 3, 360, 59.90, INTERVAL '13:00:00', INTERVAL '13:10:00'),
    (6, 6, 4, 650, 99.90, INTERVAL '17:45:00', NULL);
-- PLACES dla wszystkich podróży (5 miejsc w każdym pociągu)
INSERT INTO backend.places (travel_id, place_number)
VALUES
    (1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
    (2, 1), (2, 2), (2, 3), (2, 4), (2, 5),
    (3, 1), (3, 2), (3, 3), (3, 4), (3, 5),
    (4, 1), (4, 2), (4, 3), (4, 4), (4, 5),
    (5, 1), (5, 2), (5, 3), (5, 4), (5, 5),
    (6, 1), (6, 2), (6, 3), (6, 4), (6, 5);
-- TICKETS
INSERT INTO backend.tickets (
    ticket_date,
    ticket_price,
    travel_id,
    profile_id,
    start_stop_number,
    end_stop_number,
    ticket_created_at
)
VALUES
    ('2026-05-01 08:00:00', 59.90, 1, 1, 2, 4, NOW()),
    ('2026-05-01 08:00:00', 29.90, 1, 1, 1, 3, NOW()),
    ('2026-05-02 14:30:00', 85.00, 2, 2, 1, 3, NOW()),
    ('2026-05-03 06:15:00', 79.90, 3, 3, 1, 3, NOW()),
    ('2026-05-05 10:00:00', 65.00, 4, 1, 1, 3, NOW());
-- PLACE RESERVATIONS
INSERT INTO backend.place_reservations (
    place_id,
    profile_id,
    ticket_id,
    start_stop_number,
    end_stop_number,
    status,
    expires_at
)
VALUES
    (1, 1, 1, 2, 4, 'PURCHASED', NOW() + INTERVAL '30 days'),
    (2, 1, 2, 1, 3, 'PURCHASED', NOW() + INTERVAL '30 days'),
    (6, 2, 3, 1, 3, 'PURCHASED', NOW() + INTERVAL '30 days'),
    (11, 3, 4, 1, 3, 'PURCHASED', NOW() + INTERVAL '30 days'),
    (16, 1, 5, 1, 3, 'HELD', NOW() + INTERVAL '15 minutes');
