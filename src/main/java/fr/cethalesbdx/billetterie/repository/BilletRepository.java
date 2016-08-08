package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Billet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Billet entity.
 */
@SuppressWarnings("unused")
public interface BilletRepository extends JpaRepository<Billet,Long> {

}
