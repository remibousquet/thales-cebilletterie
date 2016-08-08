package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.Subvention;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Subvention entity.
 */
@SuppressWarnings("unused")
public interface SubventionRepository extends JpaRepository<Subvention,Long> {

    @Query("select subvention from Subvention subvention where subvention.salarie.login = ?#{principal.username}")
    List<Subvention> findBySalarieIsCurrentUser();

}
