package ma.enset.exam2test.Services;

import ma.enset.exam2test.DAO.DBConnection;
import ma.enset.exam2test.DAO.employeDAOImp;
import ma.enset.exam2test.DAO.formationDAOImp;
import ma.enset.exam2test.entities.employe;
import ma.enset.exam2test.entities.formation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ServiceTest {
    public static void main(String[] args) {
        System.out.println("=== Test de connexion à la base de données ===");

        try {
            Connection connection = DBConnection.getConnection();
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ Connexion à la base de données réussie !");
                System.out.println("URL: " + connection.getMetaData().getURL());
                System.out.println("Utilisateur: " + connection.getMetaData().getUserName());
                System.out.println("Driver: " + connection.getMetaData().getDriverName());

                // Test des DAO
                testEmployeDAO();
                testFormationDAO();
                testAjoutDonnees();

            } else {
                System.out.println("❌ Échec de la connexion à la base de données");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur de connexion à la base de données: " + e.getMessage());
            System.out.println("Vérifiez que:");
            System.out.println("1. XAMPP est démarré");
            System.out.println("2. MySQL est en cours d'exécution");
            System.out.println("3. La base de données DB_ENTREPRISE existe");
            System.out.println("4. Exécutez le script database_schema.sql pour créer les tables");
            e.printStackTrace();
        }
    }

    private static void testEmployeDAO() {
        System.out.println("\n=== Test EmployeDAO ===");
        try {
            // Test de la table employes
            Connection conn = DBConnection.getConnection();
            String checkTableSQL = "SHOW TABLES LIKE 'employes'";
            try (PreparedStatement stmt = conn.prepareStatement(checkTableSQL);
                 ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ La table 'employes' n'existe pas!");
                    System.out.println("Veuillez exécuter le script database_schema.sql");
                    return;
                }
            }

            employeDAOImp employeDAO = new employeDAOImp();
            List<employe> employes = employeDAO.findAll();
            System.out.println("✅ Nombre d'employés trouvés: " + employes.size());

            // Affichage en tableau
            System.out.println("\n📋 LISTE DES EMPLOYÉS:");
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(35) + "+" + "-".repeat(20) + "+");
            System.out.println("| ID  | Nom Complet        | Email                             | Poste              |");
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(35) + "+" + "-".repeat(20) + "+");

            for (employe emp : employes) {
                System.out.printf("| %-3d | %-18s | %-33s | %-18s |\n",
                    emp.getId(),
                    emp.getNomComplet().length() > 18 ? emp.getNomComplet().substring(0, 15) + "..." : emp.getNomComplet(),
                    emp.getEmail().length() > 33 ? emp.getEmail().substring(0, 30) + "..." : emp.getEmail(),
                    emp.getPoste().length() > 18 ? emp.getPoste().substring(0, 15) + "..." : emp.getPoste()
                );
            }
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(20) + "+" + "-".repeat(35) + "+" + "-".repeat(20) + "+");
        } catch (Exception e) {
            System.out.println("❌ Erreur lors du test EmployeDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testFormationDAO() {
        System.out.println("\n=== Test FormationDAO ===");
        try {
            // Test de la table formations
            Connection conn = DBConnection.getConnection();
            String checkTableSQL = "SHOW TABLES LIKE 'formations'";
            try (PreparedStatement stmt = conn.prepareStatement(checkTableSQL);
                 ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("❌ La table 'formations' n'existe pas!");
                    System.out.println("Veuillez exécuter le script database_schema.sql");
                    return;
                }
            }

            formationDAOImp formationDAO = new formationDAOImp();
            List<formation> formations = formationDAO.findAll();
            System.out.println("✅ Nombre de formations trouvées: " + formations.size());

            // Affichage en tableau
            System.out.println("\n📚 LISTE DES FORMATIONS:");
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(25) + "+" + "-".repeat(8) + "+" + "-".repeat(40) + "+");
            System.out.println("| ID  | Nom Formation           | Durée  | Description                            |");
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(25) + "+" + "-".repeat(8) + "+" + "-".repeat(40) + "+");

            for (formation form : formations) {
                String description = form.getDescription() != null ? form.getDescription() : "";
                System.out.printf("| %-3d | %-23s | %-6s | %-38s |\n",
                    form.getId(),
                    form.getNom().length() > 23 ? form.getNom().substring(0, 20) + "..." : form.getNom(),
                    form.getDureeHeures() + "h",
                    description.length() > 38 ? description.substring(0, 35) + "..." : description
                );
            }
            System.out.println("+" + "-".repeat(5) + "+" + "-".repeat(25) + "+" + "-".repeat(8) + "+" + "-".repeat(40) + "+");
        } catch (Exception e) {
            System.out.println("❌ Erreur lors du test FormationDAO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testAjoutDonnees() {
        System.out.println("\n=== Test d'ajout de nouvelles données ===");

        try {
            employeDAOImp employeDAO = new employeDAOImp();
            formationDAOImp formationDAO = new formationDAOImp();

            // Test ajout d'un nouvel employé
            System.out.println("\n➕ Test ajout employé:");
            employe nouvelEmploye = new employe("Testeur", "Test", "test.testeur@entreprise.ma", "Testeur QA");
            employe employeAjoute = employeDAO.save(nouvelEmploye);

            if (employeAjoute != null && employeAjoute.getId() > 0) {
                System.out.println("✅ Employé ajouté avec succès - ID: " + employeAjoute.getId());
                System.out.println("   Nom: " + employeAjoute.getNomComplet());
                System.out.println("   Email: " + employeAjoute.getEmail());
                System.out.println("   Poste: " + employeAjoute.getPoste());
            } else {
                System.out.println("❌ Échec de l'ajout de l'employé");
            }

            // Test ajout d'une nouvelle formation
            System.out.println("\n➕ Test ajout formation:");
            formation nouvelleFormation = new formation("Test Automatisé", "Formation sur les tests automatisés avec Selenium", 28);
            formation formationAjoutee = formationDAO.save(nouvelleFormation);

            if (formationAjoutee != null && formationAjoutee.getId() > 0) {
                System.out.println("✅ Formation ajoutée avec succès - ID: " + formationAjoutee.getId());
                System.out.println("   Nom: " + formationAjoutee.getNom());
                System.out.println("   Durée: " + formationAjoutee.getDureeHeures() + "h");
                System.out.println("   Description: " + formationAjoutee.getDescription());
            } else {
                System.out.println("❌ Échec de l'ajout de la formation");
            }

            // Test inscription employé à formation
            if (employeAjoute != null && formationAjoutee != null) {
                System.out.println("\n🎓 Test inscription employé à formation:");
                try {
                    var inscription = formationDAO.inscrireEmploye(employeAjoute.getId(), formationAjoutee.getId());
                    if (inscription != null) {
                        System.out.println("✅ Inscription réussie - ID: " + inscription.getId());
                        System.out.println("   Employé ID: " + inscription.getEmployeId());
                        System.out.println("   Formation ID: " + inscription.getFormationId());
                        System.out.println("   Statut: " + inscription.getStatut());
                    }
                } catch (Exception e) {
                    System.out.println("❌ Erreur lors de l'inscription: " + e.getMessage());
                }
            }

            // Afficher le nouveau total
            System.out.println("\n📊 NOUVEAUX TOTAUX:");
            List<employe> tousEmployes = employeDAO.findAll();
            List<formation> toutesFormations = formationDAO.findAll();
            System.out.println("   Total employés: " + tousEmployes.size());
            System.out.println("   Total formations: " + toutesFormations.size());

            // Test de recherche
            System.out.println("\n🔍 Test de recherche:");
            employe employeTrouve = employeDAO.findByEmail("test.testeur@entreprise.ma");
            if (employeTrouve != null) {
                System.out.println("✅ Recherche par email réussie: " + employeTrouve.getNomComplet());
            } else {
                System.out.println("❌ Employé non trouvé par email");
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur lors du test d'ajout: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
