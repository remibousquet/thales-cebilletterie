package fr.cethalesbdx.billetterie.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * L'entité Salarie n'est pas générée car est remplacée par l'entité native User qui a les mêmes champsentity Salarie {Nom String required maxlength(100),Prenom String required maxlength(100),Compte String maxlength(100),MotDePasse String maxlength(100),AdresseMail String maxlength(100),Actif Boolean}
 * 
 */
@ApiModel(description = ""
    + "L'entité Salarie n'est pas générée car est remplacée par l'entité native User qui a les mêmes champsentity Salarie {Nom String required maxlength(100),Prenom String required maxlength(100),Compte String maxlength(100),MotDePasse String maxlength(100),AdresseMail String maxlength(100),Actif Boolean}"
    + "")
@Entity
@Table(name = "subvention")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "subvention")
public class Subvention implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "subvention_annee", nullable = false)
    private Integer subventionAnnee;

    @NotNull
    @Column(name = "subvention_montant", precision=10, scale=2, nullable = false)
    private BigDecimal subventionMontant;

    @ManyToOne
    private User salarie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSubventionAnnee() {
        return subventionAnnee;
    }

    public void setSubventionAnnee(Integer subventionAnnee) {
        this.subventionAnnee = subventionAnnee;
    }

    public BigDecimal getSubventionMontant() {
        return subventionMontant;
    }

    public void setSubventionMontant(BigDecimal subventionMontant) {
        this.subventionMontant = subventionMontant;
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
        Subvention subvention = (Subvention) o;
        if(subvention.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, subvention.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Subvention{" +
            "id=" + id +
            ", subventionAnnee='" + subventionAnnee + "'" +
            ", subventionMontant='" + subventionMontant + "'" +
            '}';
    }
}
