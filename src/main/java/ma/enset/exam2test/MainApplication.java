package ma.enset.exam2test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import ma.enset.exam2test.DAO.DBConnection;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class MainApplication extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        // Vérifier la connexion à la base de données au démarrage
        if (!verifierConnexionDB()) {
            afficherErreurConnexion();
            return;
        }
        
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/ma/enset/exam2test/main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);

        // Appliquer le thème BootstrapFX
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

        // Ajouter nos styles personnalisés
        scene.getStylesheets().add(MainApplication.class.getResource("/ma/enset/exam2test/styles.css").toExternalForm());

        stage.setTitle("Gestion des Formations - Entreprise Digitale");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }
    
    private boolean verifierConnexionDB() {
        try {
            Connection connection = DBConnection.getConnection();
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
            return false;
        }
    }
    
    private void afficherErreurConnexion() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur de connexion");
        alert.setHeaderText("Impossible de se connecter à la base de données");
        alert.setContentText("Vérifiez que:\n" +
                "1. XAMPP est démarré\n" +
                "2. MySQL est en cours d'exécution\n" +
                "3. La base de données DB_ENTREPRISE existe\n" +
                "4. Le script database_schema.sql a été exécuté");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch();
    }
}
