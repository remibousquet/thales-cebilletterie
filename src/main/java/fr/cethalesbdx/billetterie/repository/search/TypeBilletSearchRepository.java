package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.TypeBillet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeBillet entity.
 */
public interface TypeBilletSearchRepository extends ElasticsearchRepository<TypeBillet, Long> {
}
