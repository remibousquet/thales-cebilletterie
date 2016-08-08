package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.Subvention;
import fr.cethalesbdx.billetterie.repository.SubventionRepository;
import fr.cethalesbdx.billetterie.repository.search.SubventionSearchRepository;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SubventionResource REST controller.
 *
 * @see SubventionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class SubventionResourceIntTest {


    private static final Integer DEFAULT_SUBVENTION_ANNEE = 1;
    private static final Integer UPDATED_SUBVENTION_ANNEE = 2;

    private static final BigDecimal DEFAULT_SUBVENTION_MONTANT = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBVENTION_MONTANT = new BigDecimal(2);

    @Inject
    private SubventionRepository subventionRepository;

    @Inject
    private SubventionSearchRepository subventionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSubventionMockMvc;

    private Subvention subvention;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubventionResource subventionResource = new SubventionResource();
        ReflectionTestUtils.setField(subventionResource, "subventionSearchRepository", subventionSearchRepository);
        ReflectionTestUtils.setField(subventionResource, "subventionRepository", subventionRepository);
        this.restSubventionMockMvc = MockMvcBuilders.standaloneSetup(subventionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        subventionSearchRepository.deleteAll();
        subvention = new Subvention();
        subvention.setSubventionAnnee(DEFAULT_SUBVENTION_ANNEE);
        subvention.setSubventionMontant(DEFAULT_SUBVENTION_MONTANT);
    }

    @Test
    @Transactional
    public void createSubvention() throws Exception {
        int databaseSizeBeforeCreate = subventionRepository.findAll().size();

        // Create the Subvention

        restSubventionMockMvc.perform(post("/api/subventions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subvention)))
                .andExpect(status().isCreated());

        // Validate the Subvention in the database
        List<Subvention> subventions = subventionRepository.findAll();
        assertThat(subventions).hasSize(databaseSizeBeforeCreate + 1);
        Subvention testSubvention = subventions.get(subventions.size() - 1);
        assertThat(testSubvention.getSubventionAnnee()).isEqualTo(DEFAULT_SUBVENTION_ANNEE);
        assertThat(testSubvention.getSubventionMontant()).isEqualTo(DEFAULT_SUBVENTION_MONTANT);

        // Validate the Subvention in ElasticSearch
        Subvention subventionEs = subventionSearchRepository.findOne(testSubvention.getId());
        assertThat(subventionEs).isEqualToComparingFieldByField(testSubvention);
    }

    @Test
    @Transactional
    public void checkSubventionAnneeIsRequired() throws Exception {
        int databaseSizeBeforeTest = subventionRepository.findAll().size();
        // set the field null
        subvention.setSubventionAnnee(null);

        // Create the Subvention, which fails.

        restSubventionMockMvc.perform(post("/api/subventions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subvention)))
                .andExpect(status().isBadRequest());

        List<Subvention> subventions = subventionRepository.findAll();
        assertThat(subventions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSubventionMontantIsRequired() throws Exception {
        int databaseSizeBeforeTest = subventionRepository.findAll().size();
        // set the field null
        subvention.setSubventionMontant(null);

        // Create the Subvention, which fails.

        restSubventionMockMvc.perform(post("/api/subventions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subvention)))
                .andExpect(status().isBadRequest());

        List<Subvention> subventions = subventionRepository.findAll();
        assertThat(subventions).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSubventions() throws Exception {
        // Initialize the database
        subventionRepository.saveAndFlush(subvention);

        // Get all the subventions
        restSubventionMockMvc.perform(get("/api/subventions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subvention.getId().intValue())))
                .andExpect(jsonPath("$.[*].subventionAnnee").value(hasItem(DEFAULT_SUBVENTION_ANNEE)))
                .andExpect(jsonPath("$.[*].subventionMontant").value(hasItem(DEFAULT_SUBVENTION_MONTANT.intValue())));
    }

    @Test
    @Transactional
    public void getSubvention() throws Exception {
        // Initialize the database
        subventionRepository.saveAndFlush(subvention);

        // Get the subvention
        restSubventionMockMvc.perform(get("/api/subventions/{id}", subvention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(subvention.getId().intValue()))
            .andExpect(jsonPath("$.subventionAnnee").value(DEFAULT_SUBVENTION_ANNEE))
            .andExpect(jsonPath("$.subventionMontant").value(DEFAULT_SUBVENTION_MONTANT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubvention() throws Exception {
        // Get the subvention
        restSubventionMockMvc.perform(get("/api/subventions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubvention() throws Exception {
        // Initialize the database
        subventionRepository.saveAndFlush(subvention);
        subventionSearchRepository.save(subvention);
        int databaseSizeBeforeUpdate = subventionRepository.findAll().size();

        // Update the subvention
        Subvention updatedSubvention = new Subvention();
        updatedSubvention.setId(subvention.getId());
        updatedSubvention.setSubventionAnnee(UPDATED_SUBVENTION_ANNEE);
        updatedSubvention.setSubventionMontant(UPDATED_SUBVENTION_MONTANT);

        restSubventionMockMvc.perform(put("/api/subventions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedSubvention)))
                .andExpect(status().isOk());

        // Validate the Subvention in the database
        List<Subvention> subventions = subventionRepository.findAll();
        assertThat(subventions).hasSize(databaseSizeBeforeUpdate);
        Subvention testSubvention = subventions.get(subventions.size() - 1);
        assertThat(testSubvention.getSubventionAnnee()).isEqualTo(UPDATED_SUBVENTION_ANNEE);
        assertThat(testSubvention.getSubventionMontant()).isEqualTo(UPDATED_SUBVENTION_MONTANT);

        // Validate the Subvention in ElasticSearch
        Subvention subventionEs = subventionSearchRepository.findOne(testSubvention.getId());
        assertThat(subventionEs).isEqualToComparingFieldByField(testSubvention);
    }

    @Test
    @Transactional
    public void deleteSubvention() throws Exception {
        // Initialize the database
        subventionRepository.saveAndFlush(subvention);
        subventionSearchRepository.save(subvention);
        int databaseSizeBeforeDelete = subventionRepository.findAll().size();

        // Get the subvention
        restSubventionMockMvc.perform(delete("/api/subventions/{id}", subvention.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean subventionExistsInEs = subventionSearchRepository.exists(subvention.getId());
        assertThat(subventionExistsInEs).isFalse();

        // Validate the database is empty
        List<Subvention> subventions = subventionRepository.findAll();
        assertThat(subventions).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSubvention() throws Exception {
        // Initialize the database
        subventionRepository.saveAndFlush(subvention);
        subventionSearchRepository.save(subvention);

        // Search the subvention
        restSubventionMockMvc.perform(get("/api/_search/subventions?query=id:" + subvention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(subvention.getId().intValue())))
            .andExpect(jsonPath("$.[*].subventionAnnee").value(hasItem(DEFAULT_SUBVENTION_ANNEE)))
            .andExpect(jsonPath("$.[*].subventionMontant").value(hasItem(DEFAULT_SUBVENTION_MONTANT.intValue())));
    }
}
