CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100),
                       phone VARCHAR(50) UNIQUE,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(255),

                       auth_provider VARCHAR(50),
                       provider_id VARCHAR(255),
                       role VARCHAR(50),
                       profile_image VARCHAR(255),
                       address VARCHAR(255),

                       created_at DATE NOT NULL,
                       updated_at DATE NOT NULL,
                       status VARCHAR(50),

                       is_account_non_expired BOOLEAN NOT NULL,
                       is_account_non_locked BOOLEAN NOT NULL,
                       is_credentials_non_expired BOOLEAN NOT NULL,
                       is_enabled BOOLEAN NOT NULL
);