package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkTokenFrequencyAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TrademarkTokenFrequency;
import com.bassi.tmapp.repository.TrademarkTokenFrequencyRepository;
import com.bassi.tmapp.service.dto.TrademarkTokenFrequencyDTO;
import com.bassi.tmapp.service.mapper.TrademarkTokenFrequencyMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link TrademarkTokenFrequencyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrademarkTokenFrequencyResourceIT {

    private static final Integer DEFAULT_FREQUENCY = 1;
    private static final Integer UPDATED_FREQUENCY = 2;

    private static final String DEFAULT_WORD = "AAAAAAAAAA";
    private static final String UPDATED_WORD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/trademark-token-frequencies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository;

    @Autowired
    private TrademarkTokenFrequencyMapper trademarkTokenFrequencyMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrademarkTokenFrequencyMockMvc;

    private TrademarkTokenFrequency trademarkTokenFrequency;

    private TrademarkTokenFrequency insertedTrademarkTokenFrequency;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkTokenFrequency createEntity() {
        return new TrademarkTokenFrequency()
            .frequency(DEFAULT_FREQUENCY)
            .word(DEFAULT_WORD)
            .createdDate(DEFAULT_CREATED_DATE)
            .deleted(DEFAULT_DELETED)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkTokenFrequency createUpdatedEntity() {
        return new TrademarkTokenFrequency()
            .frequency(UPDATED_FREQUENCY)
            .word(UPDATED_WORD)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        trademarkTokenFrequency = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrademarkTokenFrequency != null) {
            trademarkTokenFrequencyRepository.delete(insertedTrademarkTokenFrequency);
            insertedTrademarkTokenFrequency = null;
        }
    }

    @Test
    @Transactional
    void createTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);
        var returnedTrademarkTokenFrequencyDTO = om.readValue(
            restTrademarkTokenFrequencyMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrademarkTokenFrequencyDTO.class
        );

        // Validate the TrademarkTokenFrequency in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrademarkTokenFrequency = trademarkTokenFrequencyMapper.toEntity(returnedTrademarkTokenFrequencyDTO);
        assertTrademarkTokenFrequencyUpdatableFieldsEquals(
            returnedTrademarkTokenFrequency,
            getPersistedTrademarkTokenFrequency(returnedTrademarkTokenFrequency)
        );

        insertedTrademarkTokenFrequency = returnedTrademarkTokenFrequency;
    }

    @Test
    @Transactional
    void createTrademarkTokenFrequencyWithExistingId() throws Exception {
        // Create the TrademarkTokenFrequency with an existing ID
        trademarkTokenFrequency.setId(1L);
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrademarkTokenFrequencyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenFrequencyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrademarkTokenFrequencies() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        // Get all the trademarkTokenFrequencyList
        restTrademarkTokenFrequencyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademarkTokenFrequency.getId().intValue())))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY)))
            .andExpect(jsonPath("$.[*].word").value(hasItem(DEFAULT_WORD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getTrademarkTokenFrequency() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        // Get the trademarkTokenFrequency
        restTrademarkTokenFrequencyMockMvc
            .perform(get(ENTITY_API_URL_ID, trademarkTokenFrequency.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trademarkTokenFrequency.getId().intValue()))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY))
            .andExpect(jsonPath("$.word").value(DEFAULT_WORD))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingTrademarkTokenFrequency() throws Exception {
        // Get the trademarkTokenFrequency
        restTrademarkTokenFrequencyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrademarkTokenFrequency() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkTokenFrequency
        TrademarkTokenFrequency updatedTrademarkTokenFrequency = trademarkTokenFrequencyRepository
            .findById(trademarkTokenFrequency.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTrademarkTokenFrequency are not directly saved in db
        em.detach(updatedTrademarkTokenFrequency);
        updatedTrademarkTokenFrequency
            .frequency(UPDATED_FREQUENCY)
            .word(UPDATED_WORD)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(updatedTrademarkTokenFrequency);

        restTrademarkTokenFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkTokenFrequencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrademarkTokenFrequencyToMatchAllProperties(updatedTrademarkTokenFrequency);
    }

    @Test
    @Transactional
    void putNonExistingTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkTokenFrequencyDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenFrequencyDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrademarkTokenFrequencyWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkTokenFrequency using partial update
        TrademarkTokenFrequency partialUpdatedTrademarkTokenFrequency = new TrademarkTokenFrequency();
        partialUpdatedTrademarkTokenFrequency.setId(trademarkTokenFrequency.getId());

        partialUpdatedTrademarkTokenFrequency
            .frequency(UPDATED_FREQUENCY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkTokenFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkTokenFrequency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkTokenFrequency))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkTokenFrequency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkTokenFrequencyUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrademarkTokenFrequency, trademarkTokenFrequency),
            getPersistedTrademarkTokenFrequency(trademarkTokenFrequency)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrademarkTokenFrequencyWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkTokenFrequency using partial update
        TrademarkTokenFrequency partialUpdatedTrademarkTokenFrequency = new TrademarkTokenFrequency();
        partialUpdatedTrademarkTokenFrequency.setId(trademarkTokenFrequency.getId());

        partialUpdatedTrademarkTokenFrequency
            .frequency(UPDATED_FREQUENCY)
            .word(UPDATED_WORD)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkTokenFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkTokenFrequency.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkTokenFrequency))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkTokenFrequency in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkTokenFrequencyUpdatableFieldsEquals(
            partialUpdatedTrademarkTokenFrequency,
            getPersistedTrademarkTokenFrequency(partialUpdatedTrademarkTokenFrequency)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trademarkTokenFrequencyDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrademarkTokenFrequency() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkTokenFrequency.setId(longCount.incrementAndGet());

        // Create the TrademarkTokenFrequency
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO = trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenFrequencyMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trademarkTokenFrequencyDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkTokenFrequency in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrademarkTokenFrequency() throws Exception {
        // Initialize the database
        insertedTrademarkTokenFrequency = trademarkTokenFrequencyRepository.saveAndFlush(trademarkTokenFrequency);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trademarkTokenFrequency
        restTrademarkTokenFrequencyMockMvc
            .perform(delete(ENTITY_API_URL_ID, trademarkTokenFrequency.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trademarkTokenFrequencyRepository.count();
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

    protected TrademarkTokenFrequency getPersistedTrademarkTokenFrequency(TrademarkTokenFrequency trademarkTokenFrequency) {
        return trademarkTokenFrequencyRepository.findById(trademarkTokenFrequency.getId()).orElseThrow();
    }

    protected void assertPersistedTrademarkTokenFrequencyToMatchAllProperties(TrademarkTokenFrequency expectedTrademarkTokenFrequency) {
        assertTrademarkTokenFrequencyAllPropertiesEquals(
            expectedTrademarkTokenFrequency,
            getPersistedTrademarkTokenFrequency(expectedTrademarkTokenFrequency)
        );
    }

    protected void assertPersistedTrademarkTokenFrequencyToMatchUpdatableProperties(
        TrademarkTokenFrequency expectedTrademarkTokenFrequency
    ) {
        assertTrademarkTokenFrequencyAllUpdatablePropertiesEquals(
            expectedTrademarkTokenFrequency,
            getPersistedTrademarkTokenFrequency(expectedTrademarkTokenFrequency)
        );
    }
}
