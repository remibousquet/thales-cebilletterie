package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Permanence;
import fr.cethalesbdx.billetterie.repository.PermanenceRepository;
import fr.cethalesbdx.billetterie.repository.search.PermanenceSearchRepository;

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
 * Test class for the PermanenceResource REST controller.
 *
 * @see PermanenceResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class PermanenceResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_HORAIRE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_HORAIRE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_MESSAGE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private PermanenceRepository permanenceRepository;

    @Inject
    private PermanenceSearchRepository permanenceSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPermanenceMockMvc;

    private Permanence permanence;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermanenceResource permanenceResource = new PermanenceResource();
        ReflectionTestUtils.setField(permanenceResource, "permanenceSearchRepository", permanenceSearchRepository);
        ReflectionTestUtils.setField(permanenceResource, "permanenceRepository", permanenceRepository);
        this.restPermanenceMockMvc = MockMvcBuilders.standaloneSetup(permanenceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        permanenceSearchRepository.deleteAll();
        permanence = new Permanence();
        permanence.setDate(DEFAULT_DATE);
        permanence.setHoraire(DEFAULT_HORAIRE);
        permanence.setMessage(DEFAULT_MESSAGE);
    }

    @Test
    @Transactional
    public void createPermanence() throws Exception {
        int databaseSizeBeforeCreate = permanenceRepository.findAll().size();

        // Create the Permanence

        restPermanenceMockMvc.perform(post("/api/permanences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permanence)))
                .andExpect(status().isCreated());

        // Validate the Permanence in the database
        List<Permanence> permanences = permanenceRepository.findAll();
        assertThat(permanences).hasSize(databaseSizeBeforeCreate + 1);
        Permanence testPermanence = permanences.get(permanences.size() - 1);
        assertThat(testPermanence.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPermanence.getHoraire()).isEqualTo(DEFAULT_HORAIRE);
        assertThat(testPermanence.getMessage()).isEqualTo(DEFAULT_MESSAGE);

        // Validate the Permanence in ElasticSearch
        Permanence permanenceEs = permanenceSearchRepository.findOne(testPermanence.getId());
        assertThat(permanenceEs).isEqualToComparingFieldByField(testPermanence);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = permanenceRepository.findAll().size();
        // set the field null
        permanence.setDate(null);

        // Create the Permanence, which fails.

        restPermanenceMockMvc.perform(post("/api/permanences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permanence)))
                .andExpect(status().isBadRequest());

        List<Permanence> permanences = permanenceRepository.findAll();
        assertThat(permanences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHoraireIsRequired() throws Exception {
        int databaseSizeBeforeTest = permanenceRepository.findAll().size();
        // set the field null
        permanence.setHoraire(null);

        // Create the Permanence, which fails.

        restPermanenceMockMvc.perform(post("/api/permanences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permanence)))
                .andExpect(status().isBadRequest());

        List<Permanence> permanences = permanenceRepository.findAll();
        assertThat(permanences).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPermanences() throws Exception {
        // Initialize the database
        permanenceRepository.saveAndFlush(permanence);

        // Get all the permanences
        restPermanenceMockMvc.perform(get("/api/permanences?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(permanence.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].horaire").value(hasItem(DEFAULT_HORAIRE.toString())))
                .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }

    @Test
    @Transactional
    public void getPermanence() throws Exception {
        // Initialize the database
        permanenceRepository.saveAndFlush(permanence);

        // Get the permanence
        restPermanenceMockMvc.perform(get("/api/permanences/{id}", permanence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(permanence.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.horaire").value(DEFAULT_HORAIRE.toString()))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPermanence() throws Exception {
        // Get the permanence
        restPermanenceMockMvc.perform(get("/api/permanences/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermanence() throws Exception {
        // Initialize the database
        permanenceRepository.saveAndFlush(permanence);
        permanenceSearchRepository.save(permanence);
        int databaseSizeBeforeUpdate = permanenceRepository.findAll().size();

        // Update the permanence
        Permanence updatedPermanence = new Permanence();
        updatedPermanence.setId(permanence.getId());
        updatedPermanence.setDate(UPDATED_DATE);
        updatedPermanence.setHoraire(UPDATED_HORAIRE);
        updatedPermanence.setMessage(UPDATED_MESSAGE);

        restPermanenceMockMvc.perform(put("/api/permanences")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPermanence)))
                .andExpect(status().isOk());

        // Validate the Permanence in the database
        List<Permanence> permanences = permanenceRepository.findAll();
        assertThat(permanences).hasSize(databaseSizeBeforeUpdate);
        Permanence testPermanence = permanences.get(permanences.size() - 1);
        assertThat(testPermanence.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPermanence.getHoraire()).isEqualTo(UPDATED_HORAIRE);
        assertThat(testPermanence.getMessage()).isEqualTo(UPDATED_MESSAGE);

        // Validate the Permanence in ElasticSearch
        Permanence permanenceEs = permanenceSearchRepository.findOne(testPermanence.getId());
        assertThat(permanenceEs).isEqualToComparingFieldByField(testPermanence);
    }

    @Test
    @Transactional
    public void deletePermanence() throws Exception {
        // Initialize the database
        permanenceRepository.saveAndFlush(permanence);
        permanenceSearchRepository.save(permanence);
        int databaseSizeBeforeDelete = permanenceRepository.findAll().size();

        // Get the permanence
        restPermanenceMockMvc.perform(delete("/api/permanences/{id}", permanence.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean permanenceExistsInEs = permanenceSearchRepository.exists(permanence.getId());
        assertThat(permanenceExistsInEs).isFalse();

        // Validate the database is empty
        List<Permanence> permanences = permanenceRepository.findAll();
        assertThat(permanences).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPermanence() throws Exception {
        // Initialize the database
        permanenceRepository.saveAndFlush(permanence);
        permanenceSearchRepository.save(permanence);

        // Search the permanence
        restPermanenceMockMvc.perform(get("/api/_search/permanences?query=id:" + permanence.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permanence.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].horaire").value(hasItem(DEFAULT_HORAIRE.toString())))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE.toString())));
    }
}
