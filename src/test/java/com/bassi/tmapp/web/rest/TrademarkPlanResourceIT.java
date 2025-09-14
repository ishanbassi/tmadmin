package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkPlanAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static com.bassi.tmapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TrademarkPlan;
import com.bassi.tmapp.repository.TrademarkPlanRepository;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import com.bassi.tmapp.service.mapper.TrademarkPlanMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TrademarkPlanResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrademarkPlanResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FEES = new BigDecimal(1);
    private static final BigDecimal UPDATED_FEES = new BigDecimal(2);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/trademark-plans";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrademarkPlanRepository trademarkPlanRepository;

    @Autowired
    private TrademarkPlanMapper trademarkPlanMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrademarkPlanMockMvc;

    private TrademarkPlan trademarkPlan;

    private TrademarkPlan insertedTrademarkPlan;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkPlan createEntity() {
        return new TrademarkPlan()
            .name(DEFAULT_NAME)
            .fees(DEFAULT_FEES)
            .notes(DEFAULT_NOTES)
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
    public static TrademarkPlan createUpdatedEntity() {
        return new TrademarkPlan()
            .name(UPDATED_NAME)
            .fees(UPDATED_FEES)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        trademarkPlan = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrademarkPlan != null) {
            trademarkPlanRepository.delete(insertedTrademarkPlan);
            insertedTrademarkPlan = null;
        }
    }

    @Test
    @Transactional
    void createTrademarkPlan() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);
        var returnedTrademarkPlanDTO = om.readValue(
            restTrademarkPlanMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkPlanDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrademarkPlanDTO.class
        );

        // Validate the TrademarkPlan in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrademarkPlan = trademarkPlanMapper.toEntity(returnedTrademarkPlanDTO);
        assertTrademarkPlanUpdatableFieldsEquals(returnedTrademarkPlan, getPersistedTrademarkPlan(returnedTrademarkPlan));

        insertedTrademarkPlan = returnedTrademarkPlan;
    }

    @Test
    @Transactional
    void createTrademarkPlanWithExistingId() throws Exception {
        // Create the TrademarkPlan with an existing ID
        trademarkPlan.setId(1L);
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrademarkPlanMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkPlanDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrademarkPlans() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        // Get all the trademarkPlanList
        restTrademarkPlanMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademarkPlan.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fees").value(hasItem(sameNumber(DEFAULT_FEES))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getTrademarkPlan() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        // Get the trademarkPlan
        restTrademarkPlanMockMvc
            .perform(get(ENTITY_API_URL_ID, trademarkPlan.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trademarkPlan.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fees").value(sameNumber(DEFAULT_FEES)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingTrademarkPlan() throws Exception {
        // Get the trademarkPlan
        restTrademarkPlanMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrademarkPlan() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkPlan
        TrademarkPlan updatedTrademarkPlan = trademarkPlanRepository.findById(trademarkPlan.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrademarkPlan are not directly saved in db
        em.detach(updatedTrademarkPlan);
        updatedTrademarkPlan
            .name(UPDATED_NAME)
            .fees(UPDATED_FEES)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(updatedTrademarkPlan);

        restTrademarkPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkPlanDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrademarkPlanToMatchAllProperties(updatedTrademarkPlan);
    }

    @Test
    @Transactional
    void putNonExistingTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkPlanDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrademarkPlanWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkPlan using partial update
        TrademarkPlan partialUpdatedTrademarkPlan = new TrademarkPlan();
        partialUpdatedTrademarkPlan.setId(trademarkPlan.getId());

        partialUpdatedTrademarkPlan.name(UPDATED_NAME).notes(UPDATED_NOTES).deleted(UPDATED_DELETED).modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkPlan))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkPlanUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrademarkPlan, trademarkPlan),
            getPersistedTrademarkPlan(trademarkPlan)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrademarkPlanWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkPlan using partial update
        TrademarkPlan partialUpdatedTrademarkPlan = new TrademarkPlan();
        partialUpdatedTrademarkPlan.setId(trademarkPlan.getId());

        partialUpdatedTrademarkPlan
            .name(UPDATED_NAME)
            .fees(UPDATED_FEES)
            .notes(UPDATED_NOTES)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkPlan.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkPlan))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkPlan in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkPlanUpdatableFieldsEquals(partialUpdatedTrademarkPlan, getPersistedTrademarkPlan(partialUpdatedTrademarkPlan));
    }

    @Test
    @Transactional
    void patchNonExistingTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trademarkPlanDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkPlanDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrademarkPlan() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkPlan.setId(longCount.incrementAndGet());

        // Create the TrademarkPlan
        TrademarkPlanDTO trademarkPlanDTO = trademarkPlanMapper.toDto(trademarkPlan);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkPlanMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trademarkPlanDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkPlan in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrademarkPlan() throws Exception {
        // Initialize the database
        insertedTrademarkPlan = trademarkPlanRepository.saveAndFlush(trademarkPlan);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trademarkPlan
        restTrademarkPlanMockMvc
            .perform(delete(ENTITY_API_URL_ID, trademarkPlan.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trademarkPlanRepository.count();
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

    protected TrademarkPlan getPersistedTrademarkPlan(TrademarkPlan trademarkPlan) {
        return trademarkPlanRepository.findById(trademarkPlan.getId()).orElseThrow();
    }

    protected void assertPersistedTrademarkPlanToMatchAllProperties(TrademarkPlan expectedTrademarkPlan) {
        assertTrademarkPlanAllPropertiesEquals(expectedTrademarkPlan, getPersistedTrademarkPlan(expectedTrademarkPlan));
    }

    protected void assertPersistedTrademarkPlanToMatchUpdatableProperties(TrademarkPlan expectedTrademarkPlan) {
        assertTrademarkPlanAllUpdatablePropertiesEquals(expectedTrademarkPlan, getPersistedTrademarkPlan(expectedTrademarkPlan));
    }
}
