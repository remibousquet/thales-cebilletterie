package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.TitreTypeBillet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TitreTypeBillet entity.
 */
public interface TitreTypeBilletSearchRepository extends ElasticsearchRepository<TitreTypeBillet, Long> {
}
