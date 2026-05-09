ALTER TABLE backend.user
    ADD COLUMN user_balance NUMERIC(10, 2) DEFAULT 0.00 NOT NULL CHECK (user_balance >= 0);