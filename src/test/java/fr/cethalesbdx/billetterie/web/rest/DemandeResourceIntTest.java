package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Demande;
import fr.cethalesbdx.billetterie.repository.DemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.DemandeSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the DemandeResource REST controller.
 *
 * @see DemandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class DemandeResourceIntTest {

    private static final String DEFAULT_NUMERO_EXTERNE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NUMERO_EXTERNE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITE = 1;
    private static final Integer UPDATED_QUANTITE = 2;
    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private DemandeRepository demandeRepository;

    @Inject
    private DemandeSearchRepository demandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restDemandeMockMvc;

    private Demande demande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        DemandeResource demandeResource = new DemandeResource();
        ReflectionTestUtils.setField(demandeResource, "demandeSearchRepository", demandeSearchRepository);
        ReflectionTestUtils.setField(demandeResource, "demandeRepository", demandeRepository);
        this.restDemandeMockMvc = MockMvcBuilders.standaloneSetup(demandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        demandeSearchRepository.deleteAll();
        demande = new Demande();
        demande.setNumeroExterne(DEFAULT_NUMERO_EXTERNE);
        demande.setQuantite(DEFAULT_QUANTITE);
        demande.setCommentaire(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void createDemande() throws Exception {
        int databaseSizeBeforeCreate = demandeRepository.findAll().size();

        // Create the Demande

        restDemandeMockMvc.perform(post("/api/demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(demande)))
                .andExpect(status().isCreated());

        // Validate the Demande in the database
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeCreate + 1);
        Demande testDemande = demandes.get(demandes.size() - 1);
        assertThat(testDemande.getNumeroExterne()).isEqualTo(DEFAULT_NUMERO_EXTERNE);
        assertThat(testDemande.getQuantite()).isEqualTo(DEFAULT_QUANTITE);
        assertThat(testDemande.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the Demande in ElasticSearch
        Demande demandeEs = demandeSearchRepository.findOne(testDemande.getId());
        assertThat(demandeEs).isEqualToComparingFieldByField(testDemande);
    }

    @Test
    @Transactional
    public void checkQuantiteIsRequired() throws Exception {
        int databaseSizeBeforeTest = demandeRepository.findAll().size();
        // set the field null
        demande.setQuantite(null);

        // Create the Demande, which fails.

        restDemandeMockMvc.perform(post("/api/demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(demande)))
                .andExpect(status().isBadRequest());

        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDemandes() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get all the demandes
        restDemandeMockMvc.perform(get("/api/demandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
                .andExpect(jsonPath("$.[*].numeroExterne").value(hasItem(DEFAULT_NUMERO_EXTERNE.toString())))
                .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
                .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);

        // Get the demande
        restDemandeMockMvc.perform(get("/api/demandes/{id}", demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(demande.getId().intValue()))
            .andExpect(jsonPath("$.numeroExterne").value(DEFAULT_NUMERO_EXTERNE.toString()))
            .andExpect(jsonPath("$.quantite").value(DEFAULT_QUANTITE))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDemande() throws Exception {
        // Get the demande
        restDemandeMockMvc.perform(get("/api/demandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);
        demandeSearchRepository.save(demande);
        int databaseSizeBeforeUpdate = demandeRepository.findAll().size();

        // Update the demande
        Demande updatedDemande = new Demande();
        updatedDemande.setId(demande.getId());
        updatedDemande.setNumeroExterne(UPDATED_NUMERO_EXTERNE);
        updatedDemande.setQuantite(UPDATED_QUANTITE);
        updatedDemande.setCommentaire(UPDATED_COMMENTAIRE);

        restDemandeMockMvc.perform(put("/api/demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedDemande)))
                .andExpect(status().isOk());

        // Validate the Demande in the database
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeUpdate);
        Demande testDemande = demandes.get(demandes.size() - 1);
        assertThat(testDemande.getNumeroExterne()).isEqualTo(UPDATED_NUMERO_EXTERNE);
        assertThat(testDemande.getQuantite()).isEqualTo(UPDATED_QUANTITE);
        assertThat(testDemande.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the Demande in ElasticSearch
        Demande demandeEs = demandeSearchRepository.findOne(testDemande.getId());
        assertThat(demandeEs).isEqualToComparingFieldByField(testDemande);
    }

    @Test
    @Transactional
    public void deleteDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);
        demandeSearchRepository.save(demande);
        int databaseSizeBeforeDelete = demandeRepository.findAll().size();

        // Get the demande
        restDemandeMockMvc.perform(delete("/api/demandes/{id}", demande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean demandeExistsInEs = demandeSearchRepository.exists(demande.getId());
        assertThat(demandeExistsInEs).isFalse();

        // Validate the database is empty
        List<Demande> demandes = demandeRepository.findAll();
        assertThat(demandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDemande() throws Exception {
        // Initialize the database
        demandeRepository.saveAndFlush(demande);
        demandeSearchRepository.save(demande);

        // Search the demande
        restDemandeMockMvc.perform(get("/api/_search/demandes?query=id:" + demande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demande.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroExterne").value(hasItem(DEFAULT_NUMERO_EXTERNE.toString())))
            .andExpect(jsonPath("$.[*].quantite").value(hasItem(DEFAULT_QUANTITE)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }
}
