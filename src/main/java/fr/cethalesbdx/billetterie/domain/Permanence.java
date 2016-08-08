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
 * A Permanence.
 */
@Entity
@Table(name = "permanence")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "permanence")
public class Permanence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @NotNull
    @Size(max = 20)
    @Column(name = "horaire", length = 20, nullable = false)
    private String horaire;

    @Size(max = 256)
    @Column(name = "message", length = 256)
    private String message;

    @ManyToOne
    private User salarie;

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

    public String getHoraire() {
        return horaire;
    }

    public void setHoraire(String horaire) {
        this.horaire = horaire;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        Permanence permanence = (Permanence) o;
        if(permanence.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, permanence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Permanence{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", horaire='" + horaire + "'" +
            ", message='" + message + "'" +
            '}';
    }
}
