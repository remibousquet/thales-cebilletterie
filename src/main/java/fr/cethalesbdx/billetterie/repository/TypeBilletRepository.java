package fr.cethalesbdx.billetterie.repository;

import fr.cethalesbdx.billetterie.domain.TypeBillet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TypeBillet entity.
 */
@SuppressWarnings("unused")
public interface TypeBilletRepository extends JpaRepository<TypeBillet,Long> {

}
