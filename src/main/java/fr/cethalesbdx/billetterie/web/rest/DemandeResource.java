package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.Demande;
import fr.cethalesbdx.billetterie.repository.DemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.DemandeSearchRepository;
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
 * REST controller for managing Demande.
 */
@RestController
@RequestMapping("/api")
public class DemandeResource {

    private final Logger log = LoggerFactory.getLogger(DemandeResource.class);
        
    @Inject
    private DemandeRepository demandeRepository;
    
    @Inject
    private DemandeSearchRepository demandeSearchRepository;
    
    /**
     * POST  /demandes : Create a new demande.
     *
     * @param demande the demande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new demande, or with status 400 (Bad Request) if the demande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> createDemande(@Valid @RequestBody Demande demande) throws URISyntaxException {
        log.debug("REST request to save Demande : {}", demande);
        if (demande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("demande", "idexists", "A new demande cannot already have an ID")).body(null);
        }
        Demande result = demandeRepository.save(demande);
        demandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/demandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("demande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /demandes : Updates an existing demande.
     *
     * @param demande the demande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated demande,
     * or with status 400 (Bad Request) if the demande is not valid,
     * or with status 500 (Internal Server Error) if the demande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> updateDemande(@Valid @RequestBody Demande demande) throws URISyntaxException {
        log.debug("REST request to update Demande : {}", demande);
        if (demande.getId() == null) {
            return createDemande(demande);
        }
        Demande result = demandeRepository.save(demande);
        demandeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("demande", demande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /demandes : get all the demandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of demandes in body
     */
    @RequestMapping(value = "/demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Demande> getAllDemandes() {
        log.debug("REST request to get all Demandes");
        List<Demande> demandes = demandeRepository.findAll();
        return demandes;
    }

    /**
     * GET  /demandes/:id : get the "id" demande.
     *
     * @param id the id of the demande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the demande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/demandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Demande> getDemande(@PathVariable Long id) {
        log.debug("REST request to get Demande : {}", id);
        Demande demande = demandeRepository.findOne(id);
        return Optional.ofNullable(demande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /demandes/:id : delete the "id" demande.
     *
     * @param id the id of the demande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/demandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteDemande(@PathVariable Long id) {
        log.debug("REST request to delete Demande : {}", id);
        demandeRepository.delete(id);
        demandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("demande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/demandes?query=:query : search for the demande corresponding
     * to the query.
     *
     * @param query the query of the demande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Demande> searchDemandes(@RequestParam String query) {
        log.debug("REST request to search Demandes for query {}", query);
        return StreamSupport
            .stream(demandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
