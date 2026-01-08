package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkTokenAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import com.bassi.tmapp.service.mapper.TrademarkTokenMapper;
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
 * Integration tests for the {@link TrademarkTokenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrademarkTokenResourceIT {

    private static final String DEFAULT_TOKEN_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_TEXT = "BBBBBBBBBB";

    private static final TrademarkTokenType DEFAULT_TOKEN_TYPE = TrademarkTokenType.CORE;
    private static final TrademarkTokenType UPDATED_TOKEN_TYPE = TrademarkTokenType.DESCRIPTIVE;

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;

    private static final String ENTITY_API_URL = "/api/trademark-tokens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrademarkTokenRepository trademarkTokenRepository;

    @Autowired
    private TrademarkTokenMapper trademarkTokenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrademarkTokenMockMvc;

    private TrademarkToken trademarkToken;

    private TrademarkToken insertedTrademarkToken;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkToken createEntity() {
        return new TrademarkToken().tokenText(DEFAULT_TOKEN_TEXT).tokenType(DEFAULT_TOKEN_TYPE).position(DEFAULT_POSITION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkToken createUpdatedEntity() {
        return new TrademarkToken().tokenText(UPDATED_TOKEN_TEXT).tokenType(UPDATED_TOKEN_TYPE).position(UPDATED_POSITION);
    }

    @BeforeEach
    void initTest() {
        trademarkToken = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrademarkToken != null) {
            trademarkTokenRepository.delete(insertedTrademarkToken);
            insertedTrademarkToken = null;
        }
    }

    @Test
    @Transactional
    void createTrademarkToken() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);
        var returnedTrademarkTokenDTO = om.readValue(
            restTrademarkTokenMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrademarkTokenDTO.class
        );

        // Validate the TrademarkToken in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrademarkToken = trademarkTokenMapper.toEntity(returnedTrademarkTokenDTO);
        assertTrademarkTokenUpdatableFieldsEquals(returnedTrademarkToken, getPersistedTrademarkToken(returnedTrademarkToken));

        insertedTrademarkToken = returnedTrademarkToken;
    }

    @Test
    @Transactional
    void createTrademarkTokenWithExistingId() throws Exception {
        // Create the TrademarkToken with an existing ID
        trademarkToken.setId(1L);
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrademarkTokenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrademarkTokens() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        // Get all the trademarkTokenList
        restTrademarkTokenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademarkToken.getId().intValue())))
            .andExpect(jsonPath("$.[*].tokenText").value(hasItem(DEFAULT_TOKEN_TEXT)))
            .andExpect(jsonPath("$.[*].tokenType").value(hasItem(DEFAULT_TOKEN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)));
    }

    @Test
    @Transactional
    void getTrademarkToken() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        // Get the trademarkToken
        restTrademarkTokenMockMvc
            .perform(get(ENTITY_API_URL_ID, trademarkToken.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trademarkToken.getId().intValue()))
            .andExpect(jsonPath("$.tokenText").value(DEFAULT_TOKEN_TEXT))
            .andExpect(jsonPath("$.tokenType").value(DEFAULT_TOKEN_TYPE.toString()))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION));
    }

    @Test
    @Transactional
    void getNonExistingTrademarkToken() throws Exception {
        // Get the trademarkToken
        restTrademarkTokenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrademarkToken() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkToken
        TrademarkToken updatedTrademarkToken = trademarkTokenRepository.findById(trademarkToken.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrademarkToken are not directly saved in db
        em.detach(updatedTrademarkToken);
        updatedTrademarkToken.tokenText(UPDATED_TOKEN_TEXT).tokenType(UPDATED_TOKEN_TYPE).position(UPDATED_POSITION);
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(updatedTrademarkToken);

        restTrademarkTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkTokenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrademarkTokenToMatchAllProperties(updatedTrademarkToken);
    }

    @Test
    @Transactional
    void putNonExistingTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkTokenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkTokenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrademarkTokenWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkToken using partial update
        TrademarkToken partialUpdatedTrademarkToken = new TrademarkToken();
        partialUpdatedTrademarkToken.setId(trademarkToken.getId());

        partialUpdatedTrademarkToken.tokenText(UPDATED_TOKEN_TEXT);

        restTrademarkTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkToken))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkToken in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkTokenUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrademarkToken, trademarkToken),
            getPersistedTrademarkToken(trademarkToken)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrademarkTokenWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkToken using partial update
        TrademarkToken partialUpdatedTrademarkToken = new TrademarkToken();
        partialUpdatedTrademarkToken.setId(trademarkToken.getId());

        partialUpdatedTrademarkToken.tokenText(UPDATED_TOKEN_TEXT).tokenType(UPDATED_TOKEN_TYPE).position(UPDATED_POSITION);

        restTrademarkTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkToken.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkToken))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkToken in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkTokenUpdatableFieldsEquals(partialUpdatedTrademarkToken, getPersistedTrademarkToken(partialUpdatedTrademarkToken));
    }

    @Test
    @Transactional
    void patchNonExistingTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trademarkTokenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkTokenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrademarkToken() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkToken.setId(longCount.incrementAndGet());

        // Create the TrademarkToken
        TrademarkTokenDTO trademarkTokenDTO = trademarkTokenMapper.toDto(trademarkToken);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkTokenMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trademarkTokenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkToken in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrademarkToken() throws Exception {
        // Initialize the database
        insertedTrademarkToken = trademarkTokenRepository.saveAndFlush(trademarkToken);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trademarkToken
        restTrademarkTokenMockMvc
            .perform(delete(ENTITY_API_URL_ID, trademarkToken.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trademarkTokenRepository.count();
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

    protected TrademarkToken getPersistedTrademarkToken(TrademarkToken trademarkToken) {
        return trademarkTokenRepository.findById(trademarkToken.getId()).orElseThrow();
    }

    protected void assertPersistedTrademarkTokenToMatchAllProperties(TrademarkToken expectedTrademarkToken) {
        assertTrademarkTokenAllPropertiesEquals(expectedTrademarkToken, getPersistedTrademarkToken(expectedTrademarkToken));
    }

    protected void assertPersistedTrademarkTokenToMatchUpdatableProperties(TrademarkToken expectedTrademarkToken) {
        assertTrademarkTokenAllUpdatablePropertiesEquals(expectedTrademarkToken, getPersistedTrademarkToken(expectedTrademarkToken));
    }
}
