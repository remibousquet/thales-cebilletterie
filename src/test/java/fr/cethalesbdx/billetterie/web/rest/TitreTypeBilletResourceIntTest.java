package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.TitreTypeBillet;
import fr.cethalesbdx.billetterie.repository.TitreTypeBilletRepository;
import fr.cethalesbdx.billetterie.repository.search.TitreTypeBilletSearchRepository;

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
 * Test class for the TitreTypeBilletResource REST controller.
 *
 * @see TitreTypeBilletResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class TitreTypeBilletResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TitreTypeBilletRepository titreTypeBilletRepository;

    @Inject
    private TitreTypeBilletSearchRepository titreTypeBilletSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTitreTypeBilletMockMvc;

    private TitreTypeBillet titreTypeBillet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TitreTypeBilletResource titreTypeBilletResource = new TitreTypeBilletResource();
        ReflectionTestUtils.setField(titreTypeBilletResource, "titreTypeBilletSearchRepository", titreTypeBilletSearchRepository);
        ReflectionTestUtils.setField(titreTypeBilletResource, "titreTypeBilletRepository", titreTypeBilletRepository);
        this.restTitreTypeBilletMockMvc = MockMvcBuilders.standaloneSetup(titreTypeBilletResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        titreTypeBilletSearchRepository.deleteAll();
        titreTypeBillet = new TitreTypeBillet();
        titreTypeBillet.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTitreTypeBillet() throws Exception {
        int databaseSizeBeforeCreate = titreTypeBilletRepository.findAll().size();

        // Create the TitreTypeBillet

        restTitreTypeBilletMockMvc.perform(post("/api/titre-type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(titreTypeBillet)))
                .andExpect(status().isCreated());

        // Validate the TitreTypeBillet in the database
        List<TitreTypeBillet> titreTypeBillets = titreTypeBilletRepository.findAll();
        assertThat(titreTypeBillets).hasSize(databaseSizeBeforeCreate + 1);
        TitreTypeBillet testTitreTypeBillet = titreTypeBillets.get(titreTypeBillets.size() - 1);
        assertThat(testTitreTypeBillet.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TitreTypeBillet in ElasticSearch
        TitreTypeBillet titreTypeBilletEs = titreTypeBilletSearchRepository.findOne(testTitreTypeBillet.getId());
        assertThat(titreTypeBilletEs).isEqualToComparingFieldByField(testTitreTypeBillet);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = titreTypeBilletRepository.findAll().size();
        // set the field null
        titreTypeBillet.setLibelle(null);

        // Create the TitreTypeBillet, which fails.

        restTitreTypeBilletMockMvc.perform(post("/api/titre-type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(titreTypeBillet)))
                .andExpect(status().isBadRequest());

        List<TitreTypeBillet> titreTypeBillets = titreTypeBilletRepository.findAll();
        assertThat(titreTypeBillets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTitreTypeBillets() throws Exception {
        // Initialize the database
        titreTypeBilletRepository.saveAndFlush(titreTypeBillet);

        // Get all the titreTypeBillets
        restTitreTypeBilletMockMvc.perform(get("/api/titre-type-billets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(titreTypeBillet.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTitreTypeBillet() throws Exception {
        // Initialize the database
        titreTypeBilletRepository.saveAndFlush(titreTypeBillet);

        // Get the titreTypeBillet
        restTitreTypeBilletMockMvc.perform(get("/api/titre-type-billets/{id}", titreTypeBillet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(titreTypeBillet.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTitreTypeBillet() throws Exception {
        // Get the titreTypeBillet
        restTitreTypeBilletMockMvc.perform(get("/api/titre-type-billets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTitreTypeBillet() throws Exception {
        // Initialize the database
        titreTypeBilletRepository.saveAndFlush(titreTypeBillet);
        titreTypeBilletSearchRepository.save(titreTypeBillet);
        int databaseSizeBeforeUpdate = titreTypeBilletRepository.findAll().size();

        // Update the titreTypeBillet
        TitreTypeBillet updatedTitreTypeBillet = new TitreTypeBillet();
        updatedTitreTypeBillet.setId(titreTypeBillet.getId());
        updatedTitreTypeBillet.setLibelle(UPDATED_LIBELLE);

        restTitreTypeBilletMockMvc.perform(put("/api/titre-type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTitreTypeBillet)))
                .andExpect(status().isOk());

        // Validate the TitreTypeBillet in the database
        List<TitreTypeBillet> titreTypeBillets = titreTypeBilletRepository.findAll();
        assertThat(titreTypeBillets).hasSize(databaseSizeBeforeUpdate);
        TitreTypeBillet testTitreTypeBillet = titreTypeBillets.get(titreTypeBillets.size() - 1);
        assertThat(testTitreTypeBillet.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TitreTypeBillet in ElasticSearch
        TitreTypeBillet titreTypeBilletEs = titreTypeBilletSearchRepository.findOne(testTitreTypeBillet.getId());
        assertThat(titreTypeBilletEs).isEqualToComparingFieldByField(testTitreTypeBillet);
    }

    @Test
    @Transactional
    public void deleteTitreTypeBillet() throws Exception {
        // Initialize the database
        titreTypeBilletRepository.saveAndFlush(titreTypeBillet);
        titreTypeBilletSearchRepository.save(titreTypeBillet);
        int databaseSizeBeforeDelete = titreTypeBilletRepository.findAll().size();

        // Get the titreTypeBillet
        restTitreTypeBilletMockMvc.perform(delete("/api/titre-type-billets/{id}", titreTypeBillet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean titreTypeBilletExistsInEs = titreTypeBilletSearchRepository.exists(titreTypeBillet.getId());
        assertThat(titreTypeBilletExistsInEs).isFalse();

        // Validate the database is empty
        List<TitreTypeBillet> titreTypeBillets = titreTypeBilletRepository.findAll();
        assertThat(titreTypeBillets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTitreTypeBillet() throws Exception {
        // Initialize the database
        titreTypeBilletRepository.saveAndFlush(titreTypeBillet);
        titreTypeBilletSearchRepository.save(titreTypeBillet);

        // Search the titreTypeBillet
        restTitreTypeBilletMockMvc.perform(get("/api/_search/titre-type-billets?query=id:" + titreTypeBillet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(titreTypeBillet.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
