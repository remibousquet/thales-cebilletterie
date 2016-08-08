package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Permanence;
import fr.cethalesbdx.billetterie.repository.PermanenceRepository;
import fr.cethalesbdx.billetterie.repository.search.PermanenceSearchRepository;
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
 * REST controller for managing Permanence.
 */
@RestController
@RequestMapping("/api")
public class PermanenceResource {

    private final Logger log = LoggerFactory.getLogger(PermanenceResource.class);
        
    @Inject
    private PermanenceRepository permanenceRepository;
    
    @Inject
    private PermanenceSearchRepository permanenceSearchRepository;
    
    /**
     * POST  /permanences : Create a new permanence.
     *
     * @param permanence the permanence to create
     * @return the ResponseEntity with status 201 (Created) and with body the new permanence, or with status 400 (Bad Request) if the permanence has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/permanences",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permanence> createPermanence(@Valid @RequestBody Permanence permanence) throws URISyntaxException {
        log.debug("REST request to save Permanence : {}", permanence);
        if (permanence.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permanence", "idexists", "A new permanence cannot already have an ID")).body(null);
        }
        Permanence result = permanenceRepository.save(permanence);
        permanenceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/permanences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("permanence", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permanences : Updates an existing permanence.
     *
     * @param permanence the permanence to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated permanence,
     * or with status 400 (Bad Request) if the permanence is not valid,
     * or with status 500 (Internal Server Error) if the permanence couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/permanences",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permanence> updatePermanence(@Valid @RequestBody Permanence permanence) throws URISyntaxException {
        log.debug("REST request to update Permanence : {}", permanence);
        if (permanence.getId() == null) {
            return createPermanence(permanence);
        }
        Permanence result = permanenceRepository.save(permanence);
        permanenceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("permanence", permanence.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permanences : get all the permanences.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of permanences in body
     */
    @RequestMapping(value = "/permanences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Permanence> getAllPermanences() {
        log.debug("REST request to get all Permanences");
        List<Permanence> permanences = permanenceRepository.findAll();
        return permanences;
    }

    /**
     * GET  /permanences/:id : get the "id" permanence.
     *
     * @param id the id of the permanence to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the permanence, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/permanences/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permanence> getPermanence(@PathVariable Long id) {
        log.debug("REST request to get Permanence : {}", id);
        Permanence permanence = permanenceRepository.findOne(id);
        return Optional.ofNullable(permanence)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /permanences/:id : delete the "id" permanence.
     *
     * @param id the id of the permanence to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/permanences/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePermanence(@PathVariable Long id) {
        log.debug("REST request to delete Permanence : {}", id);
        permanenceRepository.delete(id);
        permanenceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("permanence", id.toString())).build();
    }

    /**
     * SEARCH  /_search/permanences?query=:query : search for the permanence corresponding
     * to the query.
     *
     * @param query the query of the permanence search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/permanences",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Permanence> searchPermanences(@RequestParam String query) {
        log.debug("REST request to search Permanences for query {}", query);
        return StreamSupport
            .stream(permanenceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
