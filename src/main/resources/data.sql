 CREATE DATABASE IF NOT EXISTS db_medilabo_users;
 USE db_medilabo_users;

CREATE TABLE IF NOT EXISTS role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    role_id BIGINT,
    FOREIGN KEY (role_id) REFERENCES role(id)
) ENGINE=InnoDB;

INSERT INTO role (name)
SELECT * FROM (SELECT 'USER') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'USER');

INSERT INTO role (name)
SELECT * FROM (SELECT 'ADMIN') AS tmp
WHERE NOT EXISTS (SELECT 1 FROM role WHERE name = 'ADMIN');