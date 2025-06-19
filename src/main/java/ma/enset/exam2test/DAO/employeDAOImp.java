package ma.enset.exam2test.DAO;

import ma.enset.exam2test.entities.employe;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class employeDAOImp implements employeDAO {

    @Override
    public employe save(employe employe) {
        String sql = "INSERT INTO employes (nom, prenom, email, poste) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setString(3, employe.getEmail());
            stmt.setString(4, employe.getPoste());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employe.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return employe;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la sauvegarde de l'employé", e);
        }
    }

    @Override
    public employe findById(int id) {
        String sql = "SELECT * FROM employes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmploye(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche de l'employé", e);
        }
        return null;
    }

    @Override
    public List<employe> findAll() {
        List<employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes ORDER BY nom, prenom";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employes.add(mapResultSetToEmploye(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des employés", e);
        }
        return employes;
    }

    @Override
    public employe update(employe employe) {
        String sql = "UPDATE employes SET nom = ?, prenom = ?, email = ?, poste = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, employe.getNom());
            stmt.setString(2, employe.getPrenom());
            stmt.setString(3, employe.getEmail());
            stmt.setString(4, employe.getPoste());
            stmt.setInt(5, employe.getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                return employe;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'employé", e);
        }
        return null;
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM employes WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'employé", e);
        }
    }

    @Override
    public employe findByEmail(String email) {
        String sql = "SELECT * FROM employes WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmploye(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par email", e);
        }
        return null;
    }

    @Override
    public List<employe> findByPoste(String poste) {
        List<employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes WHERE poste = ? ORDER BY nom, prenom";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, poste);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employes.add(mapResultSetToEmploye(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par poste", e);
        }
        return employes;
    }

    @Override
    public List<employe> findByNomOrPrenom(String searchTerm) {
        List<employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes WHERE nom LIKE ? OR prenom LIKE ? ORDER BY nom, prenom";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + searchTerm + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    employes.add(mapResultSetToEmploye(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche par nom/prénom", e);
        }
        return employes;
    }

    private employe mapResultSetToEmploye(ResultSet rs) throws SQLException {
        employe emp = new employe();
        emp.setId(rs.getInt("id"));
        emp.setNom(rs.getString("nom"));
        emp.setPrenom(rs.getString("prenom"));
        emp.setEmail(rs.getString("email"));
        emp.setPoste(rs.getString("poste"));

        Timestamp timestamp = rs.getTimestamp("date_creation");
        if (timestamp != null) {
            emp.setDateCreation(timestamp.toLocalDateTime());
        }

        return emp;
    }
}
