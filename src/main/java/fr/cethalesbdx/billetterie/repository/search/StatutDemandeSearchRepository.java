package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.StatutDemande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the StatutDemande entity.
 */
public interface StatutDemandeSearchRepository extends ElasticsearchRepository<StatutDemande, Long> {
}
