package fr.cethalesbdx.billetterie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Enfant.
 */
@Entity
@Table(name = "enfant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "enfant")
public class Enfant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nom", length = 100, nullable = false)
    private String nom;

    @NotNull
    @Size(max = 100)
    @Column(name = "prenom", length = 100, nullable = false)
    private String prenom;

    @NotNull
    @Column(name = "date_naissance", nullable = false)
    private ZonedDateTime dateNaissance;

    @ManyToOne
    private User salarie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public ZonedDateTime getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(ZonedDateTime dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public User getSalarie() {
        return salarie;
    }

    public void setSalarie(User user) {
        this.salarie = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Enfant enfant = (Enfant) o;
        if(enfant.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, enfant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Enfant{" +
            "id=" + id +
            ", nom='" + nom + "'" +
            ", prenom='" + prenom + "'" +
            ", dateNaissance='" + dateNaissance + "'" +
            '}';
    }
}
