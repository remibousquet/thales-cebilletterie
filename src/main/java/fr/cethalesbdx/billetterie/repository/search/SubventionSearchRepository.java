package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Subvention;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Subvention entity.
 */
public interface SubventionSearchRepository extends ElasticsearchRepository<Subvention, Long> {
}
