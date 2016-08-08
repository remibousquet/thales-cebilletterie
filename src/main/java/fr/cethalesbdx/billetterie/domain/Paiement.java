package fr.cethalesbdx.billetterie.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Paiement.
 */
@Entity
@Table(name = "paiement")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "paiement")
public class Paiement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Size(max = 100)
    @Column(name = "banque", length = 100, nullable = false)
    private String banque;

    @NotNull
    @Size(max = 50)
    @Column(name = "numero_cheque", length = 50, nullable = false)
    private String numeroCheque;

    @NotNull
    @Column(name = "montant", precision=10, scale=2, nullable = false)
    private BigDecimal montant;

    @Size(max = 256)
    @Column(name = "commentaire", length = 256)
    private String commentaire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public String getBanque() {
        return banque;
    }

    public void setBanque(String banque) {
        this.banque = banque;
    }

    public String getNumeroCheque() {
        return numeroCheque;
    }

    public void setNumeroCheque(String numeroCheque) {
        this.numeroCheque = numeroCheque;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Paiement paiement = (Paiement) o;
        if(paiement.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, paiement.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Paiement{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", banque='" + banque + "'" +
            ", numeroCheque='" + numeroCheque + "'" +
            ", montant='" + montant + "'" +
            ", commentaire='" + commentaire + "'" +
            '}';
    }
}
