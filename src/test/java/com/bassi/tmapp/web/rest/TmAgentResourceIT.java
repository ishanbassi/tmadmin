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

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_COMPANY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGENT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tm-agents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TmAgentRepository tmAgentRepository;

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
    public static TmAgent createEntity() {
        return new TmAgent()
            .fullName(DEFAULT_FULL_NAME)
            .address(DEFAULT_ADDRESS)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED)
            .companyName(DEFAULT_COMPANY_NAME)
            .agentCode(DEFAULT_AGENT_CODE)
            .email(DEFAULT_EMAIL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TmAgent createUpdatedEntity() {
        return new TmAgent()
            .fullName(UPDATED_FULL_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME)
            .agentCode(UPDATED_AGENT_CODE)
            .email(UPDATED_EMAIL);
    }

    @BeforeEach
    public void initTest() {
        tmAgent = createEntity();
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
        var returnedTmAgent = om.readValue(
            restTmAgentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgent)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TmAgent.class
        );

        // Validate the TmAgent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTmAgentUpdatableFieldsEquals(returnedTmAgent, getPersistedTmAgent(returnedTmAgent));

        insertedTmAgent = returnedTmAgent;
    }

    @Test
    @Transactional
    void createTmAgentWithExistingId() throws Exception {
        // Create the TmAgent with an existing ID
        tmAgent.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTmAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgent)))
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
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].agentCode").value(hasItem(DEFAULT_AGENT_CODE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
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
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.companyName").value(DEFAULT_COMPANY_NAME))
            .andExpect(jsonPath("$.agentCode").value(DEFAULT_AGENT_CODE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getTmAgentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        Long id = tmAgent.getId();

        defaultTmAgentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTmAgentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTmAgentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTmAgentsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where fullName equals to
        defaultTmAgentFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where fullName in
        defaultTmAgentFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where fullName is not null
        defaultTmAgentFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where fullName contains
        defaultTmAgentFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where fullName does not contain
        defaultTmAgentFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where address equals to
        defaultTmAgentFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where address in
        defaultTmAgentFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where address is not null
        defaultTmAgentFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where address contains
        defaultTmAgentFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where address does not contain
        defaultTmAgentFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate equals to
        defaultTmAgentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate in
        defaultTmAgentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate is not null
        defaultTmAgentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate is greater than or equal to
        defaultTmAgentFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate is less than or equal to
        defaultTmAgentFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate is less than
        defaultTmAgentFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where createdDate is greater than
        defaultTmAgentFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate equals to
        defaultTmAgentFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate in
        defaultTmAgentFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate is not null
        defaultTmAgentFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate is greater than or equal to
        defaultTmAgentFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate is less than or equal to
        defaultTmAgentFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate is less than
        defaultTmAgentFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where modifiedDate is greater than
        defaultTmAgentFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where deleted equals to
        defaultTmAgentFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTmAgentsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where deleted in
        defaultTmAgentFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTmAgentsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where deleted is not null
        defaultTmAgentFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByCompanyNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where companyName equals to
        defaultTmAgentFiltering("companyName.equals=" + DEFAULT_COMPANY_NAME, "companyName.equals=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByCompanyNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where companyName in
        defaultTmAgentFiltering(
            "companyName.in=" + DEFAULT_COMPANY_NAME + "," + UPDATED_COMPANY_NAME,
            "companyName.in=" + UPDATED_COMPANY_NAME
        );
    }

    @Test
    @Transactional
    void getAllTmAgentsByCompanyNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where companyName is not null
        defaultTmAgentFiltering("companyName.specified=true", "companyName.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByCompanyNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where companyName contains
        defaultTmAgentFiltering("companyName.contains=" + DEFAULT_COMPANY_NAME, "companyName.contains=" + UPDATED_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByCompanyNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where companyName does not contain
        defaultTmAgentFiltering("companyName.doesNotContain=" + UPDATED_COMPANY_NAME, "companyName.doesNotContain=" + DEFAULT_COMPANY_NAME);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAgentCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where agentCode equals to
        defaultTmAgentFiltering("agentCode.equals=" + DEFAULT_AGENT_CODE, "agentCode.equals=" + UPDATED_AGENT_CODE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAgentCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where agentCode in
        defaultTmAgentFiltering("agentCode.in=" + DEFAULT_AGENT_CODE + "," + UPDATED_AGENT_CODE, "agentCode.in=" + UPDATED_AGENT_CODE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAgentCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where agentCode is not null
        defaultTmAgentFiltering("agentCode.specified=true", "agentCode.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByAgentCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where agentCode contains
        defaultTmAgentFiltering("agentCode.contains=" + DEFAULT_AGENT_CODE, "agentCode.contains=" + UPDATED_AGENT_CODE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByAgentCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where agentCode does not contain
        defaultTmAgentFiltering("agentCode.doesNotContain=" + UPDATED_AGENT_CODE, "agentCode.doesNotContain=" + DEFAULT_AGENT_CODE);
    }

    @Test
    @Transactional
    void getAllTmAgentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where email equals to
        defaultTmAgentFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTmAgentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where email in
        defaultTmAgentFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTmAgentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where email is not null
        defaultTmAgentFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllTmAgentsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where email contains
        defaultTmAgentFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllTmAgentsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTmAgent = tmAgentRepository.saveAndFlush(tmAgent);

        // Get all the tmAgentList where email does not contain
        defaultTmAgentFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    private void defaultTmAgentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTmAgentShouldBeFound(shouldBeFound);
        defaultTmAgentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTmAgentShouldBeFound(String filter) throws Exception {
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tmAgent.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].companyName").value(hasItem(DEFAULT_COMPANY_NAME)))
            .andExpect(jsonPath("$.[*].agentCode").value(hasItem(DEFAULT_AGENT_CODE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTmAgentShouldNotBeFound(String filter) throws Exception {
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTmAgentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
            .fullName(UPDATED_FULL_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME)
            .agentCode(UPDATED_AGENT_CODE)
            .email(UPDATED_EMAIL);

        restTmAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTmAgent.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTmAgent))
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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(put(ENTITY_API_URL_ID, tmAgent.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgent)))
            .andExpect(status().isBadRequest());

        // Validate the TmAgent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTmAgent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tmAgent.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tmAgent))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tmAgent)))
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

        partialUpdatedTmAgent.modifiedDate(UPDATED_MODIFIED_DATE);

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
            .fullName(UPDATED_FULL_NAME)
            .address(UPDATED_ADDRESS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .companyName(UPDATED_COMPANY_NAME)
            .agentCode(UPDATED_AGENT_CODE)
            .email(UPDATED_EMAIL);

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

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tmAgent.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tmAgent))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tmAgent))
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

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTmAgentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tmAgent)))
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
