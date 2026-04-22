create extension if not exists pgcrypto;

create table users (
                       id uuid primary key default gen_random_uuid(),

                       first_name varchar(255) not null,
                       last_name varchar(255),

                       phone varchar(255) unique,
                       email varchar(255) not null unique,
                       password varchar(255),

                       auth_provider varchar(50),
                       provider_id varchar(255),

                       role varchar(50),

                       profile_image varchar(255),
                       address varchar(255),

                       status varchar(50),

                       is_account_non_expired boolean not null,
                       is_account_non_locked boolean not null,
                       is_credentials_non_expired boolean not null,
                       is_enabled boolean not null,

                       created_at timestamp not null default now(),
                       updated_at timestamp not null default now()
);