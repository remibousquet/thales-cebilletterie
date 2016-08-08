package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.TitreTypeBillet;
import fr.cethalesbdx.billetterie.repository.TitreTypeBilletRepository;
import fr.cethalesbdx.billetterie.repository.search.TitreTypeBilletSearchRepository;
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
 * REST controller for managing TitreTypeBillet.
 */
@RestController
@RequestMapping("/api")
public class TitreTypeBilletResource {

    private final Logger log = LoggerFactory.getLogger(TitreTypeBilletResource.class);
        
    @Inject
    private TitreTypeBilletRepository titreTypeBilletRepository;
    
    @Inject
    private TitreTypeBilletSearchRepository titreTypeBilletSearchRepository;
    
    /**
     * POST  /titre-type-billets : Create a new titreTypeBillet.
     *
     * @param titreTypeBillet the titreTypeBillet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new titreTypeBillet, or with status 400 (Bad Request) if the titreTypeBillet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titre-type-billets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TitreTypeBillet> createTitreTypeBillet(@Valid @RequestBody TitreTypeBillet titreTypeBillet) throws URISyntaxException {
        log.debug("REST request to save TitreTypeBillet : {}", titreTypeBillet);
        if (titreTypeBillet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("titreTypeBillet", "idexists", "A new titreTypeBillet cannot already have an ID")).body(null);
        }
        TitreTypeBillet result = titreTypeBilletRepository.save(titreTypeBillet);
        titreTypeBilletSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/titre-type-billets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("titreTypeBillet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /titre-type-billets : Updates an existing titreTypeBillet.
     *
     * @param titreTypeBillet the titreTypeBillet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated titreTypeBillet,
     * or with status 400 (Bad Request) if the titreTypeBillet is not valid,
     * or with status 500 (Internal Server Error) if the titreTypeBillet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/titre-type-billets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TitreTypeBillet> updateTitreTypeBillet(@Valid @RequestBody TitreTypeBillet titreTypeBillet) throws URISyntaxException {
        log.debug("REST request to update TitreTypeBillet : {}", titreTypeBillet);
        if (titreTypeBillet.getId() == null) {
            return createTitreTypeBillet(titreTypeBillet);
        }
        TitreTypeBillet result = titreTypeBilletRepository.save(titreTypeBillet);
        titreTypeBilletSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("titreTypeBillet", titreTypeBillet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /titre-type-billets : get all the titreTypeBillets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of titreTypeBillets in body
     */
    @RequestMapping(value = "/titre-type-billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TitreTypeBillet> getAllTitreTypeBillets() {
        log.debug("REST request to get all TitreTypeBillets");
        List<TitreTypeBillet> titreTypeBillets = titreTypeBilletRepository.findAll();
        return titreTypeBillets;
    }

    /**
     * GET  /titre-type-billets/:id : get the "id" titreTypeBillet.
     *
     * @param id the id of the titreTypeBillet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the titreTypeBillet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/titre-type-billets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TitreTypeBillet> getTitreTypeBillet(@PathVariable Long id) {
        log.debug("REST request to get TitreTypeBillet : {}", id);
        TitreTypeBillet titreTypeBillet = titreTypeBilletRepository.findOne(id);
        return Optional.ofNullable(titreTypeBillet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /titre-type-billets/:id : delete the "id" titreTypeBillet.
     *
     * @param id the id of the titreTypeBillet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/titre-type-billets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTitreTypeBillet(@PathVariable Long id) {
        log.debug("REST request to delete TitreTypeBillet : {}", id);
        titreTypeBilletRepository.delete(id);
        titreTypeBilletSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("titreTypeBillet", id.toString())).build();
    }

    /**
     * SEARCH  /_search/titre-type-billets?query=:query : search for the titreTypeBillet corresponding
     * to the query.
     *
     * @param query the query of the titreTypeBillet search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/titre-type-billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TitreTypeBillet> searchTitreTypeBillets(@RequestParam String query) {
        log.debug("REST request to search TitreTypeBillets for query {}", query);
        return StreamSupport
            .stream(titreTypeBilletSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
