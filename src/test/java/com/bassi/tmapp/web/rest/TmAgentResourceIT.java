package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TmAgentAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import com.bassi.tmapp.service.mapper.TmAgentMapper;
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
 * Integration tests for the {@link TmAgentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TmAgentResourceIT {

    private static final String DEFAULT_AGENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tm-agents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TmAgentRepository tmAgentRepository;

    @Autowired
    private TmAgentMapper tmAgentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTmAgentMockMvc;

    private TmAgent tmAgent;

    private TmAgent insertedTmAgent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TmAgent createEntity(EntityManager em) {
        TmAgent tmAgent = new TmAgent()
            .agentCode(DEFAULT_AGENT_CODE)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .address(DEFAULT_ADDRESS)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED)
            .companyName(DEFAULT_COMPANY_NAME);
        return tmAgent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TmAgent createUpdatedEntity(EntityManager em) {
        TmAgent tmAgent = new TmAgent()
            .agentCode(UPDATED_AGENT_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME);
        return tmAgent;
    }

    @BeforeEach
    public void initTest() {
        tmAgent = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTmAgent != null) {
            tmAgentRepository.delete(insertedTmAgent);
            insertedTmAgent = null;
        }
    }

    @Test
    @Transactional
    void createTmAgent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);
        var returnedTmAgentDTO = om.readValue(
            restTmAgentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TmAgentDTO.class
        );

        // Validate the TmAgent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTmAgent = tmAgentMapper.toEntity(returnedTmAgentDTO);
        assertTmAgentUpdatableFieldsEquals(returnedTmAgent, getPersistedTmAgent(returnedTmAgent));

        insertedTmAgent = returnedTmAgent;
    }

    @Test
    @Transactional
    void createTmAgentWithExistingId() throws Exception {
        // Create the TmAgent with an existing ID
        tmAgent.setId(1L);
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTmAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTmAgents() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tmAgent.getId().intValue())))
            .andExpect(jsonPath("$.[*].agentCode").value(hasItem(DEFAULT_AGENT_CODE)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)));
    }

    @Test
    @Transactional
    void getTmAgent() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get the tmAgent
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL_ID, tmAgent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tmAgent.getId().intValue()))
            .andExpect(jsonPath("$.agentCode").value(DEFAULT_AGENT_CODE))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTmAgent() throws Exception {
        // Get the tmAgent
        restTmAgentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTmAgent() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tmAgent
        TmAgent updatedTmAgent = tmAgentRepository.findById(tmAgent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTmAgent are not directly saved in db
        em.detach(updatedTmAgent);
        updatedTmAgent
            .agentCode(UPDATED_AGENT_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME);
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(updatedTmAgent);

        restTmAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tmAgentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgentDTO))
            )
            .andExpect(status().isOk());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTmAgentToMatchAllProperties(updatedTmAgent);
    }

    @Test
    @Transactional
    void putNonExistingTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tmAgentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tmAgentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTmAgentWithPatch() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tmAgent using partial update
        TmAgent partialUpdatedTmAgent = new TmAgent();
        partialUpdatedTmAgent.setId(tmAgent.getId());

        partialUpdatedTmAgent.address(UPDATED_ADDRESS);

        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTmAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTmAgent))
            )
            .andExpect(status().isOk());

        // Validate the TmAgent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTmAgentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTmAgent, tmAgent), getPersistedTmAgent(tmAgent));
    }

    @Test
    @Transactional
    void fullUpdateTmAgentWithPatch() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tmAgent using partial update
        TmAgent partialUpdatedTmAgent = new TmAgent();
        partialUpdatedTmAgent.setId(tmAgent.getId());

        partialUpdatedTmAgent
            .agentCode(UPDATED_AGENT_CODE)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME);

        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTmAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTmAgent))
            )
            .andExpect(status().isOk());

        // Validate the TmAgent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTmAgentUpdatableFieldsEquals(partialUpdatedTmAgent, getPersistedTmAgent(partialUpdatedTmAgent));
    }

    @Test
    @Transactional
    void patchNonExistingTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tmAgentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tmAgentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tmAgentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // Create the TmAgent
        TmAgentDTO tmAgentDTO = tmAgentMapper.toDto(tmAgent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tmAgentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTmAgent() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tmAgent
        restTmAgentMockMvc
            .perform(delete(ENTITY_API_URL_ID, tmAgent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tmAgentRepository.count();
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

    protected TmAgent getPersistedTmAgent(TmAgent tmAgent) {
        return tmAgentRepository.findById(tmAgent.getId()).orElseThrow();
    }

    protected void assertPersistedTmAgentToMatchAllProperties(TmAgent expectedTmAgent) {
        assertTmAgentAllPropertiesEquals(expectedTmAgent, getPersistedTmAgent(expectedTmAgent));
    }

    protected void assertPersistedTmAgentToMatchUpdatableProperties(TmAgent expectedTmAgent) {
        assertTmAgentAllUpdatablePropertiesEquals(expectedTmAgent, getPersistedTmAgent(expectedTmAgent));
    }
}
