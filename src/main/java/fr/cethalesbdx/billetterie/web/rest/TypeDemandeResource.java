package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.TypeDemande;
import fr.cethalesbdx.billetterie.repository.TypeDemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.TypeDemandeSearchRepository;
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
 * REST controller for managing TypeDemande.
 */
@RestController
@RequestMapping("/api")
public class TypeDemandeResource {

    private final Logger log = LoggerFactory.getLogger(TypeDemandeResource.class);
        
    @Inject
    private TypeDemandeRepository typeDemandeRepository;
    
    @Inject
    private TypeDemandeSearchRepository typeDemandeSearchRepository;
    
    /**
     * POST  /type-demandes : Create a new typeDemande.
     *
     * @param typeDemande the typeDemande to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeDemande, or with status 400 (Bad Request) if the typeDemande has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-demandes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeDemande> createTypeDemande(@Valid @RequestBody TypeDemande typeDemande) throws URISyntaxException {
        log.debug("REST request to save TypeDemande : {}", typeDemande);
        if (typeDemande.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeDemande", "idexists", "A new typeDemande cannot already have an ID")).body(null);
        }
        TypeDemande result = typeDemandeRepository.save(typeDemande);
        typeDemandeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-demandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeDemande", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-demandes : Updates an existing typeDemande.
     *
     * @param typeDemande the typeDemande to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeDemande,
     * or with status 400 (Bad Request) if the typeDemande is not valid,
     * or with status 500 (Internal Server Error) if the typeDemande couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-demandes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeDemande> updateTypeDemande(@Valid @RequestBody TypeDemande typeDemande) throws URISyntaxException {
        log.debug("REST request to update TypeDemande : {}", typeDemande);
        if (typeDemande.getId() == null) {
            return createTypeDemande(typeDemande);
        }
        TypeDemande result = typeDemandeRepository.save(typeDemande);
        typeDemandeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeDemande", typeDemande.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-demandes : get all the typeDemandes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeDemandes in body
     */
    @RequestMapping(value = "/type-demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeDemande> getAllTypeDemandes() {
        log.debug("REST request to get all TypeDemandes");
        List<TypeDemande> typeDemandes = typeDemandeRepository.findAll();
        return typeDemandes;
    }

    /**
     * GET  /type-demandes/:id : get the "id" typeDemande.
     *
     * @param id the id of the typeDemande to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeDemande, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-demandes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeDemande> getTypeDemande(@PathVariable Long id) {
        log.debug("REST request to get TypeDemande : {}", id);
        TypeDemande typeDemande = typeDemandeRepository.findOne(id);
        return Optional.ofNullable(typeDemande)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-demandes/:id : delete the "id" typeDemande.
     *
     * @param id the id of the typeDemande to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-demandes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeDemande(@PathVariable Long id) {
        log.debug("REST request to delete TypeDemande : {}", id);
        typeDemandeRepository.delete(id);
        typeDemandeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeDemande", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-demandes?query=:query : search for the typeDemande corresponding
     * to the query.
     *
     * @param query the query of the typeDemande search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-demandes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeDemande> searchTypeDemandes(@RequestParam String query) {
        log.debug("REST request to search TypeDemandes for query {}", query);
        return StreamSupport
            .stream(typeDemandeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
