package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Enfant;
import fr.cethalesbdx.billetterie.repository.EnfantRepository;
import fr.cethalesbdx.billetterie.repository.search.EnfantSearchRepository;
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
 * REST controller for managing Enfant.
 */
@RestController
@RequestMapping("/api")
public class EnfantResource {

    private final Logger log = LoggerFactory.getLogger(EnfantResource.class);
        
    @Inject
    private EnfantRepository enfantRepository;
    
    @Inject
    private EnfantSearchRepository enfantSearchRepository;
    
    /**
     * POST  /enfants : Create a new enfant.
     *
     * @param enfant the enfant to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enfant, or with status 400 (Bad Request) if the enfant has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enfants",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> createEnfant(@Valid @RequestBody Enfant enfant) throws URISyntaxException {
        log.debug("REST request to save Enfant : {}", enfant);
        if (enfant.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("enfant", "idexists", "A new enfant cannot already have an ID")).body(null);
        }
        Enfant result = enfantRepository.save(enfant);
        enfantSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/enfants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("enfant", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enfants : Updates an existing enfant.
     *
     * @param enfant the enfant to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enfant,
     * or with status 400 (Bad Request) if the enfant is not valid,
     * or with status 500 (Internal Server Error) if the enfant couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/enfants",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> updateEnfant(@Valid @RequestBody Enfant enfant) throws URISyntaxException {
        log.debug("REST request to update Enfant : {}", enfant);
        if (enfant.getId() == null) {
            return createEnfant(enfant);
        }
        Enfant result = enfantRepository.save(enfant);
        enfantSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("enfant", enfant.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enfants : get all the enfants.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of enfants in body
     */
    @RequestMapping(value = "/enfants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enfant> getAllEnfants() {
        log.debug("REST request to get all Enfants");
        List<Enfant> enfants = enfantRepository.findAll();
        return enfants;
    }

    /**
     * GET  /enfants/:id : get the "id" enfant.
     *
     * @param id the id of the enfant to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enfant, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/enfants/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Enfant> getEnfant(@PathVariable Long id) {
        log.debug("REST request to get Enfant : {}", id);
        Enfant enfant = enfantRepository.findOne(id);
        return Optional.ofNullable(enfant)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enfants/:id : delete the "id" enfant.
     *
     * @param id the id of the enfant to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/enfants/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteEnfant(@PathVariable Long id) {
        log.debug("REST request to delete Enfant : {}", id);
        enfantRepository.delete(id);
        enfantSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("enfant", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enfants?query=:query : search for the enfant corresponding
     * to the query.
     *
     * @param query the query of the enfant search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/enfants",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Enfant> searchEnfants(@RequestParam String query) {
        log.debug("REST request to search Enfants for query {}", query);
        return StreamSupport
            .stream(enfantSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
