package ma.enset.exam2test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Version simplifiée de MainApplication sans dépendances externes
 * Pour tester si le problème vient de JavaFX ou des dépendances
 */
public class MainAppSimple extends Application {
    
    @Override
    public void start(Stage stage) {
        System.out.println("🚀 Démarrage de MainAppSimple...");
        
        try {
            // Créer l'interface sans FXML
            BorderPane root = new BorderPane();
            
            // Menu bar
            MenuBar menuBar = new MenuBar();
            Menu fileMenu = new Menu("📁 Fichier");
            MenuItem exitItem = new MenuItem("🚪 Quitter");
            exitItem.setOnAction(e -> stage.close());
            fileMenu.getItems().add(exitItem);
            menuBar.getMenus().add(fileMenu);
            
            // Toolbar
            ToolBar toolBar = new ToolBar();
            Button refreshBtn = new Button("🔄 Actualiser");
            TextField searchField = new TextField();
            searchField.setPromptText("🔍 Rechercher...");
            toolBar.getItems().addAll(refreshBtn, new Separator(), new Label("Recherche:"), searchField);
            
            VBox top = new VBox(menuBar, toolBar);
            root.setTop(top);
            
            // Contenu principal avec onglets
            TabPane tabPane = new TabPane();
            
            // Onglet Employés
            Tab employeTab = new Tab("👥 Employés");
            employeTab.setClosable(false);
            VBox employeContent = new VBox(20);
            employeContent.setPadding(new Insets(20));
            
            Label employeTitle = new Label("Gestion des Employés");
            employeTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            
            // Formulaire simple
            GridPane form = new GridPane();
            form.setHgap(10);
            form.setVgap(10);
            form.add(new Label("Nom:"), 0, 0);
            form.add(new TextField(), 1, 0);
            form.add(new Label("Prénom:"), 0, 1);
            form.add(new TextField(), 1, 1);
            form.add(new Label("Email:"), 0, 2);
            form.add(new TextField(), 1, 2);
            form.add(new Button("✅ Ajouter"), 1, 3);
            
            // Tableau simple
            TableView<String> table = new TableView<>();
            TableColumn<String, String> col1 = new TableColumn<>("Nom");
            TableColumn<String, String> col2 = new TableColumn<>("Email");
            table.getColumns().addAll(col1, col2);
            table.setPrefHeight(200);
            
            employeContent.getChildren().addAll(employeTitle, form, table);
            employeTab.setContent(employeContent);
            
            // Onglet Formations
            Tab formationTab = new Tab("🎓 Formations");
            formationTab.setClosable(false);
            VBox formationContent = new VBox(20);
            formationContent.setPadding(new Insets(20));
            
            Label formationTitle = new Label("Gestion des Formations");
            formationTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
            
            Button testBtn = new Button("🧪 Tester la connexion DB");
            testBtn.setOnAction(e -> testerConnexion());
            
            formationContent.getChildren().addAll(formationTitle, testBtn);
            formationTab.setContent(formationContent);
            
            tabPane.getTabs().addAll(employeTab, formationTab);
            root.setCenter(tabPane);
            
            // Status bar
            HBox statusBar = new HBox();
            statusBar.setPadding(new Insets(5, 10, 5, 10));
            statusBar.setStyle("-fx-background-color: #f0f0f0; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0;");
            Label statusLabel = new Label("✅ Application démarrée avec succès - Version simplifiée");
            statusBar.getChildren().add(statusLabel);
            root.setBottom(statusBar);
            
            // Créer la scène
            Scene scene = new Scene(root, 1000, 700);
            
            stage.setTitle("🏢 Gestion des Formations - Version Simple");
            stage.setScene(scene);
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            stage.show();
            
            System.out.println("✅ Interface affichée avec succès !");
            
        } catch (Exception e) {
            System.err.println("❌ Erreur lors de la création de l'interface: " + e.getMessage());
            e.printStackTrace();
            
            // Afficher une alerte d'erreur
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible de créer l'interface");
            alert.setContentText("Erreur: " + e.getMessage());
            alert.showAndWait();
        }
    }
    
    private void testerConnexion() {
        System.out.println("🔍 Test de connexion...");
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Test de connexion");
        alert.setHeaderText("Test de la base de données");
        
        try {
            // Test simple sans vraie connexion
            alert.setContentText("✅ Test réussi !\n\nPour tester la vraie connexion:\n1. Démarrer XAMPP\n2. Exécuter setup_database.sql\n3. Lancer MainApplication");
        } catch (Exception e) {
            alert.setAlertType(Alert.AlertType.ERROR);
            alert.setContentText("❌ Erreur: " + e.getMessage());
        }
        
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        System.out.println("🎯 Lancement de MainAppSimple...");
        System.out.println("📋 Cette version teste JavaFX sans dépendances externes");
        System.out.println("🔧 Java version: " + System.getProperty("java.version"));
        
        try {
            launch(args);
        } catch (Exception e) {
            System.err.println("❌ ERREUR CRITIQUE: " + e.getMessage());
            e.printStackTrace();
            
            System.err.println("\n🆘 DIAGNOSTIC:");
            System.err.println("1. JavaFX n'est pas installé ou configuré");
            System.err.println("2. Utiliser IntelliJ IDEA avec JavaFX configuré");
            System.err.println("3. Ou installer JavaFX SDK manuellement");
        }
    }
}
