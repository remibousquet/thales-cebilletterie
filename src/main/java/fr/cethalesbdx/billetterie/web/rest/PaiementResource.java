package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Paiement;
import fr.cethalesbdx.billetterie.repository.PaiementRepository;
import fr.cethalesbdx.billetterie.repository.search.PaiementSearchRepository;
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
 * REST controller for managing Paiement.
 */
@RestController
@RequestMapping("/api")
public class PaiementResource {

    private final Logger log = LoggerFactory.getLogger(PaiementResource.class);
        
    @Inject
    private PaiementRepository paiementRepository;
    
    @Inject
    private PaiementSearchRepository paiementSearchRepository;
    
    /**
     * POST  /paiements : Create a new paiement.
     *
     * @param paiement the paiement to create
     * @return the ResponseEntity with status 201 (Created) and with body the new paiement, or with status 400 (Bad Request) if the paiement has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/paiements",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Paiement> createPaiement(@Valid @RequestBody Paiement paiement) throws URISyntaxException {
        log.debug("REST request to save Paiement : {}", paiement);
        if (paiement.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("paiement", "idexists", "A new paiement cannot already have an ID")).body(null);
        }
        Paiement result = paiementRepository.save(paiement);
        paiementSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/paiements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("paiement", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /paiements : Updates an existing paiement.
     *
     * @param paiement the paiement to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated paiement,
     * or with status 400 (Bad Request) if the paiement is not valid,
     * or with status 500 (Internal Server Error) if the paiement couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/paiements",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Paiement> updatePaiement(@Valid @RequestBody Paiement paiement) throws URISyntaxException {
        log.debug("REST request to update Paiement : {}", paiement);
        if (paiement.getId() == null) {
            return createPaiement(paiement);
        }
        Paiement result = paiementRepository.save(paiement);
        paiementSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("paiement", paiement.getId().toString()))
            .body(result);
    }

    /**
     * GET  /paiements : get all the paiements.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of paiements in body
     */
    @RequestMapping(value = "/paiements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Paiement> getAllPaiements() {
        log.debug("REST request to get all Paiements");
        List<Paiement> paiements = paiementRepository.findAll();
        return paiements;
    }

    /**
     * GET  /paiements/:id : get the "id" paiement.
     *
     * @param id the id of the paiement to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the paiement, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/paiements/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Paiement> getPaiement(@PathVariable Long id) {
        log.debug("REST request to get Paiement : {}", id);
        Paiement paiement = paiementRepository.findOne(id);
        return Optional.ofNullable(paiement)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /paiements/:id : delete the "id" paiement.
     *
     * @param id the id of the paiement to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/paiements/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePaiement(@PathVariable Long id) {
        log.debug("REST request to delete Paiement : {}", id);
        paiementRepository.delete(id);
        paiementSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("paiement", id.toString())).build();
    }

    /**
     * SEARCH  /_search/paiements?query=:query : search for the paiement corresponding
     * to the query.
     *
     * @param query the query of the paiement search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/paiements",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Paiement> searchPaiements(@RequestParam String query) {
        log.debug("REST request to search Paiements for query {}", query);
        return StreamSupport
            .stream(paiementSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
