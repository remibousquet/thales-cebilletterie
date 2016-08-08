package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Billet;
import fr.cethalesbdx.billetterie.repository.BilletRepository;
import fr.cethalesbdx.billetterie.repository.search.BilletSearchRepository;

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
 * Test class for the BilletResource REST controller.
 *
 * @see BilletResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class BilletResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final Boolean DEFAULT_TYPE_DATE = false;
    private static final Boolean UPDATED_TYPE_DATE = true;

    private static final ZonedDateTime DEFAULT_DATE_DEBUT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_DEBUT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_DEBUT_STR = dateTimeFormatter.format(DEFAULT_DATE_DEBUT);

    private static final ZonedDateTime DEFAULT_DATE_FIN = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE_FIN = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_FIN_STR = dateTimeFormatter.format(DEFAULT_DATE_FIN);
    private static final String DEFAULT_HORAIRE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_HORAIRE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_LIEU = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIEU = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_ZONE_SALLE = "AAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_ZONE_SALLE = "BBBBBBBBBBBBBBBBBBBB";
    private static final String DEFAULT_TYPE_PUBLIC = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_TYPE_PUBLIC = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRIX_UNITAIRE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRIX_UNITAIRE = new BigDecimal(2);

    private static final Integer DEFAULT_QUANTITE_STOCK = 1;
    private static final Integer UPDATED_QUANTITE_STOCK = 2;
    private static final String DEFAULT_COMMENTAIRE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_COMMENTAIRE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private BilletRepository billetRepository;

    @Inject
    private BilletSearchRepository billetSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBilletMockMvc;

    private Billet billet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BilletResource billetResource = new BilletResource();
        ReflectionTestUtils.setField(billetResource, "billetSearchRepository", billetSearchRepository);
        ReflectionTestUtils.setField(billetResource, "billetRepository", billetRepository);
        this.restBilletMockMvc = MockMvcBuilders.standaloneSetup(billetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        billetSearchRepository.deleteAll();
        billet = new Billet();
        billet.setTitre(DEFAULT_TITRE);
        billet.setTypeDate(DEFAULT_TYPE_DATE);
        billet.setDateDebut(DEFAULT_DATE_DEBUT);
        billet.setDateFin(DEFAULT_DATE_FIN);
        billet.setHoraire(DEFAULT_HORAIRE);
        billet.setLieu(DEFAULT_LIEU);
        billet.setZoneSalle(DEFAULT_ZONE_SALLE);
        billet.setTypePublic(DEFAULT_TYPE_PUBLIC);
        billet.setPrixUnitaire(DEFAULT_PRIX_UNITAIRE);
        billet.setQuantiteStock(DEFAULT_QUANTITE_STOCK);
        billet.setCommentaire(DEFAULT_COMMENTAIRE);
    }

    @Test
    @Transactional
    public void createBillet() throws Exception {
        int databaseSizeBeforeCreate = billetRepository.findAll().size();

        // Create the Billet

        restBilletMockMvc.perform(post("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billet)))
                .andExpect(status().isCreated());

        // Validate the Billet in the database
        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeCreate + 1);
        Billet testBillet = billets.get(billets.size() - 1);
        assertThat(testBillet.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testBillet.isTypeDate()).isEqualTo(DEFAULT_TYPE_DATE);
        assertThat(testBillet.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testBillet.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testBillet.getHoraire()).isEqualTo(DEFAULT_HORAIRE);
        assertThat(testBillet.getLieu()).isEqualTo(DEFAULT_LIEU);
        assertThat(testBillet.getZoneSalle()).isEqualTo(DEFAULT_ZONE_SALLE);
        assertThat(testBillet.getTypePublic()).isEqualTo(DEFAULT_TYPE_PUBLIC);
        assertThat(testBillet.getPrixUnitaire()).isEqualTo(DEFAULT_PRIX_UNITAIRE);
        assertThat(testBillet.getQuantiteStock()).isEqualTo(DEFAULT_QUANTITE_STOCK);
        assertThat(testBillet.getCommentaire()).isEqualTo(DEFAULT_COMMENTAIRE);

        // Validate the Billet in ElasticSearch
        Billet billetEs = billetSearchRepository.findOne(testBillet.getId());
        assertThat(billetEs).isEqualToComparingFieldByField(testBillet);
    }

    @Test
    @Transactional
    public void checkTitreIsRequired() throws Exception {
        int databaseSizeBeforeTest = billetRepository.findAll().size();
        // set the field null
        billet.setTitre(null);

        // Create the Billet, which fails.

        restBilletMockMvc.perform(post("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billet)))
                .andExpect(status().isBadRequest());

        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = billetRepository.findAll().size();
        // set the field null
        billet.setTypeDate(null);

        // Create the Billet, which fails.

        restBilletMockMvc.perform(post("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billet)))
                .andExpect(status().isBadRequest());

        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = billetRepository.findAll().size();
        // set the field null
        billet.setDateDebut(null);

        // Create the Billet, which fails.

        restBilletMockMvc.perform(post("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billet)))
                .andExpect(status().isBadRequest());

        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPrixUnitaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = billetRepository.findAll().size();
        // set the field null
        billet.setPrixUnitaire(null);

        // Create the Billet, which fails.

        restBilletMockMvc.perform(post("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(billet)))
                .andExpect(status().isBadRequest());

        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillets() throws Exception {
        // Initialize the database
        billetRepository.saveAndFlush(billet);

        // Get all the billets
        restBilletMockMvc.perform(get("/api/billets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(billet.getId().intValue())))
                .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
                .andExpect(jsonPath("$.[*].typeDate").value(hasItem(DEFAULT_TYPE_DATE.booleanValue())))
                .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
                .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN_STR)))
                .andExpect(jsonPath("$.[*].horaire").value(hasItem(DEFAULT_HORAIRE.toString())))
                .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
                .andExpect(jsonPath("$.[*].zoneSalle").value(hasItem(DEFAULT_ZONE_SALLE.toString())))
                .andExpect(jsonPath("$.[*].typePublic").value(hasItem(DEFAULT_TYPE_PUBLIC.toString())))
                .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
                .andExpect(jsonPath("$.[*].quantiteStock").value(hasItem(DEFAULT_QUANTITE_STOCK)))
                .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }

    @Test
    @Transactional
    public void getBillet() throws Exception {
        // Initialize the database
        billetRepository.saveAndFlush(billet);

        // Get the billet
        restBilletMockMvc.perform(get("/api/billets/{id}", billet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(billet.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE.toString()))
            .andExpect(jsonPath("$.typeDate").value(DEFAULT_TYPE_DATE.booleanValue()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT_STR))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN_STR))
            .andExpect(jsonPath("$.horaire").value(DEFAULT_HORAIRE.toString()))
            .andExpect(jsonPath("$.lieu").value(DEFAULT_LIEU.toString()))
            .andExpect(jsonPath("$.zoneSalle").value(DEFAULT_ZONE_SALLE.toString()))
            .andExpect(jsonPath("$.typePublic").value(DEFAULT_TYPE_PUBLIC.toString()))
            .andExpect(jsonPath("$.prixUnitaire").value(DEFAULT_PRIX_UNITAIRE.intValue()))
            .andExpect(jsonPath("$.quantiteStock").value(DEFAULT_QUANTITE_STOCK))
            .andExpect(jsonPath("$.commentaire").value(DEFAULT_COMMENTAIRE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBillet() throws Exception {
        // Get the billet
        restBilletMockMvc.perform(get("/api/billets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillet() throws Exception {
        // Initialize the database
        billetRepository.saveAndFlush(billet);
        billetSearchRepository.save(billet);
        int databaseSizeBeforeUpdate = billetRepository.findAll().size();

        // Update the billet
        Billet updatedBillet = new Billet();
        updatedBillet.setId(billet.getId());
        updatedBillet.setTitre(UPDATED_TITRE);
        updatedBillet.setTypeDate(UPDATED_TYPE_DATE);
        updatedBillet.setDateDebut(UPDATED_DATE_DEBUT);
        updatedBillet.setDateFin(UPDATED_DATE_FIN);
        updatedBillet.setHoraire(UPDATED_HORAIRE);
        updatedBillet.setLieu(UPDATED_LIEU);
        updatedBillet.setZoneSalle(UPDATED_ZONE_SALLE);
        updatedBillet.setTypePublic(UPDATED_TYPE_PUBLIC);
        updatedBillet.setPrixUnitaire(UPDATED_PRIX_UNITAIRE);
        updatedBillet.setQuantiteStock(UPDATED_QUANTITE_STOCK);
        updatedBillet.setCommentaire(UPDATED_COMMENTAIRE);

        restBilletMockMvc.perform(put("/api/billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedBillet)))
                .andExpect(status().isOk());

        // Validate the Billet in the database
        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeUpdate);
        Billet testBillet = billets.get(billets.size() - 1);
        assertThat(testBillet.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testBillet.isTypeDate()).isEqualTo(UPDATED_TYPE_DATE);
        assertThat(testBillet.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testBillet.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testBillet.getHoraire()).isEqualTo(UPDATED_HORAIRE);
        assertThat(testBillet.getLieu()).isEqualTo(UPDATED_LIEU);
        assertThat(testBillet.getZoneSalle()).isEqualTo(UPDATED_ZONE_SALLE);
        assertThat(testBillet.getTypePublic()).isEqualTo(UPDATED_TYPE_PUBLIC);
        assertThat(testBillet.getPrixUnitaire()).isEqualTo(UPDATED_PRIX_UNITAIRE);
        assertThat(testBillet.getQuantiteStock()).isEqualTo(UPDATED_QUANTITE_STOCK);
        assertThat(testBillet.getCommentaire()).isEqualTo(UPDATED_COMMENTAIRE);

        // Validate the Billet in ElasticSearch
        Billet billetEs = billetSearchRepository.findOne(testBillet.getId());
        assertThat(billetEs).isEqualToComparingFieldByField(testBillet);
    }

    @Test
    @Transactional
    public void deleteBillet() throws Exception {
        // Initialize the database
        billetRepository.saveAndFlush(billet);
        billetSearchRepository.save(billet);
        int databaseSizeBeforeDelete = billetRepository.findAll().size();

        // Get the billet
        restBilletMockMvc.perform(delete("/api/billets/{id}", billet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean billetExistsInEs = billetSearchRepository.exists(billet.getId());
        assertThat(billetExistsInEs).isFalse();

        // Validate the database is empty
        List<Billet> billets = billetRepository.findAll();
        assertThat(billets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchBillet() throws Exception {
        // Initialize the database
        billetRepository.saveAndFlush(billet);
        billetSearchRepository.save(billet);

        // Search the billet
        restBilletMockMvc.perform(get("/api/_search/billets?query=id:" + billet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billet.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE.toString())))
            .andExpect(jsonPath("$.[*].typeDate").value(hasItem(DEFAULT_TYPE_DATE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT_STR)))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN_STR)))
            .andExpect(jsonPath("$.[*].horaire").value(hasItem(DEFAULT_HORAIRE.toString())))
            .andExpect(jsonPath("$.[*].lieu").value(hasItem(DEFAULT_LIEU.toString())))
            .andExpect(jsonPath("$.[*].zoneSalle").value(hasItem(DEFAULT_ZONE_SALLE.toString())))
            .andExpect(jsonPath("$.[*].typePublic").value(hasItem(DEFAULT_TYPE_PUBLIC.toString())))
            .andExpect(jsonPath("$.[*].prixUnitaire").value(hasItem(DEFAULT_PRIX_UNITAIRE.intValue())))
            .andExpect(jsonPath("$.[*].quantiteStock").value(hasItem(DEFAULT_QUANTITE_STOCK)))
            .andExpect(jsonPath("$.[*].commentaire").value(hasItem(DEFAULT_COMMENTAIRE.toString())));
    }
}
