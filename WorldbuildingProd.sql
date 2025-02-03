DROP DATABASE IF EXISTS WorldbuildingProd;
CREATE DATABASE WorldbuildingProd;
USE WorldbuildingProd;

CREATE TABLE user (
    user_id BIGINT PRIMARY KEY auto_increment,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_USER') DEFAULT 'ROLE_USER', -- Role defined as ENUM
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE world (
    world_id BIGINT PRIMARY KEY auto_increment,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE location (
    location_id BIGINT PRIMARY KEY,
    world_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    description TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    misc TEXT,
    FOREIGN KEY (world_id) REFERENCES world(world_id) ON DELETE CASCADE
);

CREATE TABLE species (
    species_id BIGINT PRIMARY KEY,
    world_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    is_subspecies BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT NOW(),
	misc TEXT,
    FOREIGN KEY (world_id) REFERENCES world(world_id) ON DELETE CASCADE
);

-- Many-to-Many Relationship: Some species can have one or more parent species
CREATE TABLE species_parent (
    species_id BIGINT NOT NULL,
    parent_species_id BIGINT NOT NULL,
    PRIMARY KEY (species_id, parent_species_id),
    FOREIGN KEY (species_id) REFERENCES species(species_id) ON DELETE CASCADE,
    FOREIGN KEY (parent_species_id) REFERENCES species(species_id) ON DELETE CASCADE
);

CREATE TABLE chara (
    character_id BIGINT PRIMARY KEY,
    world_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    species_id BIGINT,
    gender_identity VARCHAR(50),
    orientation VARCHAR(50),
    birthdate VARCHAR(50),  -- Changed to DATE type
    deathdate VARCHAR(50),  -- Changed to DATE type
    appearance TEXT,
    personality TEXT,
    aliases TEXT,
    occupation VARCHAR(255),
    age VARCHAR(50), -- Flexibility in age format (can be used dynamically)
    created_at TIMESTAMP DEFAULT NOW(),
	misc TEXT,
    FOREIGN KEY (world_id) REFERENCES world(world_id) ON DELETE CASCADE,
    FOREIGN KEY (species_id) REFERENCES species(species_id) ON DELETE SET NULL
);

CREATE TABLE character_location (
	character_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    PRIMARY KEY (character_id, location_id),
	FOREIGN KEY (character_id) REFERENCES chara(character_id) ON DELETE CASCADE,
    FOREIGN KEY (location_id) REFERENCES location(location_id) ON DELETE CASCADE
);

select * from user;