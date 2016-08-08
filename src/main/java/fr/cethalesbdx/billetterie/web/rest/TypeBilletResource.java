package fr.cethalesbdx.billetterie.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.cethalesbdx.billetterie.domain.TypeBillet;
import fr.cethalesbdx.billetterie.repository.TypeBilletRepository;
import fr.cethalesbdx.billetterie.repository.search.TypeBilletSearchRepository;
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
 * REST controller for managing TypeBillet.
 */
@RestController
@RequestMapping("/api")
public class TypeBilletResource {

    private final Logger log = LoggerFactory.getLogger(TypeBilletResource.class);
        
    @Inject
    private TypeBilletRepository typeBilletRepository;
    
    @Inject
    private TypeBilletSearchRepository typeBilletSearchRepository;
    
    /**
     * POST  /type-billets : Create a new typeBillet.
     *
     * @param typeBillet the typeBillet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new typeBillet, or with status 400 (Bad Request) if the typeBillet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-billets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeBillet> createTypeBillet(@Valid @RequestBody TypeBillet typeBillet) throws URISyntaxException {
        log.debug("REST request to save TypeBillet : {}", typeBillet);
        if (typeBillet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("typeBillet", "idexists", "A new typeBillet cannot already have an ID")).body(null);
        }
        TypeBillet result = typeBilletRepository.save(typeBillet);
        typeBilletSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/type-billets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("typeBillet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /type-billets : Updates an existing typeBillet.
     *
     * @param typeBillet the typeBillet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated typeBillet,
     * or with status 400 (Bad Request) if the typeBillet is not valid,
     * or with status 500 (Internal Server Error) if the typeBillet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/type-billets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeBillet> updateTypeBillet(@Valid @RequestBody TypeBillet typeBillet) throws URISyntaxException {
        log.debug("REST request to update TypeBillet : {}", typeBillet);
        if (typeBillet.getId() == null) {
            return createTypeBillet(typeBillet);
        }
        TypeBillet result = typeBilletRepository.save(typeBillet);
        typeBilletSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("typeBillet", typeBillet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /type-billets : get all the typeBillets.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of typeBillets in body
     */
    @RequestMapping(value = "/type-billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeBillet> getAllTypeBillets() {
        log.debug("REST request to get all TypeBillets");
        List<TypeBillet> typeBillets = typeBilletRepository.findAll();
        return typeBillets;
    }

    /**
     * GET  /type-billets/:id : get the "id" typeBillet.
     *
     * @param id the id of the typeBillet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the typeBillet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/type-billets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TypeBillet> getTypeBillet(@PathVariable Long id) {
        log.debug("REST request to get TypeBillet : {}", id);
        TypeBillet typeBillet = typeBilletRepository.findOne(id);
        return Optional.ofNullable(typeBillet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /type-billets/:id : delete the "id" typeBillet.
     *
     * @param id the id of the typeBillet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/type-billets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTypeBillet(@PathVariable Long id) {
        log.debug("REST request to delete TypeBillet : {}", id);
        typeBilletRepository.delete(id);
        typeBilletSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("typeBillet", id.toString())).build();
    }

    /**
     * SEARCH  /_search/type-billets?query=:query : search for the typeBillet corresponding
     * to the query.
     *
     * @param query the query of the typeBillet search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/type-billets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TypeBillet> searchTypeBillets(@RequestParam String query) {
        log.debug("REST request to search TypeBillets for query {}", query);
        return StreamSupport
            .stream(typeBilletSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
