CREATE extension if NOT EXISTS "uuid-ossp";

CREATE TABLE "user" (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    unique (username, email)
);

CREATE TABLE game (
    id SERIAL PRIMARY KEY,
    reference uuid NOT NULL DEFAULT uuid_generate_v4(),
    snapshot jsonb NOT NULL,
    created_at timestamp WITH time zone DEFAULT now(),
    unique (reference)
);
