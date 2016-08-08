package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Demande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Demande entity.
 */
public interface DemandeSearchRepository extends ElasticsearchRepository<Demande, Long> {
}
