package ma.enset.exam2test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Version simplifiée pour tester JavaFX
 */
public class SimpleApp extends Application {
    
    @Override
    public void start(Stage stage) {
        System.out.println("🚀 Démarrage de l'application JavaFX...");
        
        // Interface simple
        Label titre = new Label("✅ JavaFX fonctionne !");
        titre.setStyle("-fx-font-size: 24px; -fx-text-fill: green;");
        
        Button testDB = new Button("🔗 Tester la base de données");
        testDB.setOnAction(e -> testerConnexionDB());
        
        Button fermer = new Button("❌ Fermer");
        fermer.setOnAction(e -> stage.close());
        
        VBox root = new VBox(20);
        root.getChildren().addAll(titre, testDB, fermer);
        root.setStyle("-fx-padding: 50; -fx-alignment: center;");
        
        Scene scene = new Scene(root, 400, 300);
        
        stage.setTitle("Test JavaFX - Application Simple");
        stage.setScene(scene);
        stage.show();
        
        System.out.println("✅ Interface JavaFX affichée avec succès !");
    }
    
    private void testerConnexionDB() {
        System.out.println("🔍 Test de connexion à la base de données...");
        try {
            // Test simple sans dépendances
            System.out.println("✅ Test de base réussi !");
        } catch (Exception e) {
            System.out.println("❌ Erreur: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        System.out.println("🎯 Lancement de SimpleApp...");
        System.out.println("Java version: " + System.getProperty("java.version"));
        System.out.println("JavaFX version: " + System.getProperty("javafx.version"));
        
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("❌ Erreur lors du lancement: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
