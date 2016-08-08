package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.TypeBillet;
import fr.cethalesbdx.billetterie.repository.TypeBilletRepository;
import fr.cethalesbdx.billetterie.repository.search.TypeBilletSearchRepository;

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
 * Test class for the TypeBilletResource REST controller.
 *
 * @see TypeBilletResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeBilletResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypeBilletRepository typeBilletRepository;

    @Inject
    private TypeBilletSearchRepository typeBilletSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeBilletMockMvc;

    private TypeBillet typeBillet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeBilletResource typeBilletResource = new TypeBilletResource();
        ReflectionTestUtils.setField(typeBilletResource, "typeBilletSearchRepository", typeBilletSearchRepository);
        ReflectionTestUtils.setField(typeBilletResource, "typeBilletRepository", typeBilletRepository);
        this.restTypeBilletMockMvc = MockMvcBuilders.standaloneSetup(typeBilletResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeBilletSearchRepository.deleteAll();
        typeBillet = new TypeBillet();
        typeBillet.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypeBillet() throws Exception {
        int databaseSizeBeforeCreate = typeBilletRepository.findAll().size();

        // Create the TypeBillet

        restTypeBilletMockMvc.perform(post("/api/type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeBillet)))
                .andExpect(status().isCreated());

        // Validate the TypeBillet in the database
        List<TypeBillet> typeBillets = typeBilletRepository.findAll();
        assertThat(typeBillets).hasSize(databaseSizeBeforeCreate + 1);
        TypeBillet testTypeBillet = typeBillets.get(typeBillets.size() - 1);
        assertThat(testTypeBillet.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeBillet in ElasticSearch
        TypeBillet typeBilletEs = typeBilletSearchRepository.findOne(testTypeBillet.getId());
        assertThat(typeBilletEs).isEqualToComparingFieldByField(testTypeBillet);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeBilletRepository.findAll().size();
        // set the field null
        typeBillet.setLibelle(null);

        // Create the TypeBillet, which fails.

        restTypeBilletMockMvc.perform(post("/api/type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeBillet)))
                .andExpect(status().isBadRequest());

        List<TypeBillet> typeBillets = typeBilletRepository.findAll();
        assertThat(typeBillets).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeBillets() throws Exception {
        // Initialize the database
        typeBilletRepository.saveAndFlush(typeBillet);

        // Get all the typeBillets
        restTypeBilletMockMvc.perform(get("/api/type-billets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeBillet.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeBillet() throws Exception {
        // Initialize the database
        typeBilletRepository.saveAndFlush(typeBillet);

        // Get the typeBillet
        restTypeBilletMockMvc.perform(get("/api/type-billets/{id}", typeBillet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeBillet.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeBillet() throws Exception {
        // Get the typeBillet
        restTypeBilletMockMvc.perform(get("/api/type-billets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeBillet() throws Exception {
        // Initialize the database
        typeBilletRepository.saveAndFlush(typeBillet);
        typeBilletSearchRepository.save(typeBillet);
        int databaseSizeBeforeUpdate = typeBilletRepository.findAll().size();

        // Update the typeBillet
        TypeBillet updatedTypeBillet = new TypeBillet();
        updatedTypeBillet.setId(typeBillet.getId());
        updatedTypeBillet.setLibelle(UPDATED_LIBELLE);

        restTypeBilletMockMvc.perform(put("/api/type-billets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeBillet)))
                .andExpect(status().isOk());

        // Validate the TypeBillet in the database
        List<TypeBillet> typeBillets = typeBilletRepository.findAll();
        assertThat(typeBillets).hasSize(databaseSizeBeforeUpdate);
        TypeBillet testTypeBillet = typeBillets.get(typeBillets.size() - 1);
        assertThat(testTypeBillet.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeBillet in ElasticSearch
        TypeBillet typeBilletEs = typeBilletSearchRepository.findOne(testTypeBillet.getId());
        assertThat(typeBilletEs).isEqualToComparingFieldByField(testTypeBillet);
    }

    @Test
    @Transactional
    public void deleteTypeBillet() throws Exception {
        // Initialize the database
        typeBilletRepository.saveAndFlush(typeBillet);
        typeBilletSearchRepository.save(typeBillet);
        int databaseSizeBeforeDelete = typeBilletRepository.findAll().size();

        // Get the typeBillet
        restTypeBilletMockMvc.perform(delete("/api/type-billets/{id}", typeBillet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeBilletExistsInEs = typeBilletSearchRepository.exists(typeBillet.getId());
        assertThat(typeBilletExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeBillet> typeBillets = typeBilletRepository.findAll();
        assertThat(typeBillets).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeBillet() throws Exception {
        // Initialize the database
        typeBilletRepository.saveAndFlush(typeBillet);
        typeBilletSearchRepository.save(typeBillet);

        // Search the typeBillet
        restTypeBilletMockMvc.perform(get("/api/_search/type-billets?query=id:" + typeBillet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeBillet.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
