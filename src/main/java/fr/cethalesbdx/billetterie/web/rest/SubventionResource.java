package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Subvention;
import fr.cethalesbdx.billetterie.repository.SubventionRepository;
import fr.cethalesbdx.billetterie.repository.search.SubventionSearchRepository;
import fr.cethalesbdx.billetterie.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Subvention.
 */
@RestController
@RequestMapping("/api")
public class SubventionResource {

    private final Logger log = LoggerFactory.getLogger(SubventionResource.class);
        
    @Inject
    private SubventionRepository subventionRepository;
    
    @Inject
    private SubventionSearchRepository subventionSearchRepository;
    
    /**
     * POST  /subventions : Create a new subvention.
     *
     * @param subvention the subvention to create
     * @return the ResponseEntity with status 201 (Created) and with body the new subvention, or with status 400 (Bad Request) if the subvention has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subventions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subvention> createSubvention(@Valid @RequestBody Subvention subvention) throws URISyntaxException {
        log.debug("REST request to save Subvention : {}", subvention);
        if (subvention.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("subvention", "idexists", "A new subvention cannot already have an ID")).body(null);
        }
        Subvention result = subventionRepository.save(subvention);
        subventionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/subventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("subvention", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /subventions : Updates an existing subvention.
     *
     * @param subvention the subvention to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated subvention,
     * or with status 400 (Bad Request) if the subvention is not valid,
     * or with status 500 (Internal Server Error) if the subvention couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/subventions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subvention> updateSubvention(@Valid @RequestBody Subvention subvention) throws URISyntaxException {
        log.debug("REST request to update Subvention : {}", subvention);
        if (subvention.getId() == null) {
            return createSubvention(subvention);
        }
        Subvention result = subventionRepository.save(subvention);
        subventionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("subvention", subvention.getId().toString()))
            .body(result);
    }

    /**
     * GET  /subventions : get all the subventions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of subventions in body
     */
    @RequestMapping(value = "/subventions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Subvention> getAllSubventions() {
        log.debug("REST request to get all Subventions");
        List<Subvention> subventions = subventionRepository.findAll();
        return subventions;
    }

    /**
     * GET  /subventions/:id : get the "id" subvention.
     *
     * @param id the id of the subvention to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the subvention, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/subventions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Subvention> getSubvention(@PathVariable Long id) {
        log.debug("REST request to get Subvention : {}", id);
        Subvention subvention = subventionRepository.findOne(id);
        return Optional.ofNullable(subvention)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /subventions/:id : delete the "id" subvention.
     *
     * @param id the id of the subvention to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/subventions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSubvention(@PathVariable Long id) {
        log.debug("REST request to delete Subvention : {}", id);
        subventionRepository.delete(id);
        subventionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("subvention", id.toString())).build();
    }

    /**
     * SEARCH  /_search/subventions?query=:query : search for the subvention corresponding
     * to the query.
     *
     * @param query the query of the subvention search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/subventions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Subvention> searchSubventions(@RequestParam String query) {
        log.debug("REST request to search Subventions for query {}", query);
        return StreamSupport
            .stream(subventionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
