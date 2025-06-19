package ma.enset.exam2test.Services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {
    
    public static void main(String[] args) {
        System.out.println("=== Configuration de la base de données ===");
        
        try {
            // Connexion à MySQL sans spécifier de base de données
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "");
            Statement stmt = conn.createStatement();
            
            // Créer la base de données
            System.out.println("Création de la base de données DB_ENTREPRISE...");
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS DB_ENTREPRISE");
            stmt.executeUpdate("USE DB_ENTREPRISE");
            
            // Créer les tables
            System.out.println("Création des tables...");
            
            // Table employes
            String createEmployesTable = """
                CREATE TABLE IF NOT EXISTS employes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(100) NOT NULL,
                    prenom VARCHAR(100) NOT NULL,
                    email VARCHAR(150) UNIQUE NOT NULL,
                    poste VARCHAR(100) NOT NULL,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createEmployesTable);
            System.out.println("✅ Table 'employes' créée");
            
            // Table formations
            String createFormationsTable = """
                CREATE TABLE IF NOT EXISTS formations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nom VARCHAR(150) NOT NULL,
                    description TEXT,
                    duree_heures INT NOT NULL,
                    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.executeUpdate(createFormationsTable);
            System.out.println("✅ Table 'formations' créée");
            
            // Table employe_formations
            String createEmployeFormationsTable = """
                CREATE TABLE IF NOT EXISTS employe_formations (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    employe_id INT NOT NULL,
                    formation_id INT NOT NULL,
                    date_inscription TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    statut ENUM('EN_COURS', 'TERMINEE', 'ABANDONNEE') DEFAULT 'EN_COURS',
                    FOREIGN KEY (employe_id) REFERENCES employes(id) ON DELETE CASCADE,
                    FOREIGN KEY (formation_id) REFERENCES formations(id) ON DELETE CASCADE,
                    UNIQUE KEY unique_employe_formation (employe_id, formation_id)
                )
                """;
            stmt.executeUpdate(createEmployeFormationsTable);
            System.out.println("✅ Table 'employe_formations' créée");
            
            // Insérer des données de test
            System.out.println("Insertion des données de test...");
            
            // Employés de test
            String insertEmployes = """
                INSERT IGNORE INTO employes (nom, prenom, email, poste) VALUES
                ('Alami', 'Ahmed', 'ahmed.alami@entreprise.ma', 'Développeur'),
                ('Benali', 'Fatima', 'fatima.benali@entreprise.ma', 'Chef de projet'),
                ('Chakir', 'Mohamed', 'mohamed.chakir@entreprise.ma', 'Analyste'),
                ('Idrissi', 'Aicha', 'aicha.idrissi@entreprise.ma', 'Designer'),
                ('Tazi', 'Omar', 'omar.tazi@entreprise.ma', 'Développeur')
                """;
            stmt.executeUpdate(insertEmployes);
            System.out.println("✅ Employés de test insérés");
            
            // Formations de test
            String insertFormations = """
                INSERT IGNORE INTO formations (nom, description, duree_heures) VALUES
                ('Java Avancé', 'Formation approfondie sur Java et ses frameworks', 40),
                ('Gestion de projet', 'Méthodologies agiles et gestion d\\'équipe', 24),
                ('Base de données', 'Conception et optimisation de bases de données', 32),
                ('JavaFX', 'Développement d\\'interfaces graphiques avec JavaFX', 20),
                ('Spring Boot', 'Framework Spring pour applications web', 35)
                """;
            stmt.executeUpdate(insertFormations);
            System.out.println("✅ Formations de test insérées");
            
            // Inscriptions de test
            String insertInscriptions = """
                INSERT IGNORE INTO employe_formations (employe_id, formation_id, statut) VALUES
                (1, 1, 'EN_COURS'),
                (1, 3, 'TERMINEE'),
                (2, 2, 'EN_COURS'),
                (3, 1, 'EN_COURS'),
                (4, 4, 'EN_COURS'),
                (5, 1, 'TERMINEE'),
                (5, 5, 'EN_COURS')
                """;
            stmt.executeUpdate(insertInscriptions);
            System.out.println("✅ Inscriptions de test insérées");
            
            stmt.close();
            conn.close();
            
            System.out.println("\n🎉 Base de données configurée avec succès!");
            System.out.println("Vous pouvez maintenant exécuter ServiceTest pour vérifier.");
            
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
