package ma.enset.exam2test.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class formation {
    private int id;
    private String nom;
    private String description;
    private int dureeHeures;
    private LocalDateTime dateCreation;

    // Constructeurs
    public formation() {
        this.dateCreation = LocalDateTime.now();
    }

    public formation(String nom, String description, int dureeHeures) {
        this();
        this.nom = nom;
        this.description = description;
        this.dureeHeures = dureeHeures;
    }

    public formation(int id, String nom, String description, int dureeHeures, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.dureeHeures = dureeHeures;
        this.dateCreation = dateCreation;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDureeHeures() {
        return dureeHeures;
    }

    public void setDureeHeures(int dureeHeures) {
        this.dureeHeures = dureeHeures;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        formation formation = (formation) o;
        return id == formation.id && Objects.equals(nom, formation.nom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom);
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", dureeHeures=" + dureeHeures +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
