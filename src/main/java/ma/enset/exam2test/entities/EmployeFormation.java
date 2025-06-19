package ma.enset.exam2test.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class EmployeFormation {
    private int id;
    private int employeId;
    private int formationId;
    private LocalDateTime dateInscription;
    private StatutFormation statut;
    
    // Objets liés (pour faciliter l'affichage)
    private employe employe;
    private formation formation;

    public enum StatutFormation {
        EN_COURS, TERMINEE, ABANDONNEE
    }

    // Constructeurs
    public EmployeFormation() {
        this.dateInscription = LocalDateTime.now();
        this.statut = StatutFormation.EN_COURS;
    }

    public EmployeFormation(int employeId, int formationId) {
        this();
        this.employeId = employeId;
        this.formationId = formationId;
    }

    public EmployeFormation(int id, int employeId, int formationId, LocalDateTime dateInscription, StatutFormation statut) {
        this.id = id;
        this.employeId = employeId;
        this.formationId = formationId;
        this.dateInscription = dateInscription;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeId() {
        return employeId;
    }

    public void setEmployeId(int employeId) {
        this.employeId = employeId;
    }

    public int getFormationId() {
        return formationId;
    }

    public void setFormationId(int formationId) {
        this.formationId = formationId;
    }

    public LocalDateTime getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(LocalDateTime dateInscription) {
        this.dateInscription = dateInscription;
    }

    public StatutFormation getStatut() {
        return statut;
    }

    public void setStatut(StatutFormation statut) {
        this.statut = statut;
    }

    public employe getEmploye() {
        return employe;
    }

    public void setEmploye(employe employe) {
        this.employe = employe;
    }

    public formation getFormation() {
        return formation;
    }

    public void setFormation(formation formation) {
        this.formation = formation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeFormation that = (EmployeFormation) o;
        return id == that.id && employeId == that.employeId && formationId == that.formationId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, employeId, formationId);
    }

    @Override
    public String toString() {
        return "EmployeFormation{" +
                "id=" + id +
                ", employeId=" + employeId +
                ", formationId=" + formationId +
                ", dateInscription=" + dateInscription +
                ", statut=" + statut +
                '}';
    }
}
