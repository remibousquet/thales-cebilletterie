package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Enfant;
import fr.cethalesbdx.billetterie.repository.EnfantRepository;
import fr.cethalesbdx.billetterie.repository.search.EnfantSearchRepository;

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
 * Test class for the EnfantResource REST controller.
 *
 * @see EnfantResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class EnfantResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_PRENOM = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE_NAISSANCE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_NAISSANCE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_NAISSANCE_STR = dateTimeFormatter.format(DEFAULT_DATE_NAISSANCE);

    @Inject
    private EnfantRepository enfantRepository;

    @Inject
    private EnfantSearchRepository enfantSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnfantMockMvc;

    private Enfant enfant;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnfantResource enfantResource = new EnfantResource();
        ReflectionTestUtils.setField(enfantResource, "enfantSearchRepository", enfantSearchRepository);
        ReflectionTestUtils.setField(enfantResource, "enfantRepository", enfantRepository);
        this.restEnfantMockMvc = MockMvcBuilders.standaloneSetup(enfantResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enfantSearchRepository.deleteAll();
        enfant = new Enfant();
        enfant.setNom(DEFAULT_NOM);
        enfant.setPrenom(DEFAULT_PRENOM);
        enfant.setDateNaissance(DEFAULT_DATE_NAISSANCE);
    }

    @Test
    @Transactional
    public void createEnfant() throws Exception {
        int databaseSizeBeforeCreate = enfantRepository.findAll().size();

        // Create the Enfant

        restEnfantMockMvc.perform(post("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isCreated());

        // Validate the Enfant in the database
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeCreate + 1);
        Enfant testEnfant = enfants.get(enfants.size() - 1);
        assertThat(testEnfant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEnfant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(DEFAULT_DATE_NAISSANCE);

        // Validate the Enfant in ElasticSearch
        Enfant enfantEs = enfantSearchRepository.findOne(testEnfant.getId());
        assertThat(enfantEs).isEqualToComparingFieldByField(testEnfant);
    }

    @Test
    @Transactional
    public void checkNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfantRepository.findAll().size();
        // set the field null
        enfant.setNom(null);

        // Create the Enfant, which fails.

        restEnfantMockMvc.perform(post("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isBadRequest());

        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrenomIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfantRepository.findAll().size();
        // set the field null
        enfant.setPrenom(null);

        // Create the Enfant, which fails.

        restEnfantMockMvc.perform(post("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isBadRequest());

        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateNaissanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = enfantRepository.findAll().size();
        // set the field null
        enfant.setDateNaissance(null);

        // Create the Enfant, which fails.

        restEnfantMockMvc.perform(post("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(enfant)))
                .andExpect(status().isBadRequest());

        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnfants() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get all the enfants
        restEnfantMockMvc.perform(get("/api/enfants?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(enfant.getId().intValue())))
                .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
                .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
                .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE_STR)));
    }

    @Test
    @Transactional
    public void getEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);

        // Get the enfant
        restEnfantMockMvc.perform(get("/api/enfants/{id}", enfant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(enfant.getId().intValue()))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM.toString()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM.toString()))
            .andExpect(jsonPath("$.dateNaissance").value(DEFAULT_DATE_NAISSANCE_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEnfant() throws Exception {
        // Get the enfant
        restEnfantMockMvc.perform(get("/api/enfants/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
        enfantSearchRepository.save(enfant);
        int databaseSizeBeforeUpdate = enfantRepository.findAll().size();

        // Update the enfant
        Enfant updatedEnfant = new Enfant();
        updatedEnfant.setId(enfant.getId());
        updatedEnfant.setNom(UPDATED_NOM);
        updatedEnfant.setPrenom(UPDATED_PRENOM);
        updatedEnfant.setDateNaissance(UPDATED_DATE_NAISSANCE);

        restEnfantMockMvc.perform(put("/api/enfants")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEnfant)))
                .andExpect(status().isOk());

        // Validate the Enfant in the database
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeUpdate);
        Enfant testEnfant = enfants.get(enfants.size() - 1);
        assertThat(testEnfant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEnfant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEnfant.getDateNaissance()).isEqualTo(UPDATED_DATE_NAISSANCE);

        // Validate the Enfant in ElasticSearch
        Enfant enfantEs = enfantSearchRepository.findOne(testEnfant.getId());
        assertThat(enfantEs).isEqualToComparingFieldByField(testEnfant);
    }

    @Test
    @Transactional
    public void deleteEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
        enfantSearchRepository.save(enfant);
        int databaseSizeBeforeDelete = enfantRepository.findAll().size();

        // Get the enfant
        restEnfantMockMvc.perform(delete("/api/enfants/{id}", enfant.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enfantExistsInEs = enfantSearchRepository.exists(enfant.getId());
        assertThat(enfantExistsInEs).isFalse();

        // Validate the database is empty
        List<Enfant> enfants = enfantRepository.findAll();
        assertThat(enfants).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEnfant() throws Exception {
        // Initialize the database
        enfantRepository.saveAndFlush(enfant);
        enfantSearchRepository.save(enfant);

        // Search the enfant
        restEnfantMockMvc.perform(get("/api/_search/enfants?query=id:" + enfant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enfant.getId().intValue())))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM.toString())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM.toString())))
            .andExpect(jsonPath("$.[*].dateNaissance").value(hasItem(DEFAULT_DATE_NAISSANCE_STR)));
    }
}
