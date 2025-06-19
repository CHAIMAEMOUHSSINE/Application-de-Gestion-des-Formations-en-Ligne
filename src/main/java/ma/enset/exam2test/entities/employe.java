package ma.enset.exam2test.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class employe {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String poste;
    private LocalDateTime dateCreation;

    // Constructeurs
    public employe() {
        this.dateCreation = LocalDateTime.now();
    }

    public employe(String nom, String prenom, String email, String poste) {
        this();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.poste = poste;
    }

    public employe(int id, String nom, String prenom, String email, String poste, LocalDateTime dateCreation) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.poste = poste;
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

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    // Méthodes utilitaires
    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        employe employe = (employe) o;
        return id == employe.id && Objects.equals(email, employe.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "Employe{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", poste='" + poste + '\'' +
                ", dateCreation=" + dateCreation +
                '}';
    }
}
