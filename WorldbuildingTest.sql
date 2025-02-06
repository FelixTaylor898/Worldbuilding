DROP DATABASE IF EXISTS WorldbuildingTest;
CREATE DATABASE WorldbuildingTest;
USE WorldbuildingTest;

CREATE TABLE user (
    user_id BIGINT PRIMARY KEY auto_increment,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password TEXT NOT NULL,
    role ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL DEFAULT 'ROLE_USER', -- Role defined as ENUM
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

DELIMITER $$

CREATE PROCEDURE reset_database()
BEGIN

    -- Disable foreign key checks to avoid constraint issues
    SET FOREIGN_KEY_CHECKS = 0;

    -- Truncate all tables
DELETE FROM USER;
DELETE FROM world;
DELETE FROM location;
DELETE FROM species;
DELETE FROM species_parent;
DELETE FROM chara;
DELETE FROM character_location;


ALTER TABLE USER AUTO_INCREMENT = 1;
ALTER TABLE world AUTO_INCREMENT = 1;
ALTER TABLE location AUTO_INCREMENT = 1;
ALTER TABLE species AUTO_INCREMENT = 1;
ALTER TABLE chara AUTO_INCREMENT = 1;


    -- Re-enable foreign key checks
    SET FOREIGN_KEY_CHECKS = 1;

    -- 1. Insert sample USERs
    INSERT INTO USER (user_id, username, email, password, role, created_at) VALUES
    (1, 'john_doe', 'john.doe@example.com', 'hashed_password_123', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
    (2, 'jane_smith', 'jane.smith@example.com', 'hashed_password_456', 'ROLE_USER', CURRENT_TIMESTAMP);

    -- 2. Insert sample worlds
    INSERT INTO world (world_id, USER_id, name, description, created_at) VALUES
    (1, 1, 'Earth', 'A blue planet with diverse ecosystems and climates.', CURRENT_TIMESTAMP),
    (2, 2, 'Mars', 'The red planet, home to ancient ruins.', CURRENT_TIMESTAMP);

    -- 3. Insert sample locations
    INSERT INTO location (location_id, world_id, name, type, description, created_at, parent_id, misc) VALUES
    (1, 1, 'New York City', 'Urban', 'A bustling city on Earth.', CURRENT_TIMESTAMP, NULL, 'Famous for skyscrapers'),
    (2, 1, 'Amazon Rainforest', 'Natural', 'A vast tropical rainforest.', CURRENT_TIMESTAMP, NULL, 'Home to diverse wildlife'),
    (3, 2, 'Valles Marineris', 'Canyon', 'A massive canyon on Mars.', CURRENT_TIMESTAMP, NULL, 'One of the largest in the solar system');

    -- 4. Insert sample species
    INSERT INTO species (species_id, world_id, name, description, is_subspecies, created_at, misc) VALUES
    (1, 1, 'Human', 'A species known for intelligence and adaptability.', FALSE, CURRENT_TIMESTAMP, 'Originated on Earth'),
    (2, 1, 'Elf', 'A magical species with pointed ears and longevity.', FALSE, CURRENT_TIMESTAMP, 'Known for their forest connection'),
    (3, 2, 'Martian', 'A species that evolved on Mars.', FALSE, CURRENT_TIMESTAMP, 'Red-skinned and tall');

    -- 5. Insert relationships between species and their parent species
    INSERT INTO species_parent (species_id, parent_species_id) VALUES
    (2, 1);  -- Elves are a subspecies of Humans

    -- 6. Insert sample characters (chara)
    INSERT INTO chara (character_id, world_id, name, description, species_id, gender_identity, orientation, birthdate, deathdate, appearance, personality, aliases, occupation, age, created_at, misc) VALUES
    (1, 1, 'Aragorn', 'A ranger from Middle Earth.', 2, 'Male', 'Heterosexual', '31-12-1000', NULL, 'Tall, rugged, with dark hair', 'Brave, wise, leader', 'Strider', 'Ranger', '35', CURRENT_TIMESTAMP, 'King of Gondor'),
    (2, 1, 'Legolas', 'An elf from Mirkwood.', 2, 'Male', 'Homosexual', '01-01-999', NULL, 'Tall, lean, with blonde hair', 'Agile, quiet, skilled archer', 'Greenleaf', 'Prince', '135', CURRENT_TIMESTAMP, 'Son of Thranduil'),
    (3, 2, 'Zara', 'A Martian explorer.', 3, 'Female', 'Bisexual', '05-03-2250', NULL, 'Red-skinned with white markings', 'Curious, resilient', 'Martian Z', 'Explorer', '30', CURRENT_TIMESTAMP, 'Researcher from Mars');

    -- 7. Insert character location relationships
    INSERT INTO character_location (character_id, location_id) VALUES
    (1, 1),  -- Aragorn is at New York City
    (2, 2),  -- Legolas is at Amazon Rainforest
    (3, 3);  -- Zara is at Valles Marineris

END $$

DELIMITER ;