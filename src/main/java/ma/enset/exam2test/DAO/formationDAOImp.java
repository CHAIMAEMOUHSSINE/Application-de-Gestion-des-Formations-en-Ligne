package ma.enset.exam2test.DAO;

import ma.enset.exam2test.entities.formation;
import ma.enset.exam2test.entities.EmployeFormation;
import ma.enset.exam2test.entities.employe;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class formationDAOImp implements formationDAO {

    @Override
    public formation save(formation formation) {
        String sql = "INSERT INTO formations (nom, description, duree_heures) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, formation.getNom());
            stmt.setString(2, formation.getDescription());
            stmt.setInt(3, formation.getDureeHeures());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        formation.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return formation;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de la formation", e);
        }
    }

    @Override
    public formation findById(int id) {
        String sql = "SELECT * FROM formations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToFormation(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de la formation", e);
        }
        return null;
    }

    @Override
    public List<formation> findAll() {
        List<formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formations ORDER BY nom";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                formations.add(mapResultSetToFormation(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des formations", e);
        }
        return formations;
    }

    @Override
    public formation update(formation formation) {
        String sql = "UPDATE formations SET nom = ?, description = ?, duree_heures = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, formation.getNom());
            stmt.setString(2, formation.getDescription());
            stmt.setInt(3, formation.getDureeHeures());
            stmt.setInt(4, formation.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return formation;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de la formation", e);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM formations WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de la formation", e);
        }
    }

    @Override
    public List<formation> findByNom(String nom) {
        List<formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formations WHERE nom LIKE ? ORDER BY nom";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + nom + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formations.add(mapResultSetToFormation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom", e);
        }
        return formations;
    }

    @Override
    public List<formation> findByDureeHeures(int dureeMin, int dureeMax) {
        List<formation> formations = new ArrayList<>();
        String sql = "SELECT * FROM formations WHERE duree_heures BETWEEN ? AND ? ORDER BY duree_heures";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dureeMin);
            stmt.setInt(2, dureeMax);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formations.add(mapResultSetToFormation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par durée", e);
        }
        return formations;
    }

    @Override
    public EmployeFormation inscrireEmploye(int employeId, int formationId) {
        String sql = "INSERT INTO employe_formations (employe_id, formation_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, employeId);
            stmt.setInt(2, formationId);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        EmployeFormation ef = new EmployeFormation(employeId, formationId);
                        ef.setId(generatedKeys.getInt(1));
                        return ef;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'inscription", e);
        }
        return null;
    }

    @Override
    public boolean desinscrireEmploye(int employeId, int formationId) {
        String sql = "DELETE FROM employe_formations WHERE employe_id = ? AND formation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeId);
            stmt.setInt(2, formationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la désinscription", e);
        }
    }

    @Override
    public List<EmployeFormation> getFormationsParEmploye(int employeId) {
        List<EmployeFormation> formations = new ArrayList<>();
        String sql = """
            SELECT ef.*, e.nom as emp_nom, e.prenom as emp_prenom, e.email as emp_email, e.poste as emp_poste,
                   f.nom as form_nom, f.description as form_description, f.duree_heures as form_duree
            FROM employe_formations ef
            JOIN employes e ON ef.employe_id = e.id
            JOIN formations f ON ef.formation_id = f.id
            WHERE ef.employe_id = ?
            ORDER BY ef.date_inscription DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    formations.add(mapResultSetToEmployeFormation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des formations de l'employé", e);
        }
        return formations;
    }

    @Override
    public List<EmployeFormation> getEmployesParFormation(int formationId) {
        List<EmployeFormation> employes = new ArrayList<>();
        String sql = """
            SELECT ef.*, e.nom as emp_nom, e.prenom as emp_prenom, e.email as emp_email, e.poste as emp_poste,
                   f.nom as form_nom, f.description as form_description, f.duree_heures as form_duree
            FROM employe_formations ef
            JOIN employes e ON ef.employe_id = e.id
            JOIN formations f ON ef.formation_id = f.id
            WHERE ef.formation_id = ?
            ORDER BY e.nom, e.prenom
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, formationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employes.add(mapResultSetToEmployeFormation(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des employés de la formation", e);
        }
        return employes;
    }

    @Override
    public boolean updateStatutFormation(int employeId, int formationId, EmployeFormation.StatutFormation statut) {
        String sql = "UPDATE employe_formations SET statut = ? WHERE employe_id = ? AND formation_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, statut.name());
            stmt.setInt(2, employeId);
            stmt.setInt(3, formationId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour du statut", e);
        }
    }

    private formation mapResultSetToFormation(ResultSet rs) throws SQLException {
        formation form = new formation();
        form.setId(rs.getInt("id"));
        form.setNom(rs.getString("nom"));
        form.setDescription(rs.getString("description"));
        form.setDureeHeures(rs.getInt("duree_heures"));

        Timestamp timestamp = rs.getTimestamp("date_creation");
        if (timestamp != null) {
            form.setDateCreation(timestamp.toLocalDateTime());
        }

        return form;
    }

    private EmployeFormation mapResultSetToEmployeFormation(ResultSet rs) throws SQLException {
        EmployeFormation ef = new EmployeFormation();
        ef.setId(rs.getInt("id"));
        ef.setEmployeId(rs.getInt("employe_id"));
        ef.setFormationId(rs.getInt("formation_id"));

        Timestamp timestamp = rs.getTimestamp("date_inscription");
        if (timestamp != null) {
            ef.setDateInscription(timestamp.toLocalDateTime());
        }

        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            ef.setStatut(EmployeFormation.StatutFormation.valueOf(statutStr));
        }

        // Mapper l'employé si les données sont présentes
        try {
            employe emp = new employe();
            emp.setId(rs.getInt("employe_id"));
            emp.setNom(rs.getString("emp_nom"));
            emp.setPrenom(rs.getString("emp_prenom"));
            emp.setEmail(rs.getString("emp_email"));
            emp.setPoste(rs.getString("emp_poste"));
            ef.setEmploye(emp);
        } catch (SQLException ignored) {
            // Les colonnes employé ne sont pas présentes dans cette requête
        }

        // Mapper la formation si les données sont présentes
        try {
            formation form = new formation();
            form.setId(rs.getInt("formation_id"));
            form.setNom(rs.getString("form_nom"));
            form.setDescription(rs.getString("form_description"));
            form.setDureeHeures(rs.getInt("form_duree"));
            ef.setFormation(form);
        } catch (SQLException ignored) {
            // Les colonnes formation ne sont pas présentes dans cette requête
        }

        return ef;
    }
}
