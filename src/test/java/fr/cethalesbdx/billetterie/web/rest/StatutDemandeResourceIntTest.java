package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.StatutDemande;
import fr.cethalesbdx.billetterie.repository.StatutDemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.StatutDemandeSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StatutDemandeResource REST controller.
 *
 * @see StatutDemandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class StatutDemandeResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    @Inject
    private StatutDemandeRepository statutDemandeRepository;

    @Inject
    private StatutDemandeSearchRepository statutDemandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStatutDemandeMockMvc;

    private StatutDemande statutDemande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StatutDemandeResource statutDemandeResource = new StatutDemandeResource();
        ReflectionTestUtils.setField(statutDemandeResource, "statutDemandeSearchRepository", statutDemandeSearchRepository);
        ReflectionTestUtils.setField(statutDemandeResource, "statutDemandeRepository", statutDemandeRepository);
        this.restStatutDemandeMockMvc = MockMvcBuilders.standaloneSetup(statutDemandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        statutDemandeSearchRepository.deleteAll();
        statutDemande = new StatutDemande();
        statutDemande.setDate(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createStatutDemande() throws Exception {
        int databaseSizeBeforeCreate = statutDemandeRepository.findAll().size();

        // Create the StatutDemande

        restStatutDemandeMockMvc.perform(post("/api/statut-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statutDemande)))
                .andExpect(status().isCreated());

        // Validate the StatutDemande in the database
        List<StatutDemande> statutDemandes = statutDemandeRepository.findAll();
        assertThat(statutDemandes).hasSize(databaseSizeBeforeCreate + 1);
        StatutDemande testStatutDemande = statutDemandes.get(statutDemandes.size() - 1);
        assertThat(testStatutDemande.getDate()).isEqualTo(DEFAULT_DATE);

        // Validate the StatutDemande in ElasticSearch
        StatutDemande statutDemandeEs = statutDemandeSearchRepository.findOne(testStatutDemande.getId());
        assertThat(statutDemandeEs).isEqualToComparingFieldByField(testStatutDemande);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = statutDemandeRepository.findAll().size();
        // set the field null
        statutDemande.setDate(null);

        // Create the StatutDemande, which fails.

        restStatutDemandeMockMvc.perform(post("/api/statut-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(statutDemande)))
                .andExpect(status().isBadRequest());

        List<StatutDemande> statutDemandes = statutDemandeRepository.findAll();
        assertThat(statutDemandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStatutDemandes() throws Exception {
        // Initialize the database
        statutDemandeRepository.saveAndFlush(statutDemande);

        // Get all the statutDemandes
        restStatutDemandeMockMvc.perform(get("/api/statut-demandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(statutDemande.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }

    @Test
    @Transactional
    public void getStatutDemande() throws Exception {
        // Initialize the database
        statutDemandeRepository.saveAndFlush(statutDemande);

        // Get the statutDemande
        restStatutDemandeMockMvc.perform(get("/api/statut-demandes/{id}", statutDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(statutDemande.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingStatutDemande() throws Exception {
        // Get the statutDemande
        restStatutDemandeMockMvc.perform(get("/api/statut-demandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStatutDemande() throws Exception {
        // Initialize the database
        statutDemandeRepository.saveAndFlush(statutDemande);
        statutDemandeSearchRepository.save(statutDemande);
        int databaseSizeBeforeUpdate = statutDemandeRepository.findAll().size();

        // Update the statutDemande
        StatutDemande updatedStatutDemande = new StatutDemande();
        updatedStatutDemande.setId(statutDemande.getId());
        updatedStatutDemande.setDate(UPDATED_DATE);

        restStatutDemandeMockMvc.perform(put("/api/statut-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStatutDemande)))
                .andExpect(status().isOk());

        // Validate the StatutDemande in the database
        List<StatutDemande> statutDemandes = statutDemandeRepository.findAll();
        assertThat(statutDemandes).hasSize(databaseSizeBeforeUpdate);
        StatutDemande testStatutDemande = statutDemandes.get(statutDemandes.size() - 1);
        assertThat(testStatutDemande.getDate()).isEqualTo(UPDATED_DATE);

        // Validate the StatutDemande in ElasticSearch
        StatutDemande statutDemandeEs = statutDemandeSearchRepository.findOne(testStatutDemande.getId());
        assertThat(statutDemandeEs).isEqualToComparingFieldByField(testStatutDemande);
    }

    @Test
    @Transactional
    public void deleteStatutDemande() throws Exception {
        // Initialize the database
        statutDemandeRepository.saveAndFlush(statutDemande);
        statutDemandeSearchRepository.save(statutDemande);
        int databaseSizeBeforeDelete = statutDemandeRepository.findAll().size();

        // Get the statutDemande
        restStatutDemandeMockMvc.perform(delete("/api/statut-demandes/{id}", statutDemande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean statutDemandeExistsInEs = statutDemandeSearchRepository.exists(statutDemande.getId());
        assertThat(statutDemandeExistsInEs).isFalse();

        // Validate the database is empty
        List<StatutDemande> statutDemandes = statutDemandeRepository.findAll();
        assertThat(statutDemandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchStatutDemande() throws Exception {
        // Initialize the database
        statutDemandeRepository.saveAndFlush(statutDemande);
        statutDemandeSearchRepository.save(statutDemande);

        // Search the statutDemande
        restStatutDemandeMockMvc.perform(get("/api/_search/statut-demandes?query=id:" + statutDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(statutDemande.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)));
    }
}
