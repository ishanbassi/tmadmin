package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.PublishedTmPhoneticsAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.repository.PublishedTmPhoneticsRepository;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.mapper.PublishedTmPhoneticsMapper;
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
 * Integration tests for the {@link PublishedTmPhoneticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublishedTmPhoneticsResourceIT {

    private static final String DEFAULT_SANITIZED_TM = "AAAAAAAAAA";
    private static final String UPDATED_SANITIZED_TM = "BBBBBBBBBB";

    private static final String DEFAULT_PHONETIC_PK = "AAAAAAAAAA";
    private static final String UPDATED_PHONETIC_PK = "BBBBBBBBBB";

    private static final String DEFAULT_PHONETIC_SK = "AAAAAAAAAA";
    private static final String UPDATED_PHONETIC_SK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_COMPLETE = false;
    private static final Boolean UPDATED_COMPLETE = true;

    private static final String ENTITY_API_URL = "/api/published-tm-phonetics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PublishedTmPhoneticsRepository publishedTmPhoneticsRepository;

    @Autowired
    private PublishedTmPhoneticsMapper publishedTmPhoneticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublishedTmPhoneticsMockMvc;

    private PublishedTmPhonetics publishedTmPhonetics;

    private PublishedTmPhonetics insertedPublishedTmPhonetics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublishedTmPhonetics createEntity(EntityManager em) {
        PublishedTmPhonetics publishedTmPhonetics = new PublishedTmPhonetics()
            .sanitizedTm(DEFAULT_SANITIZED_TM)
            .phoneticPk(DEFAULT_PHONETIC_PK)
            .phoneticSk(DEFAULT_PHONETIC_SK)
            .complete(DEFAULT_COMPLETE);
        return publishedTmPhonetics;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublishedTmPhonetics createUpdatedEntity(EntityManager em) {
        PublishedTmPhonetics publishedTmPhonetics = new PublishedTmPhonetics()
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);
        return publishedTmPhonetics;
    }

    @BeforeEach
    public void initTest() {
        publishedTmPhonetics = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPublishedTmPhonetics != null) {
            publishedTmPhoneticsRepository.delete(insertedPublishedTmPhonetics);
            insertedPublishedTmPhonetics = null;
        }
    }

    @Test
    @Transactional
    void createPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);
        var returnedPublishedTmPhoneticsDTO = om.readValue(
            restPublishedTmPhoneticsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PublishedTmPhoneticsDTO.class
        );

        // Validate the PublishedTmPhonetics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPublishedTmPhonetics = publishedTmPhoneticsMapper.toEntity(returnedPublishedTmPhoneticsDTO);
        assertPublishedTmPhoneticsUpdatableFieldsEquals(
            returnedPublishedTmPhonetics,
            getPersistedPublishedTmPhonetics(returnedPublishedTmPhonetics)
        );

        insertedPublishedTmPhonetics = returnedPublishedTmPhonetics;
    }

    @Test
    @Transactional
    void createPublishedTmPhoneticsWithExistingId() throws Exception {
        // Create the PublishedTmPhonetics with an existing ID
        publishedTmPhonetics.setId(1L);
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublishedTmPhoneticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmPhoneticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPublishedTmPhonetics() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        // Get all the publishedTmPhoneticsList
        restPublishedTmPhoneticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publishedTmPhonetics.getId().intValue())))
            .andExpect(jsonPath("$.[*].sanitizedTm").value(hasItem(DEFAULT_SANITIZED_TM)))
            .andExpect(jsonPath("$.[*].phoneticPk").value(hasItem(DEFAULT_PHONETIC_PK)))
            .andExpect(jsonPath("$.[*].phoneticSk").value(hasItem(DEFAULT_PHONETIC_SK)))
            .andExpect(jsonPath("$.[*].complete").value(hasItem(DEFAULT_COMPLETE.booleanValue())));
    }

    @Test
    @Transactional
    void getPublishedTmPhonetics() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        // Get the publishedTmPhonetics
        restPublishedTmPhoneticsMockMvc
            .perform(get(ENTITY_API_URL_ID, publishedTmPhonetics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publishedTmPhonetics.getId().intValue()))
            .andExpect(jsonPath("$.sanitizedTm").value(DEFAULT_SANITIZED_TM))
            .andExpect(jsonPath("$.phoneticPk").value(DEFAULT_PHONETIC_PK))
            .andExpect(jsonPath("$.phoneticSk").value(DEFAULT_PHONETIC_SK))
            .andExpect(jsonPath("$.complete").value(DEFAULT_COMPLETE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPublishedTmPhonetics() throws Exception {
        // Get the publishedTmPhonetics
        restPublishedTmPhoneticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPublishedTmPhonetics() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTmPhonetics
        PublishedTmPhonetics updatedPublishedTmPhonetics = publishedTmPhoneticsRepository
            .findById(publishedTmPhonetics.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPublishedTmPhonetics are not directly saved in db
        em.detach(updatedPublishedTmPhonetics);
        updatedPublishedTmPhonetics
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(updatedPublishedTmPhonetics);

        restPublishedTmPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publishedTmPhoneticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPublishedTmPhoneticsToMatchAllProperties(updatedPublishedTmPhonetics);
    }

    @Test
    @Transactional
    void putNonExistingPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publishedTmPhoneticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmPhoneticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublishedTmPhoneticsWithPatch() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTmPhonetics using partial update
        PublishedTmPhonetics partialUpdatedPublishedTmPhonetics = new PublishedTmPhonetics();
        partialUpdatedPublishedTmPhonetics.setId(publishedTmPhonetics.getId());

        restPublishedTmPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublishedTmPhonetics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublishedTmPhonetics))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTmPhonetics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublishedTmPhoneticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPublishedTmPhonetics, publishedTmPhonetics),
            getPersistedPublishedTmPhonetics(publishedTmPhonetics)
        );
    }

    @Test
    @Transactional
    void fullUpdatePublishedTmPhoneticsWithPatch() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTmPhonetics using partial update
        PublishedTmPhonetics partialUpdatedPublishedTmPhonetics = new PublishedTmPhonetics();
        partialUpdatedPublishedTmPhonetics.setId(publishedTmPhonetics.getId());

        partialUpdatedPublishedTmPhonetics
            .sanitizedTm(UPDATED_SANITIZED_TM)
            .phoneticPk(UPDATED_PHONETIC_PK)
            .phoneticSk(UPDATED_PHONETIC_SK)
            .complete(UPDATED_COMPLETE);

        restPublishedTmPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublishedTmPhonetics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublishedTmPhonetics))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTmPhonetics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublishedTmPhoneticsUpdatableFieldsEquals(
            partialUpdatedPublishedTmPhonetics,
            getPersistedPublishedTmPhonetics(partialUpdatedPublishedTmPhonetics)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publishedTmPhoneticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublishedTmPhonetics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTmPhonetics.setId(longCount.incrementAndGet());

        // Create the PublishedTmPhonetics
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO = publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmPhoneticsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(publishedTmPhoneticsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTmPhonetics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublishedTmPhonetics() throws Exception {
        // Initialize the database
        insertedPublishedTmPhonetics = publishedTmPhoneticsRepository.saveAndFlush(publishedTmPhonetics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the publishedTmPhonetics
        restPublishedTmPhoneticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, publishedTmPhonetics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return publishedTmPhoneticsRepository.count();
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

    protected PublishedTmPhonetics getPersistedPublishedTmPhonetics(PublishedTmPhonetics publishedTmPhonetics) {
        return publishedTmPhoneticsRepository.findById(publishedTmPhonetics.getId()).orElseThrow();
    }

    protected void assertPersistedPublishedTmPhoneticsToMatchAllProperties(PublishedTmPhonetics expectedPublishedTmPhonetics) {
        assertPublishedTmPhoneticsAllPropertiesEquals(
            expectedPublishedTmPhonetics,
            getPersistedPublishedTmPhonetics(expectedPublishedTmPhonetics)
        );
    }

    protected void assertPersistedPublishedTmPhoneticsToMatchUpdatableProperties(PublishedTmPhonetics expectedPublishedTmPhonetics) {
        assertPublishedTmPhoneticsAllUpdatablePropertiesEquals(
            expectedPublishedTmPhonetics,
            getPersistedPublishedTmPhonetics(expectedPublishedTmPhonetics)
        );
    }
}
