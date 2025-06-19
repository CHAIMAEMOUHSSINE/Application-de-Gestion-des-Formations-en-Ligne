package ma.enset.exam2test.Services;

import ma.enset.exam2test.entities.formation;
import ma.enset.exam2test.entities.EmployeFormation;
import java.util.List;

public interface IformationService {
    // Gestion des formations
    formation ajouterFormation(formation formation);
    formation modifierFormation(formation formation);
    boolean supprimerFormation(int id);
    formation obtenirFormation(int id);
    List<formation> obtenirToutesLesFormations();

    // Recherche de formations
    List<formation> rechercherParNom(String nom);
    List<formation> rechercherParDuree(int dureeMin, int dureeMax);

    // Gestion des inscriptions
    EmployeFormation inscrireEmployeAFormation(int employeId, int formationId);
    boolean desinscrireEmployeDeFormation(int employeId, int formationId);
    boolean modifierStatutFormation(int employeId, int formationId, EmployeFormation.StatutFormation statut);

    // Consultation des inscriptions
    List<EmployeFormation> obtenirFormationsParEmploye(int employeId);
    List<EmployeFormation> obtenirEmployesParFormation(int formationId);

    // Validation
    boolean employeDejaInscrit(int employeId, int formationId);
    boolean peutSInscrire(int employeId, int formationId);
}
