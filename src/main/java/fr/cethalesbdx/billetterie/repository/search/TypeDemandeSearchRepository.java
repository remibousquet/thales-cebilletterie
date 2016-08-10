package fr.cethalesbdx.billetterie.repository.search;

import fr.cethalesbdx.billetterie.domain.TypeDemande;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the TypeDemande entity.
 */
public interface TypeDemandeSearchRepository extends ElasticsearchRepository<TypeDemande, Long> {
}
