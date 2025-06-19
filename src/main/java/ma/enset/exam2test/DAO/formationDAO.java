package ma.enset.exam2test.DAO;

import ma.enset.exam2test.entities.formation;
import ma.enset.exam2test.entities.EmployeFormation;
import java.util.List;

public interface formationDAO {
    // Opérations CRUD de base pour les formations
    formation save(formation formation);
    formation findById(int id);
    List<formation> findAll();
    formation update(formation formation);
    boolean delete(int id);

    // Méthodes spécifiques aux formations
    List<formation> findByNom(String nom);
    List<formation> findByDureeHeures(int dureeMin, int dureeMax);

    // Gestion des inscriptions employé-formation
    EmployeFormation inscrireEmploye(int employeId, int formationId);
    boolean desinscrireEmploye(int employeId, int formationId);
    List<EmployeFormation> getFormationsParEmploye(int employeId);
    List<EmployeFormation> getEmployesParFormation(int formationId);
    boolean updateStatutFormation(int employeId, int formationId, EmployeFormation.StatutFormation statut);
}
