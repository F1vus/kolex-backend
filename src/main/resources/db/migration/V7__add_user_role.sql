ALTER TABLE backend.users
    ADD COLUMN user_role VARCHAR(50) NOT NULL DEFAULT 'USER';


ALTER TABLE backend.users
    ADD CONSTRAINT chk_user_role CHECK (user_role IN ('USER', 'ADMIN'));

CREATE INDEX idx_users_role ON backend.users(user_role);

-- Updating existing data
UPDATE backend.users
SET user_role = 'USER';