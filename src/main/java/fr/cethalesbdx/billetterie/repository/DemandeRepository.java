package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Demande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Demande entity.
 */
@SuppressWarnings("unused")
public interface DemandeRepository extends JpaRepository<Demande,Long> {

}
