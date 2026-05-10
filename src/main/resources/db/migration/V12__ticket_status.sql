ALTER TABLE backend.ticket
    ADD COLUMN ticket_status VARCHAR(20) DEFAULT 'PAID' NOT NULL CHECK (ticket_status IN ('PAID', 'REFUNDED'));