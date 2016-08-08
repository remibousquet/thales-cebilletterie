package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Paiement;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Paiement entity.
 */
@SuppressWarnings("unused")
public interface PaiementRepository extends JpaRepository<Paiement,Long> {

}
