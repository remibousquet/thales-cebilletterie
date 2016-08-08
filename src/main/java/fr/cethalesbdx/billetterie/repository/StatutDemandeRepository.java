package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.StatutDemande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StatutDemande entity.
 */
@SuppressWarnings("unused")
public interface StatutDemandeRepository extends JpaRepository<StatutDemande,Long> {

}
