-- Script de création de la base de données pour l'application de gestion des formations

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS DB_ENTREPRISE;
USE DB_ENTREPRISE;

-- Table des employés
CREATE TABLE IF NOT EXISTS employes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    poste VARCHAR(100) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des formations
CREATE TABLE IF NOT EXISTS formations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    description TEXT,
    duree_heures INT NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table de liaison employé-formation (formations suivies)
CREATE TABLE IF NOT EXISTS employe_formations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    employe_id INT NOT NULL,
    formation_id INT NOT NULL,
    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut ENUM('EN_COURS', 'TERMINEE', 'ABANDONNEE') DEFAULT 'EN_COURS',
    FOREIGN KEY (employe_id) REFERENCES employes(id) ON DELETE CASCADE,
    FOREIGN KEY (formation_id) REFERENCES formations(id) ON DELETE CASCADE,
    UNIQUE KEY unique_employe_formation (employe_id, formation_id)
);

-- Insertion de données de test
INSERT INTO employes (nom, prenom, email, poste) VALUES
('Alami', 'Ahmed', 'ahmed.alami@entreprise.ma', 'Développeur'),
('Benali', 'Fatima', 'fatima.benali@entreprise.ma', 'Chef de projet'),
('Chakir', 'Mohamed', 'mohamed.chakir@entreprise.ma', 'Analyste');

INSERT INTO formations (nom, description, duree_heures) VALUES
('Java Avancé', 'Formation approfondie sur Java et ses frameworks', 40),
('Gestion de projet', 'Méthodologies agiles et gestion d\'équipe', 24),
('Base de données', 'Conception et optimisation de bases de données', 32);

INSERT INTO employe_formations (employe_id, formation_id, statut) VALUES
(1, 1, 'EN_COURS'),
(1, 3, 'TERMINEE'),
(2, 2, 'EN_COURS'),
(3, 1, 'EN_COURS');
