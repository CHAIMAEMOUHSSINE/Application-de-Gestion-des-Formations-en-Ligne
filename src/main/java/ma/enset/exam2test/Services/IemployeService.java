package ma.enset.exam2test.Services;

import ma.enset.exam2test.entities.employe;
import java.util.List;
import java.io.File;

public interface IemployeService {
    // Gestion des employés
    employe ajouterEmploye(employe employe);
    employe modifierEmploye(employe employe);
    boolean supprimerEmploye(int id);
    employe obtenirEmploye(int id);
    List<employe> obtenirTousLesEmployes();

    // Recherche
    employe rechercherParEmail(String email);
    List<employe> rechercherParPoste(String poste);
    List<employe> rechercherParNom(String searchTerm);

    // Validation
    boolean validerEmail(String email);
    boolean emailExiste(String email);

    // Export
    File exporterEmployesCSV();
    void exporterEmployesCSVAsync(ExportCallback callback);

    // Interface pour le callback d'export asynchrone
    interface ExportCallback {
        void onSuccess(File file);
        void onError(Exception e);
        void onProgress(int progress);
    }
}
