CREATE TABLE backend.places(
                               place_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                               travel_id BIGINT NOT NULL,
                               place_number INT NOT NULL,
                               CONSTRAINT fk_places_travel FOREIGN KEY (travel_id)
                                   REFERENCES backend.travels(travel_id),
                               CONSTRAINT un_place UNIQUE(travel_id, place_number)
);

CREATE TABLE backend.place_reservations (
                                            reservation_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                            place_id BIGINT NOT NULL,
                                            profile_id BIGINT NOT NULL,
                                            ticket_id BIGINT,
                                            start_stop_number INT NOT NULL,
                                            end_stop_number INT NOT NULL,
                                            status VARCHAR(20) NOT NULL CHECK (status IN ('HELD', 'PURCHASED', 'EXPIRED', 'CANCELLED')),
                                            created_at TIMESTAMP NOT NULL DEFAULT NOW(),
                                            expires_at TIMESTAMP NOT NULL,
                                            CONSTRAINT fk_reservation_place FOREIGN KEY (place_id)
                                                REFERENCES backend.places(place_id),
                                            CONSTRAINT fk_reservation_profile FOREIGN KEY (profile_id)
                                                REFERENCES backend.profiles(profile_id),
                                            CONSTRAINT fk_reservation_ticket FOREIGN KEY (ticket_id)
                                                REFERENCES backend.tickets(ticket_id),
                                            CONSTRAINT chk_segment_order CHECK (end_stop_number > start_stop_number)
);