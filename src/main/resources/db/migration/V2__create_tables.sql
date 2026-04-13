CREATE TABLE backend.users(
                              user_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                              user_password VARCHAR(255) NOT NULL,
                              user_email VARCHAR(254) UNIQUE NOT NULL,
                              user_created_at TIMESTAMP DEFAULT NOW() NOT NULL
);

CREATE TABLE backend.profiles(
                                 profile_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 profile_first_name VARCHAR(255) NOT NULL,
                                 profile_last_name VARCHAR(255) NOT NULL,
                                 user_id BIGINT NOT NULL,
                                 profile_created_at TIMESTAMP DEFAULT NOW() NOT NULL,
                                 CONSTRAINT fk_profiles_user FOREIGN KEY (user_id)
                                     REFERENCES backend.users(user_id) ON DELETE CASCADE
);

CREATE TABLE backend.stations(
                                 station_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                 station_name VARCHAR(150) NOT NULL UNIQUE,
                                 station_city VARCHAR(150) NOT NULL
);

CREATE TABLE backend.travels(
                                travel_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                                travel_departure TIMESTAMP NOT NULL,
                                travel_duration INTERVAL NOT NULL,
                                travel_train VARCHAR(50) NOT NULL
);