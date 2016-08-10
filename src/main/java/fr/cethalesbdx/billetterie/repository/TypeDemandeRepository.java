package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.TypeDemande;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeDemande entity.
 */
@SuppressWarnings("unused")
public interface TypeDemandeRepository extends JpaRepository<TypeDemande,Long> {

}
