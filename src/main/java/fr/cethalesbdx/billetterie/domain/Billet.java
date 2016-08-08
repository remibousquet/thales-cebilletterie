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
 * A Billet.
 */
@Entity
@Table(name = "billet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "billet")
public class Billet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "titre", length = 100, nullable = false)
    private String titre;

    @NotNull
    @Column(name = "type_date", nullable = false)
    private Boolean typeDate;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private ZonedDateTime dateDebut;

    @Column(name = "date_fin")
    private ZonedDateTime dateFin;

    @Size(max = 20)
    @Column(name = "horaire", length = 20)
    private String horaire;

    @Size(max = 100)
    @Column(name = "lieu", length = 100)
    private String lieu;

    @Size(max = 20)
    @Column(name = "zone_salle", length = 20)
    private String zoneSalle;

    @Size(max = 100)
    @Column(name = "type_public", length = 100)
    private String typePublic;

    @NotNull
    @Column(name = "prix_unitaire", precision=10, scale=2, nullable = false)
    private BigDecimal prixUnitaire;

    @Column(name = "quantite_stock")
    private Integer quantiteStock;

    @Size(max = 256)
    @Column(name = "commentaire", length = 256)
    private String commentaire;

    @ManyToOne
    private TypeBillet type;

    @ManyToOne
    private TitreTypeBillet titreType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Boolean isTypeDate() {
        return typeDate;
    }

    public void setTypeDate(Boolean typeDate) {
        this.typeDate = typeDate;
    }

    public ZonedDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(ZonedDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public ZonedDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(ZonedDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getZoneSalle() {
        return zoneSalle;
    }

    public void setZoneSalle(String zoneSalle) {
        this.zoneSalle = zoneSalle;
    }

    public String getTypePublic() {
        return typePublic;
    }

    public void setTypePublic(String typePublic) {
        this.typePublic = typePublic;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantiteStock() {
        return quantiteStock;
    }

    public void setQuantiteStock(Integer quantiteStock) {
        this.quantiteStock = quantiteStock;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public TypeBillet getType() {
        return type;
    }

    public void setType(TypeBillet typeBillet) {
        this.type = typeBillet;
    }

    public TitreTypeBillet getTitreType() {
        return titreType;
    }

    public void setTitreType(TitreTypeBillet titreTypeBillet) {
        this.titreType = titreTypeBillet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Billet billet = (Billet) o;
        if(billet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, billet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Billet{" +
            "id=" + id +
            ", titre='" + titre + "'" +
            ", typeDate='" + typeDate + "'" +
            ", dateDebut='" + dateDebut + "'" +
            ", dateFin='" + dateFin + "'" +
            ", horaire='" + horaire + "'" +
            ", lieu='" + lieu + "'" +
            ", zoneSalle='" + zoneSalle + "'" +
            ", typePublic='" + typePublic + "'" +
            ", prixUnitaire='" + prixUnitaire + "'" +
            ", quantiteStock='" + quantiteStock + "'" +
            ", commentaire='" + commentaire + "'" +
            '}';
    }
}
