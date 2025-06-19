package ma.enset.exam2test.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ma.enset.exam2test.Services.IemployeService;
import ma.enset.exam2test.Services.employeServiceImp;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private TabPane tabPane;
    @FXML private TextField rechercheField;
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;

    private IemployeService employeService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeService = new employeServiceImp();
        updateStatus("Application initialisée");
    }

    @FXML
    private void actualiser() {
        updateStatus("Actualisation en cours...");
        // TODO: Actualiser les données dans les onglets
        updateStatus("Données actualisées");
    }

    @FXML
    private void rechercher() {
        String terme = rechercheField.getText();
        if (terme != null && !terme.trim().isEmpty()) {
            updateStatus("Recherche: " + terme);
            // TODO: Implémenter la recherche dans l'onglet actif
        }
    }

    @FXML
    private void effacerRecherche() {
        rechercheField.clear();
        updateStatus("Recherche effacée");
        // TODO: Réinitialiser l'affichage
    }

    @FXML
    private void exporterEmployes() {
        updateStatus("Démarrage de l'export...");
        
        progressBar.setVisible(true);
        progressLabel.setVisible(true);
        progressLabel.setText("0%");
        
        employeService.exporterEmployesCSVAsync(new IemployeService.ExportCallback() {
            @Override
            public void onSuccess(File file) {
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    progressLabel.setVisible(false);
                    updateStatus("Export terminé: " + file.getName());
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export réussi");
                    alert.setHeaderText("Export CSV terminé");
                    alert.setContentText("Fichier créé: " + file.getAbsolutePath());
                    
                    ButtonType ouvrirButton = new ButtonType("Ouvrir le fichier");
                    ButtonType ouvrirDossierButton = new ButtonType("Ouvrir le dossier");
                    ButtonType fermerButton = new ButtonType("Fermer", ButtonBar.ButtonData.CANCEL_CLOSE);
                    
                    alert.getButtonTypes().setAll(ouvrirButton, ouvrirDossierButton, fermerButton);
                    
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ouvrirButton) {
                            ouvrirFichier(file);
                        } else if (response == ouvrirDossierButton) {
                            ouvrirDossier(file.getParentFile());
                        }
                    });
                });
            }

            @Override
            public void onError(Exception e) {
                Platform.runLater(() -> {
                    progressBar.setVisible(false);
                    progressLabel.setVisible(false);
                    updateStatus("Erreur lors de l'export");
                    
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur d'export");
                    alert.setHeaderText("Impossible d'exporter les données");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                });
            }

            @Override
            public void onProgress(int progress) {
                Platform.runLater(() -> {
                    progressBar.setProgress(progress / 100.0);
                    progressLabel.setText(progress + "%");
                });
            }
        });
    }

    @FXML
    private void quitter() {
        Platform.exit();
    }

    @FXML
    private void aPropos() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("À propos");
        alert.setHeaderText("Gestion des Formations");
        alert.setContentText("Application de gestion des formations en ligne\n" +
                "Version 1.0\n" +
                "Développé avec JavaFX et MySQL");
        alert.showAndWait();
    }

    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    private void ouvrirFichier(File file) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible d'ouvrir le fichier: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void ouvrirDossier(File dossier) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(dossier);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setContentText("Impossible d'ouvrir le dossier: " + e.getMessage());
            alert.showAndWait();
        }
    }

    // Méthodes FXML manquantes pour éviter les erreurs
    @FXML
    private void parametres() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Paramètres");
        alert.setHeaderText("Configuration");
        alert.setContentText("Fonctionnalité à implémenter : Paramètres de l'application");
        alert.showAndWait();
    }

    @FXML
    private void statistiques() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Statistiques");
        alert.setHeaderText("Données statistiques");
        alert.setContentText("Fonctionnalité à implémenter : Statistiques détaillées");
        alert.showAndWait();
    }

    @FXML
    private void documentation() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Documentation");
        alert.setHeaderText("Aide et documentation");
        alert.setContentText("Fonctionnalité à implémenter : Documentation utilisateur");
        alert.showAndWait();
    }
}
