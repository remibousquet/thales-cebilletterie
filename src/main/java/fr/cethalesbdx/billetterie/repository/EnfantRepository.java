package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Enfant;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Enfant entity.
 */
@SuppressWarnings("unused")
public interface EnfantRepository extends JpaRepository<Enfant,Long> {

    @Query("select enfant from Enfant enfant where enfant.salarie.login = ?#{principal.username}")
    List<Enfant> findBySalarieIsCurrentUser();

}
