package ma.enset.exam2test.Controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ma.enset.exam2test.Services.IemployeService;
import ma.enset.exam2test.Services.employeServiceImp;
import ma.enset.exam2test.entities.employe;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class employeController implements Initializable {

    // Champs du formulaire
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField posteField;

    // Boutons
    @FXML private Button ajouterBtn;
    @FXML private Button modifierBtn;
    @FXML private Button annulerBtn;

    // Recherche
    @FXML private TextField rechercheField;

    // Tableau
    @FXML private TableView<employe> employeTable;
    @FXML private TableColumn<employe, Integer> idColumn;
    @FXML private TableColumn<employe, String> nomColumn;
    @FXML private TableColumn<employe, String> prenomColumn;
    @FXML private TableColumn<employe, String> emailColumn;
    @FXML private TableColumn<employe, String> posteColumn;
    @FXML private TableColumn<employe, LocalDateTime> dateColumn;

    // Status et progress
    @FXML private Label statusLabel;
    @FXML private Label nombreEmployesLabel;
    @FXML private ProgressBar progressBar;
    @FXML private Label progressLabel;

    // Services
    private IemployeService employeService;
    private ObservableList<employe> employesList;
    private employe employeEnModification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeService = new employeServiceImp();
        employesList = FXCollections.observableArrayList();

        // Configuration du tableau
        configurerTableau();

        // Chargement initial des données
        chargerEmployes();

        // Configuration des listeners
        configurerListeners();

        updateStatus("Prêt - " + employesList.size() + " employés chargés");
    }

    private void configurerTableau() {
        // Configuration des colonnes
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        posteColumn.setCellValueFactory(new PropertyValueFactory<>("poste"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        // Formatage de la colonne date
        dateColumn.setCellFactory(column -> new TableCell<employe, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        // Liaison avec la liste observable
        employeTable.setItems(employesList);
    }

    private void configurerListeners() {
        // Sélection dans le tableau
        employeTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                remplirFormulaire(newSelection);
                employeEnModification = newSelection;
                modifierBtn.setVisible(true);
                annulerBtn.setVisible(true);
                ajouterBtn.setText("✅ Ajouter Nouveau");
            }
        });
    }

    private void chargerEmployes() {
        try {
            List<employe> employes = employeService.obtenirTousLesEmployes();
            employesList.clear();
            employesList.addAll(employes);

            if (nombreEmployesLabel != null) {
                nombreEmployesLabel.setText(employes.size() + " employés");
            }

            updateStatus("Données chargées - " + employes.size() + " employés");
        } catch (Exception e) {
            showError("Erreur de chargement", "Impossible de charger les employés: " + e.getMessage());
        }
    }

    @FXML
    private void ajouterEmploye() {
        try {
            if (!validerFormulaire()) {
                return;
            }

            employe nouvelEmploye = new employe(
                nomField.getText().trim(),
                prenomField.getText().trim(),
                emailField.getText().trim(),
                posteField.getText().trim()
            );

            employe employeAjoute = employeService.ajouterEmploye(nouvelEmploye);
            employesList.add(employeAjoute);

            viderFormulaire();
            updateStatus("Employé ajouté: " + employeAjoute.getNomComplet());

            showSuccess("Succès", "Employé ajouté avec succès!");

        } catch (Exception e) {
            showError("Erreur d'ajout", e.getMessage());
        }
    }

    @FXML
    private void modifierEmploye() {
        try {
            if (employeEnModification == null || !validerFormulaire()) {
                return;
            }

            employeEnModification.setNom(nomField.getText().trim());
            employeEnModification.setPrenom(prenomField.getText().trim());
            employeEnModification.setEmail(emailField.getText().trim());
            employeEnModification.setPoste(posteField.getText().trim());

            employe employeModifie = employeService.modifierEmploye(employeEnModification);

            // Actualiser le tableau
            employeTable.refresh();

            annulerModification();
            updateStatus("Employé modifié: " + employeModifie.getNomComplet());

            showSuccess("Succès", "Employé modifié avec succès!");

        } catch (Exception e) {
            showError("Erreur de modification", e.getMessage());
        }
    }

    @FXML
    private void supprimerEmploye() {
        employe employeSelectionne = employeTable.getSelectionModel().getSelectedItem();
        if (employeSelectionne == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner un employé à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer l'employé");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + employeSelectionne.getNomComplet() + " ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean supprime = employeService.supprimerEmploye(employeSelectionne.getId());
                    if (supprime) {
                        employesList.remove(employeSelectionne);
                        annulerModification();
                        updateStatus("Employé supprimé: " + employeSelectionne.getNomComplet());
                        showSuccess("Succès", "Employé supprimé avec succès!");
                    }
                } catch (Exception e) {
                    showError("Erreur de suppression", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void annulerModification() {
        employeEnModification = null;
        viderFormulaire();
        modifierBtn.setVisible(false);
        annulerBtn.setVisible(false);
        ajouterBtn.setText("✅ Ajouter Employé");
        employeTable.getSelectionModel().clearSelection();
        updateStatus("Modification annulée");
    }

    @FXML
    private void rechercher() {
        String terme = rechercheField.getText();
        if (terme == null || terme.trim().isEmpty()) {
            chargerEmployes();
            return;
        }

        try {
            List<employe> resultats = employeService.rechercherParNom(terme.trim());
            employesList.clear();
            employesList.addAll(resultats);
            updateStatus("Recherche: " + resultats.size() + " résultat(s) pour '" + terme + "'");
        } catch (Exception e) {
            showError("Erreur de recherche", e.getMessage());
        }
    }

    @FXML
    private void effacerRecherche() {
        rechercheField.clear();
        chargerEmployes();
    }

    @FXML
    private void actualiserListe() {
        chargerEmployes();
    }

    @FXML
    private void exporterCSV() {
        if (progressBar != null) {
            progressBar.setVisible(true);
            progressLabel.setVisible(true);
            progressLabel.setText("0%");
        }

        updateStatus("Démarrage de l'export CSV...");

        employeService.exporterEmployesCSVAsync(new IemployeService.ExportCallback() {
            @Override
            public void onSuccess(File file) {
                Platform.runLater(() -> {
                    if (progressBar != null) {
                        progressBar.setVisible(false);
                        progressLabel.setVisible(false);
                    }
                    updateStatus("Export terminé: " + file.getName());
                    showSuccess("Export réussi", "Fichier créé: " + file.getAbsolutePath());
                });
            }

            @Override
            public void onError(Exception e) {
                Platform.runLater(() -> {
                    if (progressBar != null) {
                        progressBar.setVisible(false);
                        progressLabel.setVisible(false);
                    }
                    updateStatus("Erreur lors de l'export");
                    showError("Erreur d'export", e.getMessage());
                });
            }

            @Override
            public void onProgress(int progress) {
                Platform.runLater(() -> {
                    if (progressBar != null && progressLabel != null) {
                        progressBar.setProgress(progress / 100.0);
                        progressLabel.setText(progress + "%");
                    }
                });
            }
        });
    }

    @FXML
    private void voirFormations() {
        employe employeSelectionne = employeTable.getSelectionModel().getSelectedItem();
        if (employeSelectionne == null) {
            showWarning("Aucune sélection", "Veuillez sélectionner un employé.");
            return;
        }

        // TODO: Ouvrir une fenêtre pour voir les formations de l'employé
        showInfo("Formations", "Fonctionnalité à implémenter: voir les formations de " + employeSelectionne.getNomComplet());
    }

    // Méthodes utilitaires
    private boolean validerFormulaire() {
        if (nomField.getText() == null || nomField.getText().trim().isEmpty()) {
            showWarning("Validation", "Le nom est obligatoire.");
            nomField.requestFocus();
            return false;
        }

        if (prenomField.getText() == null || prenomField.getText().trim().isEmpty()) {
            showWarning("Validation", "Le prénom est obligatoire.");
            prenomField.requestFocus();
            return false;
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            showWarning("Validation", "L'email est obligatoire.");
            emailField.requestFocus();
            return false;
        }

        if (!employeService.validerEmail(emailField.getText().trim())) {
            showWarning("Validation", "L'email n'est pas valide.");
            emailField.requestFocus();
            return false;
        }

        if (posteField.getText() == null || posteField.getText().trim().isEmpty()) {
            showWarning("Validation", "Le poste est obligatoire.");
            posteField.requestFocus();
            return false;
        }

        return true;
    }

    private void viderFormulaire() {
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        posteField.clear();
    }

    private void remplirFormulaire(employe employe) {
        nomField.setText(employe.getNom());
        prenomField.setText(employe.getPrenom());
        emailField.setText(employe.getEmail());
        posteField.setText(employe.getPoste());
    }

    private void updateStatus(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }

    // Méthodes d'affichage des messages
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
