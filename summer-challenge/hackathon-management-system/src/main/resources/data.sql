-- Hackathon Management System - Database Initialization Script
-- Run this manually if you want to set up the database before starting the app.
-- With spring.jpa.hibernate.ddl-auto=update, tables are created automatically.

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS hackathons (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    location VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    organizer_id BIGINT REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS teams (
    id BIGSERIAL PRIMARY KEY,
    team_name VARCHAR(255) NOT NULL,
    leader_id BIGINT NOT NULL REFERENCES users(id),
    hackathon_id BIGINT NOT NULL REFERENCES hackathons(id)
);

CREATE TABLE IF NOT EXISTS team_members (
    team_id BIGINT NOT NULL REFERENCES teams(id),
    user_id BIGINT NOT NULL REFERENCES users(id),
    PRIMARY KEY (team_id, user_id)
);

CREATE TABLE IF NOT EXISTS registrations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id),
    hackathon_id BIGINT NOT NULL REFERENCES hackathons(id),
    registration_date TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS submissions (
    id BIGSERIAL PRIMARY KEY,
    team_id BIGINT NOT NULL REFERENCES teams(id),
    hackathon_id BIGINT NOT NULL REFERENCES hackathons(id),
    github_link VARCHAR(500),
    demo_link VARCHAR(500),
    submitted_at TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS judges (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS scores (
    id BIGSERIAL PRIMARY KEY,
    judge_id BIGINT NOT NULL REFERENCES judges(id),
    team_id BIGINT NOT NULL REFERENCES teams(id),
    innovation_score INTEGER NOT NULL,
    technical_score INTEGER NOT NULL,
    presentation_score INTEGER NOT NULL,
    total_score INTEGER NOT NULL
);
