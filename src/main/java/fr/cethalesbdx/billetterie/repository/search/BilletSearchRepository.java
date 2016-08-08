package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.Billet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Billet entity.
 */
public interface BilletSearchRepository extends ElasticsearchRepository<Billet, Long> {
}
