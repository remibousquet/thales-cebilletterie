package fr.cethalesbdx.billetterie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "demande")
public class Demande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 100)
    @Column(name = "numero_externe", length = 100)
    private String numeroExterne;

    @NotNull
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Size(max = 256)
    @Column(name = "commentaire", length = 256)
    private String commentaire;

    @ManyToOne
    private StatutDemande statut;

    @ManyToOne
    private Paiement paiement;

    @ManyToOne
    private Billet billet;

    @ManyToOne
    private TypeDemande type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroExterne() {
        return numeroExterne;
    }

    public void setNumeroExterne(String numeroExterne) {
        this.numeroExterne = numeroExterne;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statutDemande) {
        this.statut = statutDemande;
    }

    public Paiement getPaiement() {
        return paiement;
    }

    public void setPaiement(Paiement paiement) {
        this.paiement = paiement;
    }

    public Billet getBillet() {
        return billet;
    }

    public void setBillet(Billet billet) {
        this.billet = billet;
    }

    public TypeDemande getType() {
        return type;
    }

    public void setType(TypeDemande typeDemande) {
        this.type = typeDemande;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Demande demande = (Demande) o;
        if(demande.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, demande.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Demande{" +
            "id=" + id +
            ", numeroExterne='" + numeroExterne + "'" +
            ", quantite='" + quantite + "'" +
            ", commentaire='" + commentaire + "'" +
            '}';
    }
}
