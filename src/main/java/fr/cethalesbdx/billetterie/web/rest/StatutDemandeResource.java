package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.StatutDemande;
import fr.cethalesbdx.billetterie.repository.StatutDemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.StatutDemandeSearchRepository;
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
 * REST controller for managing StatutDemande.
 */
@RestController
@RequestMapping("/api")
public class StatutDemandeResource {

    private final Logger log = LoggerFactory.getLogger(StatutDemandeResource.class);
        
    @Inject
    private StatutDemandeRepository statutDemandeRepository;
    
    @Inject
    private StatutDemandeSearchRepository statutDemandeSearchRepository;
    
    /**
     * POST  /statut-demandes : Create a new statutDemande.
     *
     * @param statutDemande the statutDemande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new statutDemande, or with status 400 (Bad Request) if the statutDemande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statut-demandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutDemande> createStatutDemande(@Valid @RequestBody StatutDemande statutDemande) throws URISyntaxException {
        log.debug("REST request to save StatutDemande : {}", statutDemande);
        if (statutDemande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("statutDemande", "idexists", "A new statutDemande cannot already have an ID")).body(null);
        }
        StatutDemande result = statutDemandeRepository.save(statutDemande);
        statutDemandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/statut-demandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("statutDemande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /statut-demandes : Updates an existing statutDemande.
     *
     * @param statutDemande the statutDemande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated statutDemande,
     * or with status 400 (Bad Request) if the statutDemande is not valid,
     * or with status 500 (Internal Server Error) if the statutDemande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/statut-demandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutDemande> updateStatutDemande(@Valid @RequestBody StatutDemande statutDemande) throws URISyntaxException {
        log.debug("REST request to update StatutDemande : {}", statutDemande);
        if (statutDemande.getId() == null) {
            return createStatutDemande(statutDemande);
        }
        StatutDemande result = statutDemandeRepository.save(statutDemande);
        statutDemandeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("statutDemande", statutDemande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /statut-demandes : get all the statutDemandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of statutDemandes in body
     */
    @RequestMapping(value = "/statut-demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StatutDemande> getAllStatutDemandes() {
        log.debug("REST request to get all StatutDemandes");
        List<StatutDemande> statutDemandes = statutDemandeRepository.findAll();
        return statutDemandes;
    }

    /**
     * GET  /statut-demandes/:id : get the "id" statutDemande.
     *
     * @param id the id of the statutDemande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the statutDemande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/statut-demandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StatutDemande> getStatutDemande(@PathVariable Long id) {
        log.debug("REST request to get StatutDemande : {}", id);
        StatutDemande statutDemande = statutDemandeRepository.findOne(id);
        return Optional.ofNullable(statutDemande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /statut-demandes/:id : delete the "id" statutDemande.
     *
     * @param id the id of the statutDemande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/statut-demandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStatutDemande(@PathVariable Long id) {
        log.debug("REST request to delete StatutDemande : {}", id);
        statutDemandeRepository.delete(id);
        statutDemandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("statutDemande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/statut-demandes?query=:query : search for the statutDemande corresponding
     * to the query.
     *
     * @param query the query of the statutDemande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/statut-demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StatutDemande> searchStatutDemandes(@RequestParam String query) {
        log.debug("REST request to search StatutDemandes for query {}", query);
        return StreamSupport
            .stream(statutDemandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
