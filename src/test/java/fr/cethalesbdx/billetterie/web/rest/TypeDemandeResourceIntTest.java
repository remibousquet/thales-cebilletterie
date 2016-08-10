package fr.cethalesbdx.billetterie.web.rest;

import fr.cethalesbdx.billetterie.CebilletterieApp;
import fr.cethalesbdx.billetterie.domain.TypeDemande;
import fr.cethalesbdx.billetterie.repository.TypeDemandeRepository;
import fr.cethalesbdx.billetterie.repository.search.TypeDemandeSearchRepository;

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
 * Test class for the TypeDemandeResource REST controller.
 *
 * @see TypeDemandeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CebilletterieApp.class)
@WebAppConfiguration
@IntegrationTest
public class TypeDemandeResourceIntTest {

    private static final String DEFAULT_LIBELLE = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_LIBELLE = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    @Inject
    private TypeDemandeRepository typeDemandeRepository;

    @Inject
    private TypeDemandeSearchRepository typeDemandeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTypeDemandeMockMvc;

    private TypeDemande typeDemande;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TypeDemandeResource typeDemandeResource = new TypeDemandeResource();
        ReflectionTestUtils.setField(typeDemandeResource, "typeDemandeSearchRepository", typeDemandeSearchRepository);
        ReflectionTestUtils.setField(typeDemandeResource, "typeDemandeRepository", typeDemandeRepository);
        this.restTypeDemandeMockMvc = MockMvcBuilders.standaloneSetup(typeDemandeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        typeDemandeSearchRepository.deleteAll();
        typeDemande = new TypeDemande();
        typeDemande.setLibelle(DEFAULT_LIBELLE);
    }

    @Test
    @Transactional
    public void createTypeDemande() throws Exception {
        int databaseSizeBeforeCreate = typeDemandeRepository.findAll().size();

        // Create the TypeDemande

        restTypeDemandeMockMvc.perform(post("/api/type-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeDemande)))
                .andExpect(status().isCreated());

        // Validate the TypeDemande in the database
        List<TypeDemande> typeDemandes = typeDemandeRepository.findAll();
        assertThat(typeDemandes).hasSize(databaseSizeBeforeCreate + 1);
        TypeDemande testTypeDemande = typeDemandes.get(typeDemandes.size() - 1);
        assertThat(testTypeDemande.getLibelle()).isEqualTo(DEFAULT_LIBELLE);

        // Validate the TypeDemande in ElasticSearch
        TypeDemande typeDemandeEs = typeDemandeSearchRepository.findOne(testTypeDemande.getId());
        assertThat(typeDemandeEs).isEqualToComparingFieldByField(testTypeDemande);
    }

    @Test
    @Transactional
    public void checkLibelleIsRequired() throws Exception {
        int databaseSizeBeforeTest = typeDemandeRepository.findAll().size();
        // set the field null
        typeDemande.setLibelle(null);

        // Create the TypeDemande, which fails.

        restTypeDemandeMockMvc.perform(post("/api/type-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(typeDemande)))
                .andExpect(status().isBadRequest());

        List<TypeDemande> typeDemandes = typeDemandeRepository.findAll();
        assertThat(typeDemandes).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTypeDemandes() throws Exception {
        // Initialize the database
        typeDemandeRepository.saveAndFlush(typeDemande);

        // Get all the typeDemandes
        restTypeDemandeMockMvc.perform(get("/api/type-demandes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(typeDemande.getId().intValue())))
                .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }

    @Test
    @Transactional
    public void getTypeDemande() throws Exception {
        // Initialize the database
        typeDemandeRepository.saveAndFlush(typeDemande);

        // Get the typeDemande
        restTypeDemandeMockMvc.perform(get("/api/type-demandes/{id}", typeDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(typeDemande.getId().intValue()))
            .andExpect(jsonPath("$.libelle").value(DEFAULT_LIBELLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTypeDemande() throws Exception {
        // Get the typeDemande
        restTypeDemandeMockMvc.perform(get("/api/type-demandes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTypeDemande() throws Exception {
        // Initialize the database
        typeDemandeRepository.saveAndFlush(typeDemande);
        typeDemandeSearchRepository.save(typeDemande);
        int databaseSizeBeforeUpdate = typeDemandeRepository.findAll().size();

        // Update the typeDemande
        TypeDemande updatedTypeDemande = new TypeDemande();
        updatedTypeDemande.setId(typeDemande.getId());
        updatedTypeDemande.setLibelle(UPDATED_LIBELLE);

        restTypeDemandeMockMvc.perform(put("/api/type-demandes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTypeDemande)))
                .andExpect(status().isOk());

        // Validate the TypeDemande in the database
        List<TypeDemande> typeDemandes = typeDemandeRepository.findAll();
        assertThat(typeDemandes).hasSize(databaseSizeBeforeUpdate);
        TypeDemande testTypeDemande = typeDemandes.get(typeDemandes.size() - 1);
        assertThat(testTypeDemande.getLibelle()).isEqualTo(UPDATED_LIBELLE);

        // Validate the TypeDemande in ElasticSearch
        TypeDemande typeDemandeEs = typeDemandeSearchRepository.findOne(testTypeDemande.getId());
        assertThat(typeDemandeEs).isEqualToComparingFieldByField(testTypeDemande);
    }

    @Test
    @Transactional
    public void deleteTypeDemande() throws Exception {
        // Initialize the database
        typeDemandeRepository.saveAndFlush(typeDemande);
        typeDemandeSearchRepository.save(typeDemande);
        int databaseSizeBeforeDelete = typeDemandeRepository.findAll().size();

        // Get the typeDemande
        restTypeDemandeMockMvc.perform(delete("/api/type-demandes/{id}", typeDemande.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean typeDemandeExistsInEs = typeDemandeSearchRepository.exists(typeDemande.getId());
        assertThat(typeDemandeExistsInEs).isFalse();

        // Validate the database is empty
        List<TypeDemande> typeDemandes = typeDemandeRepository.findAll();
        assertThat(typeDemandes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTypeDemande() throws Exception {
        // Initialize the database
        typeDemandeRepository.saveAndFlush(typeDemande);
        typeDemandeSearchRepository.save(typeDemande);

        // Search the typeDemande
        restTypeDemandeMockMvc.perform(get("/api/_search/type-demandes?query=id:" + typeDemande.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeDemande.getId().intValue())))
            .andExpect(jsonPath("$.[*].libelle").value(hasItem(DEFAULT_LIBELLE.toString())));
    }
}
