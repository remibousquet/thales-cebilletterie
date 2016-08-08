package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Paiement;
import fr.cethalesbdx.billetterie.repository.PaiementRepository;
import fr.cethalesbdx.billetterie.repository.search.PaiementSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the PaiementResource REST controller.
 *
 * @see PaiementResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class PaiementResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);
    private static final String DEFAULT_BANQUE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_BANQUE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_NUMERO_CHEQUE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NUMERO_CHEQUE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final BigDecimal DEFAULT_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MONTANT = new BigDecimal(2);
    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private PaiementRepository paiementRepository;

    @Inject
    private PaiementSearchRepository paiementSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPaiementMockMvc;

    private Paiement paiement;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PaiementResource paiementResource = new PaiementResource();
        ReflectionTestUtils.setField(paiementResource, "paiementSearchRepository", paiementSearchRepository);
        ReflectionTestUtils.setField(paiementResource, "paiementRepository", paiementRepository);
        this.restPaiementMockMvc = MockMvcBuilders.standaloneSetup(paiementResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        paiementSearchRepository.deleteAll();
        paiement = new Paiement();
        paiement.setDate(DEFAULT_DATE);
        paiement.setBanque(DEFAULT_BANQUE);
        paiement.setNumeroCheque(DEFAULT_NUMERO_CHEQUE);
        paiement.setMontant(DEFAULT_MONTANT);
        paiement.setCommentaire(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void createPaiement() throws Exception {
        int databaseSizeBeforeCreate = paiementRepository.findAll().size();

        // Create the Paiement

        restPaiementMockMvc.perform(post("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiement)))
                .andExpect(status().isCreated());

        // Validate the Paiement in the database
        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeCreate + 1);
        Paiement testPaiement = paiements.get(paiements.size() - 1);
        assertThat(testPaiement.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testPaiement.getBanque()).isEqualTo(DEFAULT_BANQUE);
        assertThat(testPaiement.getNumeroCheque()).isEqualTo(DEFAULT_NUMERO_CHEQUE);
        assertThat(testPaiement.getMontant()).isEqualTo(DEFAULT_MONTANT);
        assertThat(testPaiement.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the Paiement in ElasticSearch
        Paiement paiementEs = paiementSearchRepository.findOne(testPaiement.getId());
        assertThat(paiementEs).isEqualToComparingFieldByField(testPaiement);
    }

    @Test
    @Transactional
    public void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setDate(null);

        // Create the Paiement, which fails.

        restPaiementMockMvc.perform(post("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiement)))
                .andExpect(status().isBadRequest());

        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkBanqueIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setBanque(null);

        // Create the Paiement, which fails.

        restPaiementMockMvc.perform(post("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiement)))
                .andExpect(status().isBadRequest());

        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNumeroChequeIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setNumeroCheque(null);

        // Create the Paiement, which fails.

        restPaiementMockMvc.perform(post("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiement)))
                .andExpect(status().isBadRequest());

        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = paiementRepository.findAll().size();
        // set the field null
        paiement.setMontant(null);

        // Create the Paiement, which fails.

        restPaiementMockMvc.perform(post("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(paiement)))
                .andExpect(status().isBadRequest());

        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPaiements() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get all the paiements
        restPaiementMockMvc.perform(get("/api/paiements?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE.toString())))
                .andExpect(jsonPath("$.[*].numeroCheque").value(hasItem(DEFAULT_NUMERO_CHEQUE.toString())))
                .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
                .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);

        // Get the paiement
        restPaiementMockMvc.perform(get("/api/paiements/{id}", paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(paiement.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.banque").value(DEFAULT_BANQUE.toString()))
            .andExpect(jsonPath("$.numeroCheque").value(DEFAULT_NUMERO_CHEQUE.toString()))
            .andExpect(jsonPath("$.montant").value(DEFAULT_MONTANT.intValue()))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPaiement() throws Exception {
        // Get the paiement
        restPaiementMockMvc.perform(get("/api/paiements/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);
        paiementSearchRepository.save(paiement);
        int databaseSizeBeforeUpdate = paiementRepository.findAll().size();

        // Update the paiement
        Paiement updatedPaiement = new Paiement();
        updatedPaiement.setId(paiement.getId());
        updatedPaiement.setDate(UPDATED_DATE);
        updatedPaiement.setBanque(UPDATED_BANQUE);
        updatedPaiement.setNumeroCheque(UPDATED_NUMERO_CHEQUE);
        updatedPaiement.setMontant(UPDATED_MONTANT);
        updatedPaiement.setCommentaire(UPDATED_COMMENTAIRE);

        restPaiementMockMvc.perform(put("/api/paiements")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPaiement)))
                .andExpect(status().isOk());

        // Validate the Paiement in the database
        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeUpdate);
        Paiement testPaiement = paiements.get(paiements.size() - 1);
        assertThat(testPaiement.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testPaiement.getBanque()).isEqualTo(UPDATED_BANQUE);
        assertThat(testPaiement.getNumeroCheque()).isEqualTo(UPDATED_NUMERO_CHEQUE);
        assertThat(testPaiement.getMontant()).isEqualTo(UPDATED_MONTANT);
        assertThat(testPaiement.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the Paiement in ElasticSearch
        Paiement paiementEs = paiementSearchRepository.findOne(testPaiement.getId());
        assertThat(paiementEs).isEqualToComparingFieldByField(testPaiement);
    }

    @Test
    @Transactional
    public void deletePaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);
        paiementSearchRepository.save(paiement);
        int databaseSizeBeforeDelete = paiementRepository.findAll().size();

        // Get the paiement
        restPaiementMockMvc.perform(delete("/api/paiements/{id}", paiement.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean paiementExistsInEs = paiementSearchRepository.exists(paiement.getId());
        assertThat(paiementExistsInEs).isFalse();

        // Validate the database is empty
        List<Paiement> paiements = paiementRepository.findAll();
        assertThat(paiements).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPaiement() throws Exception {
        // Initialize the database
        paiementRepository.saveAndFlush(paiement);
        paiementSearchRepository.save(paiement);

        // Search the paiement
        restPaiementMockMvc.perform(get("/api/_search/paiements?query=id:" + paiement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(paiement.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
            .andExpect(jsonPath("$.[*].banque").value(hasItem(DEFAULT_BANQUE.toString())))
            .andExpect(jsonPath("$.[*].numeroCheque").value(hasItem(DEFAULT_NUMERO_CHEQUE.toString())))
            .andExpect(jsonPath("$.[*].montant").value(hasItem(DEFAULT_MONTANT.intValue())))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }
}
