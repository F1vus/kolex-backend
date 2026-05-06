DROP SCHEMA backend CASCADE;

CREATE SCHEMA backend;
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE SCHEMA IF NOT EXISTS backend;
CREATE EXTENSION IF NOT EXISTS btree_gist;

CREATE TABLE IF NOT EXISTS backend.user(
                                           user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                           user_password VARCHAR(255) NOT NULL,
    user_email VARCHAR(254) UNIQUE NOT NULL,
    user_created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    user_role VARCHAR(50) NOT NULL DEFAULT 'USER'
    );

CREATE TABLE IF NOT EXISTS backend.profile(
                                              profile_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                              profile_first_name VARCHAR(255) NOT NULL,
    profile_last_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    profile_created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_profile_user
    FOREIGN KEY (user_id) REFERENCES backend.user(user_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS backend.station(
                                              station_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                              station_name VARCHAR(150) NOT NULL UNIQUE,
    station_city VARCHAR(150) NOT NULL
    );

CREATE TABLE IF NOT EXISTS backend.travel(
                                             travel_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                             travel_departure TIMESTAMP NOT NULL,
                                             travel_duration INTERVAL NOT NULL,
                                             travel_train VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS backend.travel_route(
                                                   travel_id BIGINT NOT NULL,
                                                   travel_station_stop_id BIGINT NOT NULL,
                                                   travel_stop_number INT NOT NULL CHECK (travel_stop_number > 0),
    travel_distance INT NOT NULL CHECK (travel_distance >= 0),
    travel_price NUMERIC(10,2) NOT NULL CHECK (travel_price >= 0),
    arrival_offset INTERVAL,
    departure_offset INTERVAL,
    CONSTRAINT pk_travel_route
    PRIMARY KEY (travel_id, travel_stop_number),
    CONSTRAINT fk_travel_route_travel
    FOREIGN KEY (travel_id) REFERENCES backend.travel(travel_id),
    CONSTRAINT fk_travel_route_station
    FOREIGN KEY (travel_station_stop_id) REFERENCES backend.station(station_id),
    CONSTRAINT un_travel_route_station
    UNIQUE (travel_id, travel_station_stop_id) -- Stacja w travel_route jest unikatowa
    );

CREATE TABLE IF NOT EXISTS backend.ticket(
                                             ticket_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                             departure_date TIMESTAMP NOT NULL,
                                             ticket_price NUMERIC(10, 2) NOT NULL CHECK (ticket_price >= 0),
    travel_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    start_stop_number INT NOT NULL CHECK (start_stop_number > 0),
    end_stop_number INT NOT NULL CHECK (end_stop_number > start_stop_number),
    ticket_created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    CONSTRAINT fk_ticket_profile
    FOREIGN KEY (profile_id) REFERENCES backend.profile(profile_id),
    CONSTRAINT fk_ticket_travel
    FOREIGN KEY (travel_id) REFERENCES backend.travel(travel_id),
    -- CONSTRAINT fk_ticket_travel_route
    --   FOREIGN KEY (travel_id) REFERENCES backend.travel_route(travel_id), -- Nie mogę dodać, ponieważ travel_route(travel_id) nie jest unikalny i występuje wielokrotnie
    CONSTRAINT fk_ticket_start_stop
    FOREIGN KEY (travel_id, start_stop_number) REFERENCES backend.travel_route(travel_id, travel_stop_number),
    CONSTRAINT fk_ticket_end_stop
    FOREIGN KEY (travel_id, end_stop_number) REFERENCES backend.travel_route(travel_id, travel_stop_number)
    );

CREATE TABLE IF NOT EXISTS backend.seat(
                                           seat_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                           travel_id BIGINT NOT NULL,
                                           seat_number INT NOT NULL,
                                           CONSTRAINT fk_seat_travel
                                           FOREIGN KEY (travel_id) REFERENCES backend.travel(travel_id),
    CONSTRAINT un_seat
    UNIQUE(travel_id, seat_number)
    );




CREATE TABLE IF NOT EXISTS backend.seat_reservation (
                                                        reservation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                                        seat_id BIGINT NOT NULL,
                                                        profile_id BIGINT NOT NULL,
                                                        ticket_id BIGINT,
                                                        start_stop_number INT NOT NULL, -- potrzebne do sprawdzania czy jest overlap(już rozwiązane za pomocą GIST)
                                                        end_stop_number INT NOT NULL, -- potrzebne do sprawdzania czy jest overlap(już rozwiązane za pomocą GIST)
                                                        status VARCHAR(20) NOT NULL CHECK (status IN ('HELD', 'PURCHASED', 'EXPIRED', 'CANCELLED')),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    expires_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_seat_reservation_seat
    FOREIGN KEY (seat_id) REFERENCES backend.seat(seat_id),
    CONSTRAINT fk_seat_reservation_profile
    FOREIGN KEY (profile_id) REFERENCES backend.profile(profile_id),
    CONSTRAINT fk_seat_reservation_ticket
    FOREIGN KEY (ticket_id) REFERENCES backend.ticket(ticket_id),
    CONSTRAINT chk_seat_reservation_segment_order
    CHECK (end_stop_number > start_stop_number),
    EXCLUDE USING gist (
                           seat_id WITH =,
                           int4range(start_stop_number, end_stop_number, '[)') WITH &&
                )
    );
