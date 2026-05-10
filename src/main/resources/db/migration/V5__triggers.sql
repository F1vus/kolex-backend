CREATE OR REPLACE FUNCTION backend.check_travel_id_consistency()
RETURNS TRIGGER AS $$
DECLARE
v_travel_id_place BIGINT;
    v_travel_id_ticket BIGINT;
BEGIN
SELECT travel_id INTO v_travel_id_place
FROM backend.places
WHERE place_id = NEW.place_id;

IF NEW.ticket_id IS NOT NULL THEN
SELECT travel_id INTO v_travel_id_ticket
FROM backend.tickets
WHERE ticket_id = NEW.ticket_id;

IF v_travel_id_place != v_travel_id_ticket THEN
            RAISE EXCEPTION 'Travel mismatch';
END IF;
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_travel_id_consistency
    BEFORE INSERT OR UPDATE ON backend.place_reservations
                         FOR EACH ROW
                         EXECUTE FUNCTION backend.check_travel_id_consistency();


CREATE OR REPLACE FUNCTION backend.check_place_availability()
RETURNS TRIGGER AS $$
DECLARE
v_overlapping_count INT;
BEGIN
SELECT COUNT(*) INTO v_overlapping_count
FROM backend.place_reservations pr
WHERE pr.place_id = NEW.place_id
  AND pr.status IN ('HELD', 'PURCHASED')
  AND pr.reservation_id != NEW.reservation_id
        AND NOT (
            pr.end_stop_number <= NEW.start_stop_number
            OR pr.start_stop_number >= NEW.end_stop_number
        );

IF v_overlapping_count > 0 THEN
        RAISE EXCEPTION 'Place already reserved';
END IF;

RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_check_place_availability
    BEFORE INSERT OR UPDATE ON backend.place_reservations
                         FOR EACH ROW
                         EXECUTE FUNCTION backend.check_place_availability();