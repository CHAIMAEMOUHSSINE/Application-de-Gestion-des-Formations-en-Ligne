package ma.enset.exam2test.DAO;

import ma.enset.exam2test.entities.employe;
import java.util.List;

public interface employeDAO {
    // Opérations CRUD de base
    employe save(employe employe);
    employe findById(int id);
    List<employe> findAll();
    employe update(employe employe);
    boolean delete(int id);

    // Méthodes spécifiques
    employe findByEmail(String email);
    List<employe> findByPoste(String poste);
    List<employe> findByNomOrPrenom(String searchTerm);
}
