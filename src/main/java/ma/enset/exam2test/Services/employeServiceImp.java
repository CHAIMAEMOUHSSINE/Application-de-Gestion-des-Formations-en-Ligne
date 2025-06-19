package ma.enset.exam2test.Services;

import ma.enset.exam2test.DAO.employeDAO;
import ma.enset.exam2test.DAO.employeDAOImp;
import ma.enset.exam2test.entities.employe;
import javafx.concurrent.Task;
import javafx.concurrent.Service;
import javafx.application.Platform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Pattern;

public class employeServiceImp implements IemployeService {

    private final employeDAO employeDAO;
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$");

    public employeServiceImp() {
        this.employeDAO = new employeDAOImp();
    }

    @Override
    public employe ajouterEmploye(employe employe) {
        if (employe == null) {
            throw new IllegalArgumentException("L'employé ne peut pas être null");
        }

        // Validation
        if (!validerEmail(employe.getEmail())) {
            throw new IllegalArgumentException("Email invalide");
        }

        if (emailExiste(employe.getEmail())) {
            throw new IllegalArgumentException("Un employé avec cet email existe déjà");
        }

        return employeDAO.save(employe);
    }

    @Override
    public employe modifierEmploye(employe employe) {
        if (employe == null || employe.getId() <= 0) {
            throw new IllegalArgumentException("Employé invalide");
        }

        // Vérifier que l'employé existe
        employe existant = employeDAO.findById(employe.getId());
        if (existant == null) {
            throw new IllegalArgumentException("Employé introuvable");
        }

        // Validation email si changé
        if (!existant.getEmail().equals(employe.getEmail())) {
            if (!validerEmail(employe.getEmail())) {
                throw new IllegalArgumentException("Email invalide");
            }
            if (emailExiste(employe.getEmail())) {
                throw new IllegalArgumentException("Un employé avec cet email existe déjà");
            }
        }

        return employeDAO.update(employe);
    }

    @Override
    public boolean supprimerEmploye(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }
        return employeDAO.delete(id);
    }

    @Override
    public employe obtenirEmploye(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalide");
        }
        return employeDAO.findById(id);
    }

    @Override
    public List<employe> obtenirTousLesEmployes() {
        return employeDAO.findAll();
    }

    @Override
    public employe rechercherParEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email ne peut pas être vide");
        }
        return employeDAO.findByEmail(email.trim());
    }

    @Override
    public List<employe> rechercherParPoste(String poste) {
        if (poste == null || poste.trim().isEmpty()) {
            throw new IllegalArgumentException("Poste ne peut pas être vide");
        }
        return employeDAO.findByPoste(poste.trim());
    }

    @Override
    public List<employe> rechercherParNom(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            throw new IllegalArgumentException("Terme de recherche ne peut pas être vide");
        }
        return employeDAO.findByNomOrPrenom(searchTerm.trim());
    }

    @Override
    public boolean validerEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    @Override
    public boolean emailExiste(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return employeDAO.findByEmail(email.trim()) != null;
    }

    @Override
    public File exporterEmployesCSV() {
        try {
            List<employe> employes = employeDAO.findAll();

            // Créer le fichier avec timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File file = new File("exports/employes_" + timestamp + ".csv");

            // Créer le dossier s'il n'existe pas
            file.getParentFile().mkdirs();

            try (FileWriter writer = new FileWriter(file)) {
                // En-têtes CSV
                writer.append("ID,Nom,Prénom,Email,Poste,Date de création\n");

                // Données
                for (employe emp : employes) {
                    writer.append(String.valueOf(emp.getId())).append(",");
                    writer.append(escapeCSV(emp.getNom())).append(",");
                    writer.append(escapeCSV(emp.getPrenom())).append(",");
                    writer.append(escapeCSV(emp.getEmail())).append(",");
                    writer.append(escapeCSV(emp.getPoste())).append(",");
                    writer.append(emp.getDateCreation() != null ?
                        emp.getDateCreation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "")
                        .append("\n");
                }
            }

            return file;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'export CSV", e);
        }
    }

    @Override
    public void exporterEmployesCSVAsync(ExportCallback callback) {
        Task<File> exportTask = new Task<File>() {
            @Override
            protected File call() throws Exception {
                List<employe> employes = employeDAO.findAll();
                int total = employes.size();

                // Créer le fichier avec timestamp
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
                File file = new File("exports/employes_" + timestamp + ".csv");

                // Créer le dossier s'il n'existe pas
                file.getParentFile().mkdirs();

                try (FileWriter writer = new FileWriter(file)) {
                    // En-têtes CSV
                    writer.append("ID,Nom,Prénom,Email,Poste,Date de création\n");
                    updateProgress(1, total + 1);

                    // Données
                    for (int i = 0; i < employes.size(); i++) {
                        employe emp = employes.get(i);
                        writer.append(String.valueOf(emp.getId())).append(",");
                        writer.append(escapeCSV(emp.getNom())).append(",");
                        writer.append(escapeCSV(emp.getPrenom())).append(",");
                        writer.append(escapeCSV(emp.getEmail())).append(",");
                        writer.append(escapeCSV(emp.getPoste())).append(",");
                        writer.append(emp.getDateCreation() != null ?
                            emp.getDateCreation().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "")
                            .append("\n");

                        updateProgress(i + 2, total + 1);

                        // Simuler un délai pour voir la progress bar
                        Thread.sleep(50);
                    }
                }

                return file;
            }
        };

        exportTask.setOnSucceeded(e -> {
            Platform.runLater(() -> callback.onSuccess(exportTask.getValue()));
        });

        exportTask.setOnFailed(e -> {
            Platform.runLater(() -> callback.onError((Exception) exportTask.getException()));
        });

        exportTask.progressProperty().addListener((obs, oldProgress, newProgress) -> {
            Platform.runLater(() -> callback.onProgress((int) (newProgress.doubleValue() * 100)));
        });

        Thread exportThread = new Thread(exportTask);
        exportThread.setDaemon(true);
        exportThread.start();
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
