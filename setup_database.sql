-- Script de configuration complète de la base de données
-- À exécuter dans phpMyAdmin ou MySQL Workbench

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS DB_ENTREPRISE;
USE DB_ENTREPRISE;

-- Supprimer les tables existantes si elles existent (pour un reset complet)
DROP TABLE IF EXISTS employe_formations;
DROP TABLE IF EXISTS formations;
DROP TABLE IF EXISTS employes;

-- Table des employés
CREATE TABLE employes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    poste VARCHAR(100) NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des formations
CREATE TABLE formations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    description TEXT,
    duree_heures INT NOT NULL,
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table de liaison employé-formation (formations suivies)
CREATE TABLE employe_formations (
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
('Chakir', 'Mohamed', 'mohamed.chakir@entreprise.ma', 'Analyste'),
('Idrissi', 'Aicha', 'aicha.idrissi@entreprise.ma', 'Designer'),
('Tazi', 'Omar', 'omar.tazi@entreprise.ma', 'Développeur'),
('Bennani', 'Salma', 'salma.bennani@entreprise.ma', 'Testeur'),
('Fassi', 'Youssef', 'youssef.fassi@entreprise.ma', 'Architecte'),
('Alaoui', 'Zineb', 'zineb.alaoui@entreprise.ma', 'Product Owner');

INSERT INTO formations (nom, description, duree_heures) VALUES
('Java Avancé', 'Formation approfondie sur Java et ses frameworks', 40),
('Gestion de projet', 'Méthodologies agiles et gestion d\'équipe', 24),
('Base de données', 'Conception et optimisation de bases de données', 32),
('JavaFX', 'Développement d\'interfaces graphiques avec JavaFX', 20),
('Spring Boot', 'Framework Spring pour applications web', 35),
('Docker', 'Conteneurisation avec Docker', 16),
('Git & GitHub', 'Gestion de versions avec Git', 12),
('Tests unitaires', 'JUnit et Mockito pour les tests', 18);

INSERT INTO employe_formations (employe_id, formation_id, statut) VALUES
(1, 1, 'EN_COURS'),
(1, 3, 'TERMINEE'),
(1, 4, 'EN_COURS'),
(2, 2, 'EN_COURS'),
(2, 7, 'TERMINEE'),
(3, 1, 'EN_COURS'),
(3, 3, 'TERMINEE'),
(4, 4, 'EN_COURS'),
(4, 6, 'EN_COURS'),
(5, 1, 'TERMINEE'),
(5, 5, 'EN_COURS'),
(5, 8, 'EN_COURS'),
(6, 8, 'EN_COURS'),
(7, 5, 'TERMINEE'),
(7, 6, 'EN_COURS'),
(8, 2, 'EN_COURS');

-- Vérification des données insérées
SELECT 'Employés créés:' as Info;
SELECT COUNT(*) as NombreEmployes FROM employes;

SELECT 'Formations créées:' as Info;
SELECT COUNT(*) as NombreFormations FROM formations;

SELECT 'Inscriptions créées:' as Info;
SELECT COUNT(*) as NombreInscriptions FROM employe_formations;

-- Affichage des données pour vérification
SELECT 'Liste des employés:' as Info;
SELECT id, nom, prenom, email, poste FROM employes ORDER BY nom, prenom;

SELECT 'Liste des formations:' as Info;
SELECT id, nom, duree_heures FROM formations ORDER BY nom;

SELECT 'Inscriptions avec détails:' as Info;
SELECT 
    e.nom, 
    e.prenom, 
    f.nom as formation, 
    ef.statut,
    ef.date_inscription
FROM employe_formations ef
JOIN employes e ON ef.employe_id = e.id
JOIN formations f ON ef.formation_id = f.id
ORDER BY e.nom, e.prenom, f.nom;
