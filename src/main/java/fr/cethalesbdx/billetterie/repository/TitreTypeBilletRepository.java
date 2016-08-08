package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.TitreTypeBillet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TitreTypeBillet entity.
 */
@SuppressWarnings("unused")
public interface TitreTypeBilletRepository extends JpaRepository<TitreTypeBillet,Long> {

}
