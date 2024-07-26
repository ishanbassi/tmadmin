package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.PhoneticsAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.repository.PhoneticsRepository;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
import com.bassi.tmapp.service.mapper.PhoneticsMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PhoneticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PhoneticsResourceIT {

    private static final String DEFAULT_SANITIZED_TM = "AAAAAAAAAA";
    private static final String UPDATED_SANITIZED_TM = "BBBBBBBBBB";

    private static final String DEFAULT_PHONETIC_PK = "AAAAAAAAAA";
    private static final String UPDATED_PHONETIC_PK = "BBBBBBBBBB";

    private static final String DEFAULT_PHONETIC_SK = "AAAAAAAAAA";
    private static final String UPDATED_PHONETIC_SK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETE = false;
    private static final Boolean UPDATED_COMPLETE = true;

    private static final String ENTITY_API_URL = "/api/phonetics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PhoneticsRepository phoneticsRepository;

    @Autowired
    private PhoneticsMapper phoneticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhoneticsMockMvc;

    private Phonetics phonetics;

    private Phonetics insertedPhonetics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phonetics createEntity(EntityManager em) {
        Phonetics phonetics = new Phonetics()
            .sanitizedTm(DEFAULT_SANITIZED_TM)
            .phoneticPk(DEFAULT_PHONETIC_PK)
            .phoneticSk(DEFAULT_PHONETIC_SK)
            .complete(DEFAULT_COMPLETE);
        return phonetics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phonetics createUpdatedEntity(EntityManager em) {
        Phonetics phonetics = new Phonetics()
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);
        return phonetics;
    }

    @BeforeEach
    public void initTest() {
        phonetics = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPhonetics != null) {
            phoneticsRepository.delete(insertedPhonetics);
            insertedPhonetics = null;
        }
    }

    @Test
    @Transactional
    void createPhonetics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);
        var returnedPhoneticsDTO = om.readValue(
            restPhoneticsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneticsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PhoneticsDTO.class
        );

        // Validate the Phonetics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPhonetics = phoneticsMapper.toEntity(returnedPhoneticsDTO);
        assertPhoneticsUpdatableFieldsEquals(returnedPhonetics, getPersistedPhonetics(returnedPhonetics));

        insertedPhonetics = returnedPhonetics;
    }

    @Test
    @Transactional
    void createPhoneticsWithExistingId() throws Exception {
        // Create the Phonetics with an existing ID
        phonetics.setId(1L);
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhoneticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPhonetics() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        // Get all the phoneticsList
        restPhoneticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phonetics.getId().intValue())))
            .andExpect(jsonPath("$.[*].sanitizedTm").value(hasItem(DEFAULT_SANITIZED_TM)))
            .andExpect(jsonPath("$.[*].phoneticPk").value(hasItem(DEFAULT_PHONETIC_PK)))
            .andExpect(jsonPath("$.[*].phoneticSk").value(hasItem(DEFAULT_PHONETIC_SK)))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())));
    }

    @Test
    @Transactional
    void getPhonetics() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        // Get the phonetics
        restPhoneticsMockMvc
            .perform(get(ENTITY_API_URL_ID, phonetics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phonetics.getId().intValue()))
            .andExpect(jsonPath("$.sanitizedTm").value(DEFAULT_SANITIZED_TM))
            .andExpect(jsonPath("$.phoneticPk").value(DEFAULT_PHONETIC_PK))
            .andExpect(jsonPath("$.phoneticSk").value(DEFAULT_PHONETIC_SK))
            .andExpect(jsonPath("$.complete").value(DEFAULT_COMPLETE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPhonetics() throws Exception {
        // Get the phonetics
        restPhoneticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhonetics() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phonetics
        Phonetics updatedPhonetics = phoneticsRepository.findById(phonetics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhonetics are not directly saved in db
        em.detach(updatedPhonetics);
        updatedPhonetics
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(updatedPhonetics);

        restPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phoneticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPhoneticsToMatchAllProperties(updatedPhonetics);
    }

    @Test
    @Transactional
    void putNonExistingPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phoneticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(phoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(phoneticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhoneticsWithPatch() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phonetics using partial update
        Phonetics partialUpdatedPhonetics = new Phonetics();
        partialUpdatedPhonetics.setId(phonetics.getId());

        partialUpdatedPhonetics.phoneticPk(UPDATED_PHONETIC_PK);

        restPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhonetics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhonetics))
            )
            .andExpect(status().isOk());

        // Validate the Phonetics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhoneticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPhonetics, phonetics),
            getPersistedPhonetics(phonetics)
        );
    }

    @Test
    @Transactional
    void fullUpdatePhoneticsWithPatch() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the phonetics using partial update
        Phonetics partialUpdatedPhonetics = new Phonetics();
        partialUpdatedPhonetics.setId(phonetics.getId());

        partialUpdatedPhonetics
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);

        restPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhonetics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPhonetics))
            )
            .andExpect(status().isOk());

        // Validate the Phonetics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPhoneticsUpdatableFieldsEquals(partialUpdatedPhonetics, getPersistedPhonetics(partialUpdatedPhonetics));
    }

    @Test
    @Transactional
    void patchNonExistingPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phoneticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(phoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        phonetics.setId(longCount.incrementAndGet());

        // Create the Phonetics
        PhoneticsDTO phoneticsDTO = phoneticsMapper.toDto(phonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhoneticsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(phoneticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhonetics() throws Exception {
        // Initialize the database
        insertedPhonetics = phoneticsRepository.saveAndFlush(phonetics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the phonetics
        restPhoneticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, phonetics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return phoneticsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Phonetics getPersistedPhonetics(Phonetics phonetics) {
        return phoneticsRepository.findById(phonetics.getId()).orElseThrow();
    }

    protected void assertPersistedPhoneticsToMatchAllProperties(Phonetics expectedPhonetics) {
        assertPhoneticsAllPropertiesEquals(expectedPhonetics, getPersistedPhonetics(expectedPhonetics));
    }

    protected void assertPersistedPhoneticsToMatchUpdatableProperties(Phonetics expectedPhonetics) {
        assertPhoneticsAllUpdatablePropertiesEquals(expectedPhonetics, getPersistedPhonetics(expectedPhonetics));
    }
}
