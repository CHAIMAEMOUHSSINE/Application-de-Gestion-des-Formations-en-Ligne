package ma.enset.exam2test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Test simple de connexion à la base de données
 * Peut être exécuté directement depuis l'IDE
 */
public class TestSimple {
    
    public static void main(String[] args) {
        System.out.println("=== Test Simple de Connexion ===");
        
        try {
            // Test de connexion directe
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/DB_ENTREPRISE", 
                "root", 
                ""
            );
            
            System.out.println("✅ Connexion réussie à la base de données!");
            
            // Test des tables
            Statement stmt = conn.createStatement();
            
            // Vérifier la table employes
            try {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employes");
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("✅ Table 'employes' trouvée avec " + count + " enregistrements");
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("❌ Table 'employes' non trouvée: " + e.getMessage());
            }
            
            // Vérifier la table formations
            try {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM formations");
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("✅ Table 'formations' trouvée avec " + count + " enregistrements");
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("❌ Table 'formations' non trouvée: " + e.getMessage());
            }
            
            // Vérifier la table employe_formations
            try {
                ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM employe_formations");
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("✅ Table 'employe_formations' trouvée avec " + count + " enregistrements");
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("❌ Table 'employe_formations' non trouvée: " + e.getMessage());
            }
            
            // Afficher quelques employés
            try {
                System.out.println("\n--- Employés dans la base ---");
                ResultSet rs = stmt.executeQuery("SELECT nom, prenom, email, poste FROM employes LIMIT 5");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("prenom") + " " + rs.getString("nom") + 
                                     " (" + rs.getString("email") + ") - " + rs.getString("poste"));
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("❌ Erreur lors de la lecture des employés: " + e.getMessage());
            }
            
            // Afficher quelques formations
            try {
                System.out.println("\n--- Formations dans la base ---");
                ResultSet rs = stmt.executeQuery("SELECT nom, duree_heures FROM formations LIMIT 5");
                while (rs.next()) {
                    System.out.println("- " + rs.getString("nom") + " (" + rs.getInt("duree_heures") + "h)");
                }
                rs.close();
            } catch (Exception e) {
                System.out.println("❌ Erreur lors de la lecture des formations: " + e.getMessage());
            }
            
            stmt.close();
            conn.close();
            
            System.out.println("\n🎉 Test terminé avec succès!");
            System.out.println("\nPour continuer:");
            System.out.println("1. Si les tables n'existent pas, exécutez setup_database.sql dans phpMyAdmin");
            System.out.println("2. Ensuite, vous pouvez exécuter ServiceTest.java pour tester les DAO");
            
        } catch (Exception e) {
            System.out.println("❌ Erreur de connexion: " + e.getMessage());
            System.out.println("\nVérifiez que:");
            System.out.println("1. XAMPP est démarré");
            System.out.println("2. MySQL fonctionne sur le port 3306");
            System.out.println("3. La base de données DB_ENTREPRISE existe");
            System.out.println("4. Exécutez setup_database.sql dans phpMyAdmin");
            
            e.printStackTrace();
        }
    }
}
