package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Permanence;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Permanence entity.
 */
@SuppressWarnings("unused")
public interface PermanenceRepository extends JpaRepository<Permanence,Long> {

    @Query("select permanence from Permanence permanence where permanence.salarie.login = ?#{principal.username}")
    List<Permanence> findBySalarieIsCurrentUser();

}
