package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TokenPhoneticAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.enumeration.PhoneticAlgorithmType;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.service.dto.TokenPhoneticDTO;
import com.bassi.tmapp.service.mapper.TokenPhoneticMapper;
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
 * Integration tests for the {@link TokenPhoneticResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TokenPhoneticResourceIT {

    private static final PhoneticAlgorithmType DEFAULT_ALGORITHM = PhoneticAlgorithmType.SOUNDEX;
    private static final PhoneticAlgorithmType UPDATED_ALGORITHM = PhoneticAlgorithmType.METAPHONE;

    private static final String DEFAULT_PHONETIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_PHONETIC_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_PHONETIC_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_PHONETIC_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/token-phonetics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TokenPhoneticRepository tokenPhoneticRepository;

    @Autowired
    private TokenPhoneticMapper tokenPhoneticMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTokenPhoneticMockMvc;

    private TokenPhonetic tokenPhonetic;

    private TokenPhonetic insertedTokenPhonetic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenPhonetic createEntity() {
        return new TokenPhonetic()
            .algorithm(DEFAULT_ALGORITHM)
            .phoneticCode(DEFAULT_PHONETIC_CODE)
            .secondaryPhoneticCode(DEFAULT_SECONDARY_PHONETIC_CODE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TokenPhonetic createUpdatedEntity() {
        return new TokenPhonetic()
            .algorithm(UPDATED_ALGORITHM)
            .phoneticCode(UPDATED_PHONETIC_CODE)
            .secondaryPhoneticCode(UPDATED_SECONDARY_PHONETIC_CODE);
    }

    @BeforeEach
    void initTest() {
        tokenPhonetic = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTokenPhonetic != null) {
            tokenPhoneticRepository.delete(insertedTokenPhonetic);
            insertedTokenPhonetic = null;
        }
    }

    @Test
    @Transactional
    void createTokenPhonetic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);
        var returnedTokenPhoneticDTO = om.readValue(
            restTokenPhoneticMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tokenPhoneticDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TokenPhoneticDTO.class
        );

        // Validate the TokenPhonetic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTokenPhonetic = tokenPhoneticMapper.toEntity(returnedTokenPhoneticDTO);
        assertTokenPhoneticUpdatableFieldsEquals(returnedTokenPhonetic, getPersistedTokenPhonetic(returnedTokenPhonetic));

        insertedTokenPhonetic = returnedTokenPhonetic;
    }

    @Test
    @Transactional
    void createTokenPhoneticWithExistingId() throws Exception {
        // Create the TokenPhonetic with an existing ID
        tokenPhonetic.setId(1L);
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTokenPhoneticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tokenPhoneticDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTokenPhonetics() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        // Get all the tokenPhoneticList
        restTokenPhoneticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tokenPhonetic.getId().intValue())))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].phoneticCode").value(hasItem(DEFAULT_PHONETIC_CODE)))
            .andExpect(jsonPath("$.[*].secondaryPhoneticCode").value(hasItem(DEFAULT_SECONDARY_PHONETIC_CODE)));
    }

    @Test
    @Transactional
    void getTokenPhonetic() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        // Get the tokenPhonetic
        restTokenPhoneticMockMvc
            .perform(get(ENTITY_API_URL_ID, tokenPhonetic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tokenPhonetic.getId().intValue()))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
            .andExpect(jsonPath("$.phoneticCode").value(DEFAULT_PHONETIC_CODE))
            .andExpect(jsonPath("$.secondaryPhoneticCode").value(DEFAULT_SECONDARY_PHONETIC_CODE));
    }

    @Test
    @Transactional
    void getNonExistingTokenPhonetic() throws Exception {
        // Get the tokenPhonetic
        restTokenPhoneticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTokenPhonetic() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tokenPhonetic
        TokenPhonetic updatedTokenPhonetic = tokenPhoneticRepository.findById(tokenPhonetic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTokenPhonetic are not directly saved in db
        em.detach(updatedTokenPhonetic);
        updatedTokenPhonetic
            .algorithm(UPDATED_ALGORITHM)
            .phoneticCode(UPDATED_PHONETIC_CODE)
            .secondaryPhoneticCode(UPDATED_SECONDARY_PHONETIC_CODE);
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(updatedTokenPhonetic);

        restTokenPhoneticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tokenPhoneticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tokenPhoneticDTO))
            )
            .andExpect(status().isOk());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTokenPhoneticToMatchAllProperties(updatedTokenPhonetic);
    }

    @Test
    @Transactional
    void putNonExistingTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tokenPhoneticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tokenPhoneticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tokenPhoneticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tokenPhoneticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTokenPhoneticWithPatch() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tokenPhonetic using partial update
        TokenPhonetic partialUpdatedTokenPhonetic = new TokenPhonetic();
        partialUpdatedTokenPhonetic.setId(tokenPhonetic.getId());

        partialUpdatedTokenPhonetic.phoneticCode(UPDATED_PHONETIC_CODE).secondaryPhoneticCode(UPDATED_SECONDARY_PHONETIC_CODE);

        restTokenPhoneticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTokenPhonetic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTokenPhonetic))
            )
            .andExpect(status().isOk());

        // Validate the TokenPhonetic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTokenPhoneticUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTokenPhonetic, tokenPhonetic),
            getPersistedTokenPhonetic(tokenPhonetic)
        );
    }

    @Test
    @Transactional
    void fullUpdateTokenPhoneticWithPatch() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tokenPhonetic using partial update
        TokenPhonetic partialUpdatedTokenPhonetic = new TokenPhonetic();
        partialUpdatedTokenPhonetic.setId(tokenPhonetic.getId());

        partialUpdatedTokenPhonetic
            .algorithm(UPDATED_ALGORITHM)
            .phoneticCode(UPDATED_PHONETIC_CODE)
            .secondaryPhoneticCode(UPDATED_SECONDARY_PHONETIC_CODE);

        restTokenPhoneticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTokenPhonetic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTokenPhonetic))
            )
            .andExpect(status().isOk());

        // Validate the TokenPhonetic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTokenPhoneticUpdatableFieldsEquals(partialUpdatedTokenPhonetic, getPersistedTokenPhonetic(partialUpdatedTokenPhonetic));
    }

    @Test
    @Transactional
    void patchNonExistingTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tokenPhoneticDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tokenPhoneticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tokenPhoneticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTokenPhonetic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tokenPhonetic.setId(longCount.incrementAndGet());

        // Create the TokenPhonetic
        TokenPhoneticDTO tokenPhoneticDTO = tokenPhoneticMapper.toDto(tokenPhonetic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTokenPhoneticMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tokenPhoneticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TokenPhonetic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTokenPhonetic() throws Exception {
        // Initialize the database
        insertedTokenPhonetic = tokenPhoneticRepository.saveAndFlush(tokenPhonetic);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tokenPhonetic
        restTokenPhoneticMockMvc
            .perform(delete(ENTITY_API_URL_ID, tokenPhonetic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tokenPhoneticRepository.count();
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

    protected TokenPhonetic getPersistedTokenPhonetic(TokenPhonetic tokenPhonetic) {
        return tokenPhoneticRepository.findById(tokenPhonetic.getId()).orElseThrow();
    }

    protected void assertPersistedTokenPhoneticToMatchAllProperties(TokenPhonetic expectedTokenPhonetic) {
        assertTokenPhoneticAllPropertiesEquals(expectedTokenPhonetic, getPersistedTokenPhonetic(expectedTokenPhonetic));
    }

    protected void assertPersistedTokenPhoneticToMatchUpdatableProperties(TokenPhonetic expectedTokenPhonetic) {
        assertTokenPhoneticAllUpdatablePropertiesEquals(expectedTokenPhonetic, getPersistedTokenPhonetic(expectedTokenPhonetic));
    }
}
