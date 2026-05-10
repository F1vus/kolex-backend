CREATE TABLE backend.travel_route(
                                     travel_id BIGINT NOT NULL,
                                     travel_station_stop_id BIGINT NOT NULL,
                                     travel_stop_number INT NOT NULL CHECK (travel_stop_number > 0),
                                     travel_distance INT NOT NULL CHECK (travel_distance >= 0),
                                     travel_price NUMERIC(10,2) NOT NULL CHECK (travel_price >= 0),
                                     arrival_offset INTERVAL,
                                     departure_offset INTERVAL,
                                     CONSTRAINT pk_travel_route PRIMARY KEY (travel_id, travel_stop_number),
                                     CONSTRAINT fk_travels_id FOREIGN KEY (travel_id)
                                         REFERENCES backend.travels(travel_id),
                                     CONSTRAINT fk_travels_station_stop_id FOREIGN KEY (travel_station_stop_id)
                                         REFERENCES backend.stations(station_id),
                                     CONSTRAINT un_travel_station UNIQUE (travel_id, travel_station_stop_id)
);

CREATE TABLE backend.tickets(
                                ticket_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                ticket_date TIMESTAMP NOT NULL,
                                ticket_price NUMERIC(10, 2) NOT NULL CHECK (ticket_price >= 0),
                                travel_id BIGINT NOT NULL,
                                profile_id BIGINT NOT NULL,
                                start_stop_number INT NOT NULL CHECK (start_stop_number > 0),
                                end_stop_number INT NOT NULL CHECK (end_stop_number > start_stop_number),
                                ticket_created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                CONSTRAINT fk_tickets_profile FOREIGN KEY (profile_id)
                                    REFERENCES backend.profiles(profile_id),
                                CONSTRAINT fk_tickets_travel FOREIGN KEY (travel_id)
                                    REFERENCES backend.travels(travel_id),
                                CONSTRAINT fk_tickets_start_stop FOREIGN KEY (travel_id, start_stop_number)
                                    REFERENCES backend.travel_route(travel_id, travel_stop_number),
                                CONSTRAINT fk_tickets_end_stop FOREIGN KEY (travel_id, end_stop_number)
                                    REFERENCES backend.travel_route(travel_id, travel_stop_number)
);