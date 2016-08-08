package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Billet;
import fr.cethalesbdx.billetterie.repository.BilletRepository;
import fr.cethalesbdx.billetterie.repository.search.BilletSearchRepository;
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
 * REST controller for managing Billet.
 */
@RestController
@RequestMapping("/api")
public class BilletResource {

    private final Logger log = LoggerFactory.getLogger(BilletResource.class);
        
    @Inject
    private BilletRepository billetRepository;
    
    @Inject
    private BilletSearchRepository billetSearchRepository;
    
    /**
     * POST  /billets : Create a new billet.
     *
     * @param billet the billet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new billet, or with status 400 (Bad Request) if the billet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billet> createBillet(@Valid @RequestBody Billet billet) throws URISyntaxException {
        log.debug("REST request to save Billet : {}", billet);
        if (billet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("billet", "idexists", "A new billet cannot already have an ID")).body(null);
        }
        Billet result = billetRepository.save(billet);
        billetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/billets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("billet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /billets : Updates an existing billet.
     *
     * @param billet the billet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated billet,
     * or with status 400 (Bad Request) if the billet is not valid,
     * or with status 500 (Internal Server Error) if the billet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/billets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billet> updateBillet(@Valid @RequestBody Billet billet) throws URISyntaxException {
        log.debug("REST request to update Billet : {}", billet);
        if (billet.getId() == null) {
            return createBillet(billet);
        }
        Billet result = billetRepository.save(billet);
        billetSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("billet", billet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /billets : get all the billets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of billets in body
     */
    @RequestMapping(value = "/billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Billet> getAllBillets() {
        log.debug("REST request to get all Billets");
        List<Billet> billets = billetRepository.findAll();
        return billets;
    }

    /**
     * GET  /billets/:id : get the "id" billet.
     *
     * @param id the id of the billet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the billet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/billets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Billet> getBillet(@PathVariable Long id) {
        log.debug("REST request to get Billet : {}", id);
        Billet billet = billetRepository.findOne(id);
        return Optional.ofNullable(billet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /billets/:id : delete the "id" billet.
     *
     * @param id the id of the billet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/billets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBillet(@PathVariable Long id) {
        log.debug("REST request to delete Billet : {}", id);
        billetRepository.delete(id);
        billetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("billet", id.toString())).build();
    }

    /**
     * SEARCH  /_search/billets?query=:query : search for the billet corresponding
     * to the query.
     *
     * @param query the query of the billet search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Billet> searchBillets(@RequestParam String query) {
        log.debug("REST request to search Billets for query {}", query);
        return StreamSupport
            .stream(billetSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
