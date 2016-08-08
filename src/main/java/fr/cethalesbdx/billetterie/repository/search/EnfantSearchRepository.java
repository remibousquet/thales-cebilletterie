package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Enfant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Enfant entity.
 */
public interface EnfantSearchRepository extends ElasticsearchRepository<Enfant, Long> {
}
