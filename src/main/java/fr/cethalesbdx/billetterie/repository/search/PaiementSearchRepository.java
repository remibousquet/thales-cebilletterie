package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Paiement;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Paiement entity.
 */
public interface PaiementSearchRepository extends ElasticsearchRepository<Paiement, Long> {
}
