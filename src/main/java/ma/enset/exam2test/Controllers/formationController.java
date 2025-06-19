package ma.enset.exam2test.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ma.enset.exam2test.Services.IformationService;
import ma.enset.exam2test.Services.formationServiceImp;
import ma.enset.exam2test.entities.formation;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class formationController implements Initializable {

    // Champs du formulaire
    @FXML private TextField nomFormationField;
    @FXML private Spinner<Integer> dureeSpinner;
    @FXML private TextArea descriptionArea;

    // Boutons
    @FXML private Button ajouterFormationBtn;
    @FXML private Button modifierFormationBtn;
    @FXML private Button annulerFormationBtn;

    // Recherche
    @FXML private TextField rechercheFormationField;
    @FXML private Spinner<Integer> dureeMinSpinner;
    @FXML private Spinner<Integer> dureeMaxSpinner;

    // Tableau des formations
    @FXML private TableView<formation> formationTable;
    @FXML private TableColumn<formation, Integer> idFormationColumn;
    @FXML private TableColumn<formation, String> nomFormationColumn;
    @FXML private TableColumn<formation, Integer> dureeColumn;
    @FXML private TableColumn<formation, String> descriptionColumn;
    @FXML private TableColumn<formation, LocalDateTime> dateCreationColumn;

    // Status
    @FXML private Label statusFormationLabel;

    // Services
    private IformationService formationService;
    private ObservableList<formation> formationsList;
    private formation formationEnModification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        formationService = new formationServiceImp();
        formationsList = FXCollections.observableArrayList();

        // Configuration des spinners
        configurerSpinners();

        // Configuration du tableau
        configurerTableauFormations();

        // Chargement initial des données
        chargerFormations();

        // Configuration des listeners
        configurerListenersFormations();

        updateStatusFormation("Prêt - " + formationsList.size() + " formations chargées");
    }

    private void configurerSpinners() {
        // Spinner durée principale
        if (dureeSpinner != null) {
            dureeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 200, 20));
        }

        // Spinners de filtrage
        if (dureeMinSpinner != null) {
            dureeMinSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 0));
        }

        if (dureeMaxSpinner != null) {
            dureeMaxSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 200, 200));
        }
    }

    private void configurerTableauFormations() {
        // Configuration des colonnes
        if (idFormationColumn != null) idFormationColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (nomFormationColumn != null) nomFormationColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        if (dureeColumn != null) dureeColumn.setCellValueFactory(new PropertyValueFactory<>("dureeHeures"));
        if (descriptionColumn != null) descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        if (dateCreationColumn != null) dateCreationColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));

        // Formatage de la colonne date
        if (dateCreationColumn != null) {
            dateCreationColumn.setCellFactory(column -> new TableCell<formation, LocalDateTime>() {
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
        }

        // Liaison avec la liste observable
        if (formationTable != null) {
            formationTable.setItems(formationsList);
        }
    }

    private void configurerListenersFormations() {
        if (formationTable != null) {
            formationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    remplirFormulaireFormation(newSelection);
                    formationEnModification = newSelection;
                    if (modifierFormationBtn != null) modifierFormationBtn.setVisible(true);
                    if (annulerFormationBtn != null) annulerFormationBtn.setVisible(true);
                    if (ajouterFormationBtn != null) ajouterFormationBtn.setText("✅ Ajouter Nouvelle");
                }
            });
        }
    }

    private void chargerFormations() {
        try {
            List<formation> formations = formationService.obtenirToutesLesFormations();
            formationsList.clear();
            formationsList.addAll(formations);
            updateStatusFormation("Données chargées - " + formations.size() + " formations");
        } catch (Exception e) {
            showErrorFormation("Erreur de chargement", "Impossible de charger les formations: " + e.getMessage());
        }
    }

    @FXML
    private void ajouterFormation() {
        try {
            if (!validerFormulaireFormation()) {
                return;
            }

            formation nouvelleFormation = new formation(
                nomFormationField.getText().trim(),
                descriptionArea.getText().trim(),
                dureeSpinner.getValue()
            );

            formation formationAjoutee = formationService.ajouterFormation(nouvelleFormation);
            formationsList.add(formationAjoutee);

            viderFormulaireFormation();
            updateStatusFormation("Formation ajoutée: " + formationAjoutee.getNom());

            showSuccessFormation("Succès", "Formation ajoutée avec succès!");

        } catch (Exception e) {
            showErrorFormation("Erreur d'ajout", e.getMessage());
        }
    }

    @FXML
    private void modifierFormation() {
        try {
            if (formationEnModification == null || !validerFormulaireFormation()) {
                return;
            }

            formationEnModification.setNom(nomFormationField.getText().trim());
            formationEnModification.setDescription(descriptionArea.getText().trim());
            formationEnModification.setDureeHeures(dureeSpinner.getValue());

            formation formationModifiee = formationService.modifierFormation(formationEnModification);

            // Actualiser le tableau
            if (formationTable != null) {
                formationTable.refresh();
            }

            annulerModificationFormation();
            updateStatusFormation("Formation modifiée: " + formationModifiee.getNom());

            showSuccessFormation("Succès", "Formation modifiée avec succès!");

        } catch (Exception e) {
            showErrorFormation("Erreur de modification", e.getMessage());
        }
    }

    @FXML
    private void supprimerFormation() {
        formation formationSelectionnee = formationTable != null ? formationTable.getSelectionModel().getSelectedItem() : null;
        if (formationSelectionnee == null) {
            showWarningFormation("Aucune sélection", "Veuillez sélectionner une formation à supprimer.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer la formation");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer '" + formationSelectionnee.getNom() + "' ?");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    boolean supprimee = formationService.supprimerFormation(formationSelectionnee.getId());
                    if (supprimee) {
                        formationsList.remove(formationSelectionnee);
                        annulerModificationFormation();
                        updateStatusFormation("Formation supprimée: " + formationSelectionnee.getNom());
                        showSuccessFormation("Succès", "Formation supprimée avec succès!");
                    }
                } catch (Exception e) {
                    showErrorFormation("Erreur de suppression", e.getMessage());
                }
            }
        });
    }

    @FXML
    private void annulerModificationFormation() {
        formationEnModification = null;
        viderFormulaireFormation();
        if (modifierFormationBtn != null) modifierFormationBtn.setVisible(false);
        if (annulerFormationBtn != null) annulerFormationBtn.setVisible(false);
        if (ajouterFormationBtn != null) ajouterFormationBtn.setText("✅ Ajouter Formation");
        if (formationTable != null) formationTable.getSelectionModel().clearSelection();
        updateStatusFormation("Modification annulée");
    }

    // Méthodes utilitaires
    private boolean validerFormulaireFormation() {
        if (nomFormationField == null || nomFormationField.getText() == null || nomFormationField.getText().trim().isEmpty()) {
            showWarningFormation("Validation", "Le nom de la formation est obligatoire.");
            if (nomFormationField != null) nomFormationField.requestFocus();
            return false;
        }

        if (dureeSpinner == null || dureeSpinner.getValue() == null || dureeSpinner.getValue() <= 0) {
            showWarningFormation("Validation", "La durée doit être positive.");
            if (dureeSpinner != null) dureeSpinner.requestFocus();
            return false;
        }

        return true;
    }

    private void viderFormulaireFormation() {
        if (nomFormationField != null) nomFormationField.clear();
        if (descriptionArea != null) descriptionArea.clear();
        if (dureeSpinner != null) dureeSpinner.getValueFactory().setValue(20);
    }

    private void remplirFormulaireFormation(formation formation) {
        if (nomFormationField != null) nomFormationField.setText(formation.getNom());
        if (descriptionArea != null) descriptionArea.setText(formation.getDescription());
        if (dureeSpinner != null) dureeSpinner.getValueFactory().setValue(formation.getDureeHeures());
    }

    private void updateStatusFormation(String message) {
        if (statusFormationLabel != null) {
            statusFormationLabel.setText(message);
        }
    }

    // Méthodes d'affichage des messages
    private void showSuccessFormation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showErrorFormation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarningFormation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthodes FXML pour les actions (stubs pour éviter les erreurs)
    @FXML private void rechercherFormation() { /* TODO */ }
    @FXML private void filtrerParDuree() { /* TODO */ }
    @FXML private void effacerRechercheFormation() { chargerFormations(); }
    @FXML private void inscrireEmploye() { /* TODO */ }
    @FXML private void changerStatut() { /* TODO */ }
    @FXML private void desinscrireEmploye() { /* TODO */ }
}
