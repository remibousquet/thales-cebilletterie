package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Permanence;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Permanence entity.
 */
public interface PermanenceSearchRepository extends ElasticsearchRepository<Permanence, Long> {
}
