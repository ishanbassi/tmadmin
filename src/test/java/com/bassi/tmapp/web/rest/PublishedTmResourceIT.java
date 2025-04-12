package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.PublishedTmAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
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
 * Integration tests for the {@link PublishedTmResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PublishedTmResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Long DEFAULT_APPLICATION_NO = 1L;
    private static final Long UPDATED_APPLICATION_NO = 2L;
    private static final Long SMALLER_APPLICATION_NO = 1L - 1L;

    private static final LocalDate DEFAULT_APPLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_APPLICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_AGENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AGENT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_AGENT_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PROPRIETOR_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETOR_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PROPRIETOR_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PROPRIETOR_ADDRESS = "BBBBBBBBBB";

    private static final HeadOffice DEFAULT_HEAD_OFFICE = HeadOffice.DELHI;
    private static final HeadOffice UPDATED_HEAD_OFFICE = HeadOffice.MUMBAI;

    private static final String DEFAULT_IMG_URL = "AAAAAAAAAA";
    private static final String UPDATED_IMG_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_TM_CLASS = 1;
    private static final Integer UPDATED_TM_CLASS = 2;
    private static final Integer SMALLER_TM_CLASS = 1 - 1;

    private static final Integer DEFAULT_JOURNAL_NO = 1;
    private static final Integer UPDATED_JOURNAL_NO = 2;
    private static final Integer SMALLER_JOURNAL_NO = 1 - 1;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_USAGE = "AAAAAAAAAA";
    private static final String UPDATED_USAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSOCIATED_TMS = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_TMS = "BBBBBBBBBB";

    private static final String DEFAULT_TRADEMARK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TRADEMARK_STATUS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final LocalDate DEFAULT_RENEWAL_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RENEWAL_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_RENEWAL_DATE = LocalDate.ofEpochDay(-1L);

    private static final TrademarkType DEFAULT_TYPE = TrademarkType.IMAGEMARK;
    private static final TrademarkType UPDATED_TYPE = TrademarkType.TRADEMARK;

    private static final Integer DEFAULT_PAGE_NO = 1;
    private static final Integer UPDATED_PAGE_NO = 2;
    private static final Integer SMALLER_PAGE_NO = 1 - 1;

    private static final String ENTITY_API_URL = "/api/published-tms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PublishedTmRepository publishedTmRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublishedTmMockMvc;

    private PublishedTm publishedTm;

    private PublishedTm insertedPublishedTm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublishedTm createEntity(EntityManager em) {
        PublishedTm publishedTm = new PublishedTm()
            .name(DEFAULT_NAME)
            .details(DEFAULT_DETAILS)
            .applicationNo(DEFAULT_APPLICATION_NO)
            .applicationDate(DEFAULT_APPLICATION_DATE)
            .agentName(DEFAULT_AGENT_NAME)
            .agentAddress(DEFAULT_AGENT_ADDRESS)
            .proprietorName(DEFAULT_PROPRIETOR_NAME)
            .proprietorAddress(DEFAULT_PROPRIETOR_ADDRESS)
            .headOffice(DEFAULT_HEAD_OFFICE)
            .imgUrl(DEFAULT_IMG_URL)
            .tmClass(DEFAULT_TM_CLASS)
            .journalNo(DEFAULT_JOURNAL_NO)
            .deleted(DEFAULT_DELETED)
            .usage(DEFAULT_USAGE)
            .associatedTms(DEFAULT_ASSOCIATED_TMS)
            .trademarkStatus(DEFAULT_TRADEMARK_STATUS)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .renewalDate(DEFAULT_RENEWAL_DATE)
            .type(DEFAULT_TYPE)
            .pageNo(DEFAULT_PAGE_NO);
        return publishedTm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PublishedTm createUpdatedEntity(EntityManager em) {
        PublishedTm publishedTm = new PublishedTm()
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .applicationNo(UPDATED_APPLICATION_NO)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .agentName(UPDATED_AGENT_NAME)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .proprietorAddress(UPDATED_PROPRIETOR_ADDRESS)
            .headOffice(UPDATED_HEAD_OFFICE)
            .imgUrl(UPDATED_IMG_URL)
            .tmClass(UPDATED_TM_CLASS)
            .journalNo(UPDATED_JOURNAL_NO)
            .deleted(UPDATED_DELETED)
            .usage(UPDATED_USAGE)
            .associatedTms(UPDATED_ASSOCIATED_TMS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .type(UPDATED_TYPE)
            .pageNo(UPDATED_PAGE_NO);
        return publishedTm;
    }

    @BeforeEach
    public void initTest() {
        publishedTm = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedPublishedTm != null) {
            publishedTmRepository.delete(insertedPublishedTm);
            insertedPublishedTm = null;
        }
    }

    @Test
    @Transactional
    void createPublishedTm() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PublishedTm
        var returnedPublishedTm = om.readValue(
            restPublishedTmMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTm)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PublishedTm.class
        );

        // Validate the PublishedTm in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertPublishedTmUpdatableFieldsEquals(returnedPublishedTm, getPersistedPublishedTm(returnedPublishedTm));

        insertedPublishedTm = returnedPublishedTm;
    }

    @Test
    @Transactional
    void createPublishedTmWithExistingId() throws Exception {
        // Create the PublishedTm with an existing ID
        publishedTm.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublishedTmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTm)))
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPublishedTms() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publishedTm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].applicationNo").value(hasItem(DEFAULT_APPLICATION_NO.intValue())))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].agentName").value(hasItem(DEFAULT_AGENT_NAME)))
            .andExpect(jsonPath("$.[*].agentAddress").value(hasItem(DEFAULT_AGENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].proprietorName").value(hasItem(DEFAULT_PROPRIETOR_NAME)))
            .andExpect(jsonPath("$.[*].proprietorAddress").value(hasItem(DEFAULT_PROPRIETOR_ADDRESS)))
            .andExpect(jsonPath("$.[*].headOffice").value(hasItem(DEFAULT_HEAD_OFFICE.toString())))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].journalNo").value(hasItem(DEFAULT_JOURNAL_NO)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].associatedTms").value(hasItem(DEFAULT_ASSOCIATED_TMS)))
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].renewalDate").value(hasItem(DEFAULT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)));
    }

    @Test
    @Transactional
    void getPublishedTm() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get the publishedTm
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL_ID, publishedTm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(publishedTm.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.applicationNo").value(DEFAULT_APPLICATION_NO.intValue()))
            .andExpect(jsonPath("$.applicationDate").value(DEFAULT_APPLICATION_DATE.toString()))
            .andExpect(jsonPath("$.agentName").value(DEFAULT_AGENT_NAME))
            .andExpect(jsonPath("$.agentAddress").value(DEFAULT_AGENT_ADDRESS))
            .andExpect(jsonPath("$.proprietorName").value(DEFAULT_PROPRIETOR_NAME))
            .andExpect(jsonPath("$.proprietorAddress").value(DEFAULT_PROPRIETOR_ADDRESS))
            .andExpect(jsonPath("$.headOffice").value(DEFAULT_HEAD_OFFICE.toString()))
            .andExpect(jsonPath("$.imgUrl").value(DEFAULT_IMG_URL))
            .andExpect(jsonPath("$.tmClass").value(DEFAULT_TM_CLASS))
            .andExpect(jsonPath("$.journalNo").value(DEFAULT_JOURNAL_NO))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE))
            .andExpect(jsonPath("$.associatedTms").value(DEFAULT_ASSOCIATED_TMS))
            .andExpect(jsonPath("$.trademarkStatus").value(DEFAULT_TRADEMARK_STATUS))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.renewalDate").value(DEFAULT_RENEWAL_DATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.pageNo").value(DEFAULT_PAGE_NO));
    }

    @Test
    @Transactional
    void getPublishedTmsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        Long id = publishedTm.getId();

        defaultPublishedTmFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPublishedTmFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPublishedTmFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where name equals to
        defaultPublishedTmFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where name in
        defaultPublishedTmFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where name is not null
        defaultPublishedTmFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where name contains
        defaultPublishedTmFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where name does not contain
        defaultPublishedTmFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where details equals to
        defaultPublishedTmFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where details in
        defaultPublishedTmFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where details is not null
        defaultPublishedTmFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where details contains
        defaultPublishedTmFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where details does not contain
        defaultPublishedTmFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo equals to
        defaultPublishedTmFiltering("applicationNo.equals=" + DEFAULT_APPLICATION_NO, "applicationNo.equals=" + UPDATED_APPLICATION_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo in
        defaultPublishedTmFiltering(
            "applicationNo.in=" + DEFAULT_APPLICATION_NO + "," + UPDATED_APPLICATION_NO,
            "applicationNo.in=" + UPDATED_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo is not null
        defaultPublishedTmFiltering("applicationNo.specified=true", "applicationNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo is greater than or equal to
        defaultPublishedTmFiltering(
            "applicationNo.greaterThanOrEqual=" + DEFAULT_APPLICATION_NO,
            "applicationNo.greaterThanOrEqual=" + UPDATED_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo is less than or equal to
        defaultPublishedTmFiltering(
            "applicationNo.lessThanOrEqual=" + DEFAULT_APPLICATION_NO,
            "applicationNo.lessThanOrEqual=" + SMALLER_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo is less than
        defaultPublishedTmFiltering("applicationNo.lessThan=" + UPDATED_APPLICATION_NO, "applicationNo.lessThan=" + DEFAULT_APPLICATION_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationNo is greater than
        defaultPublishedTmFiltering(
            "applicationNo.greaterThan=" + SMALLER_APPLICATION_NO,
            "applicationNo.greaterThan=" + DEFAULT_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate equals to
        defaultPublishedTmFiltering(
            "applicationDate.equals=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.equals=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate in
        defaultPublishedTmFiltering(
            "applicationDate.in=" + DEFAULT_APPLICATION_DATE + "," + UPDATED_APPLICATION_DATE,
            "applicationDate.in=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate is not null
        defaultPublishedTmFiltering("applicationDate.specified=true", "applicationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate is greater than or equal to
        defaultPublishedTmFiltering(
            "applicationDate.greaterThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.greaterThanOrEqual=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate is less than or equal to
        defaultPublishedTmFiltering(
            "applicationDate.lessThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.lessThanOrEqual=" + SMALLER_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate is less than
        defaultPublishedTmFiltering(
            "applicationDate.lessThan=" + UPDATED_APPLICATION_DATE,
            "applicationDate.lessThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByApplicationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where applicationDate is greater than
        defaultPublishedTmFiltering(
            "applicationDate.greaterThan=" + SMALLER_APPLICATION_DATE,
            "applicationDate.greaterThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentName equals to
        defaultPublishedTmFiltering("agentName.equals=" + DEFAULT_AGENT_NAME, "agentName.equals=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentName in
        defaultPublishedTmFiltering("agentName.in=" + DEFAULT_AGENT_NAME + "," + UPDATED_AGENT_NAME, "agentName.in=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentName is not null
        defaultPublishedTmFiltering("agentName.specified=true", "agentName.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentName contains
        defaultPublishedTmFiltering("agentName.contains=" + DEFAULT_AGENT_NAME, "agentName.contains=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentName does not contain
        defaultPublishedTmFiltering("agentName.doesNotContain=" + UPDATED_AGENT_NAME, "agentName.doesNotContain=" + DEFAULT_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentAddress equals to
        defaultPublishedTmFiltering("agentAddress.equals=" + DEFAULT_AGENT_ADDRESS, "agentAddress.equals=" + UPDATED_AGENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentAddress in
        defaultPublishedTmFiltering(
            "agentAddress.in=" + DEFAULT_AGENT_ADDRESS + "," + UPDATED_AGENT_ADDRESS,
            "agentAddress.in=" + UPDATED_AGENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentAddress is not null
        defaultPublishedTmFiltering("agentAddress.specified=true", "agentAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentAddress contains
        defaultPublishedTmFiltering("agentAddress.contains=" + DEFAULT_AGENT_ADDRESS, "agentAddress.contains=" + UPDATED_AGENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAgentAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where agentAddress does not contain
        defaultPublishedTmFiltering(
            "agentAddress.doesNotContain=" + UPDATED_AGENT_ADDRESS,
            "agentAddress.doesNotContain=" + DEFAULT_AGENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorName equals to
        defaultPublishedTmFiltering("proprietorName.equals=" + DEFAULT_PROPRIETOR_NAME, "proprietorName.equals=" + UPDATED_PROPRIETOR_NAME);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorName in
        defaultPublishedTmFiltering(
            "proprietorName.in=" + DEFAULT_PROPRIETOR_NAME + "," + UPDATED_PROPRIETOR_NAME,
            "proprietorName.in=" + UPDATED_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorName is not null
        defaultPublishedTmFiltering("proprietorName.specified=true", "proprietorName.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorNameContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorName contains
        defaultPublishedTmFiltering(
            "proprietorName.contains=" + DEFAULT_PROPRIETOR_NAME,
            "proprietorName.contains=" + UPDATED_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorName does not contain
        defaultPublishedTmFiltering(
            "proprietorName.doesNotContain=" + UPDATED_PROPRIETOR_NAME,
            "proprietorName.doesNotContain=" + DEFAULT_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorAddress equals to
        defaultPublishedTmFiltering(
            "proprietorAddress.equals=" + DEFAULT_PROPRIETOR_ADDRESS,
            "proprietorAddress.equals=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorAddress in
        defaultPublishedTmFiltering(
            "proprietorAddress.in=" + DEFAULT_PROPRIETOR_ADDRESS + "," + UPDATED_PROPRIETOR_ADDRESS,
            "proprietorAddress.in=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorAddress is not null
        defaultPublishedTmFiltering("proprietorAddress.specified=true", "proprietorAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorAddress contains
        defaultPublishedTmFiltering(
            "proprietorAddress.contains=" + DEFAULT_PROPRIETOR_ADDRESS,
            "proprietorAddress.contains=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByProprietorAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where proprietorAddress does not contain
        defaultPublishedTmFiltering(
            "proprietorAddress.doesNotContain=" + UPDATED_PROPRIETOR_ADDRESS,
            "proprietorAddress.doesNotContain=" + DEFAULT_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByHeadOfficeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where headOffice equals to
        defaultPublishedTmFiltering("headOffice.equals=" + DEFAULT_HEAD_OFFICE, "headOffice.equals=" + UPDATED_HEAD_OFFICE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByHeadOfficeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where headOffice in
        defaultPublishedTmFiltering(
            "headOffice.in=" + DEFAULT_HEAD_OFFICE + "," + UPDATED_HEAD_OFFICE,
            "headOffice.in=" + UPDATED_HEAD_OFFICE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByHeadOfficeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where headOffice is not null
        defaultPublishedTmFiltering("headOffice.specified=true", "headOffice.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where imgUrl equals to
        defaultPublishedTmFiltering("imgUrl.equals=" + DEFAULT_IMG_URL, "imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where imgUrl in
        defaultPublishedTmFiltering("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL, "imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where imgUrl is not null
        defaultPublishedTmFiltering("imgUrl.specified=true", "imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where imgUrl contains
        defaultPublishedTmFiltering("imgUrl.contains=" + DEFAULT_IMG_URL, "imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where imgUrl does not contain
        defaultPublishedTmFiltering("imgUrl.doesNotContain=" + UPDATED_IMG_URL, "imgUrl.doesNotContain=" + DEFAULT_IMG_URL);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass equals to
        defaultPublishedTmFiltering("tmClass.equals=" + DEFAULT_TM_CLASS, "tmClass.equals=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass in
        defaultPublishedTmFiltering("tmClass.in=" + DEFAULT_TM_CLASS + "," + UPDATED_TM_CLASS, "tmClass.in=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass is not null
        defaultPublishedTmFiltering("tmClass.specified=true", "tmClass.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass is greater than or equal to
        defaultPublishedTmFiltering("tmClass.greaterThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.greaterThanOrEqual=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass is less than or equal to
        defaultPublishedTmFiltering("tmClass.lessThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.lessThanOrEqual=" + SMALLER_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass is less than
        defaultPublishedTmFiltering("tmClass.lessThan=" + UPDATED_TM_CLASS, "tmClass.lessThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmClassIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where tmClass is greater than
        defaultPublishedTmFiltering("tmClass.greaterThan=" + SMALLER_TM_CLASS, "tmClass.greaterThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo equals to
        defaultPublishedTmFiltering("journalNo.equals=" + DEFAULT_JOURNAL_NO, "journalNo.equals=" + UPDATED_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo in
        defaultPublishedTmFiltering("journalNo.in=" + DEFAULT_JOURNAL_NO + "," + UPDATED_JOURNAL_NO, "journalNo.in=" + UPDATED_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo is not null
        defaultPublishedTmFiltering("journalNo.specified=true", "journalNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo is greater than or equal to
        defaultPublishedTmFiltering(
            "journalNo.greaterThanOrEqual=" + DEFAULT_JOURNAL_NO,
            "journalNo.greaterThanOrEqual=" + UPDATED_JOURNAL_NO
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo is less than or equal to
        defaultPublishedTmFiltering("journalNo.lessThanOrEqual=" + DEFAULT_JOURNAL_NO, "journalNo.lessThanOrEqual=" + SMALLER_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo is less than
        defaultPublishedTmFiltering("journalNo.lessThan=" + UPDATED_JOURNAL_NO, "journalNo.lessThan=" + DEFAULT_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByJournalNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where journalNo is greater than
        defaultPublishedTmFiltering("journalNo.greaterThan=" + SMALLER_JOURNAL_NO, "journalNo.greaterThan=" + DEFAULT_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where deleted equals to
        defaultPublishedTmFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where deleted in
        defaultPublishedTmFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where deleted is not null
        defaultPublishedTmFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where usage equals to
        defaultPublishedTmFiltering("usage.equals=" + DEFAULT_USAGE, "usage.equals=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where usage in
        defaultPublishedTmFiltering("usage.in=" + DEFAULT_USAGE + "," + UPDATED_USAGE, "usage.in=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where usage is not null
        defaultPublishedTmFiltering("usage.specified=true", "usage.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByUsageContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where usage contains
        defaultPublishedTmFiltering("usage.contains=" + DEFAULT_USAGE, "usage.contains=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByUsageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where usage does not contain
        defaultPublishedTmFiltering("usage.doesNotContain=" + UPDATED_USAGE, "usage.doesNotContain=" + DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAssociatedTmsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where associatedTms equals to
        defaultPublishedTmFiltering("associatedTms.equals=" + DEFAULT_ASSOCIATED_TMS, "associatedTms.equals=" + UPDATED_ASSOCIATED_TMS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAssociatedTmsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where associatedTms in
        defaultPublishedTmFiltering(
            "associatedTms.in=" + DEFAULT_ASSOCIATED_TMS + "," + UPDATED_ASSOCIATED_TMS,
            "associatedTms.in=" + UPDATED_ASSOCIATED_TMS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAssociatedTmsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where associatedTms is not null
        defaultPublishedTmFiltering("associatedTms.specified=true", "associatedTms.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAssociatedTmsContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where associatedTms contains
        defaultPublishedTmFiltering("associatedTms.contains=" + DEFAULT_ASSOCIATED_TMS, "associatedTms.contains=" + UPDATED_ASSOCIATED_TMS);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByAssociatedTmsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where associatedTms does not contain
        defaultPublishedTmFiltering(
            "associatedTms.doesNotContain=" + UPDATED_ASSOCIATED_TMS,
            "associatedTms.doesNotContain=" + DEFAULT_ASSOCIATED_TMS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTrademarkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where trademarkStatus equals to
        defaultPublishedTmFiltering(
            "trademarkStatus.equals=" + DEFAULT_TRADEMARK_STATUS,
            "trademarkStatus.equals=" + UPDATED_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTrademarkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where trademarkStatus in
        defaultPublishedTmFiltering(
            "trademarkStatus.in=" + DEFAULT_TRADEMARK_STATUS + "," + UPDATED_TRADEMARK_STATUS,
            "trademarkStatus.in=" + UPDATED_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTrademarkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where trademarkStatus is not null
        defaultPublishedTmFiltering("trademarkStatus.specified=true", "trademarkStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTrademarkStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where trademarkStatus contains
        defaultPublishedTmFiltering(
            "trademarkStatus.contains=" + DEFAULT_TRADEMARK_STATUS,
            "trademarkStatus.contains=" + UPDATED_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTrademarkStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where trademarkStatus does not contain
        defaultPublishedTmFiltering(
            "trademarkStatus.doesNotContain=" + UPDATED_TRADEMARK_STATUS,
            "trademarkStatus.doesNotContain=" + DEFAULT_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate equals to
        defaultPublishedTmFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate in
        defaultPublishedTmFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate is not null
        defaultPublishedTmFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate is greater than or equal to
        defaultPublishedTmFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate is less than or equal to
        defaultPublishedTmFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate is less than
        defaultPublishedTmFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where createdDate is greater than
        defaultPublishedTmFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate equals to
        defaultPublishedTmFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate in
        defaultPublishedTmFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate is not null
        defaultPublishedTmFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate is greater than or equal to
        defaultPublishedTmFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate is less than or equal to
        defaultPublishedTmFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate is less than
        defaultPublishedTmFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where modifiedDate is greater than
        defaultPublishedTmFiltering(
            "modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE,
            "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate equals to
        defaultPublishedTmFiltering("renewalDate.equals=" + DEFAULT_RENEWAL_DATE, "renewalDate.equals=" + UPDATED_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate in
        defaultPublishedTmFiltering(
            "renewalDate.in=" + DEFAULT_RENEWAL_DATE + "," + UPDATED_RENEWAL_DATE,
            "renewalDate.in=" + UPDATED_RENEWAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate is not null
        defaultPublishedTmFiltering("renewalDate.specified=true", "renewalDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate is greater than or equal to
        defaultPublishedTmFiltering(
            "renewalDate.greaterThanOrEqual=" + DEFAULT_RENEWAL_DATE,
            "renewalDate.greaterThanOrEqual=" + UPDATED_RENEWAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate is less than or equal to
        defaultPublishedTmFiltering(
            "renewalDate.lessThanOrEqual=" + DEFAULT_RENEWAL_DATE,
            "renewalDate.lessThanOrEqual=" + SMALLER_RENEWAL_DATE
        );
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate is less than
        defaultPublishedTmFiltering("renewalDate.lessThan=" + UPDATED_RENEWAL_DATE, "renewalDate.lessThan=" + DEFAULT_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByRenewalDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where renewalDate is greater than
        defaultPublishedTmFiltering("renewalDate.greaterThan=" + SMALLER_RENEWAL_DATE, "renewalDate.greaterThan=" + DEFAULT_RENEWAL_DATE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where type equals to
        defaultPublishedTmFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where type in
        defaultPublishedTmFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where type is not null
        defaultPublishedTmFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo equals to
        defaultPublishedTmFiltering("pageNo.equals=" + DEFAULT_PAGE_NO, "pageNo.equals=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo in
        defaultPublishedTmFiltering("pageNo.in=" + DEFAULT_PAGE_NO + "," + UPDATED_PAGE_NO, "pageNo.in=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo is not null
        defaultPublishedTmFiltering("pageNo.specified=true", "pageNo.specified=false");
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo is greater than or equal to
        defaultPublishedTmFiltering("pageNo.greaterThanOrEqual=" + DEFAULT_PAGE_NO, "pageNo.greaterThanOrEqual=" + UPDATED_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo is less than or equal to
        defaultPublishedTmFiltering("pageNo.lessThanOrEqual=" + DEFAULT_PAGE_NO, "pageNo.lessThanOrEqual=" + SMALLER_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo is less than
        defaultPublishedTmFiltering("pageNo.lessThan=" + UPDATED_PAGE_NO, "pageNo.lessThan=" + DEFAULT_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByPageNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        // Get all the publishedTmList where pageNo is greater than
        defaultPublishedTmFiltering("pageNo.greaterThan=" + SMALLER_PAGE_NO, "pageNo.greaterThan=" + DEFAULT_PAGE_NO);
    }

    @Test
    @Transactional
    void getAllPublishedTmsByTmAgentIsEqualToSomething() throws Exception {
        TmAgent tmAgent;
        if (TestUtil.findAll(em, TmAgent.class).isEmpty()) {
            publishedTmRepository.saveAndFlush(publishedTm);
            tmAgent = TmAgentResourceIT.createEntity(em);
        } else {
            tmAgent = TestUtil.findAll(em, TmAgent.class).get(0);
        }
        em.persist(tmAgent);
        em.flush();
        publishedTm.setTmAgent(tmAgent);
        publishedTmRepository.saveAndFlush(publishedTm);
        Long tmAgentId = tmAgent.getId();
        // Get all the publishedTmList where tmAgent equals to tmAgentId
        defaultPublishedTmShouldBeFound("tmAgentId.equals=" + tmAgentId);

        // Get all the publishedTmList where tmAgent equals to (tmAgentId + 1)
        defaultPublishedTmShouldNotBeFound("tmAgentId.equals=" + (tmAgentId + 1));
    }

    private void defaultPublishedTmFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPublishedTmShouldBeFound(shouldBeFound);
        defaultPublishedTmShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPublishedTmShouldBeFound(String filter) throws Exception {
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(publishedTm.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].applicationNo").value(hasItem(DEFAULT_APPLICATION_NO.intValue())))
            .andExpect(jsonPath("$.[*].applicationDate").value(hasItem(DEFAULT_APPLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].agentName").value(hasItem(DEFAULT_AGENT_NAME)))
            .andExpect(jsonPath("$.[*].agentAddress").value(hasItem(DEFAULT_AGENT_ADDRESS)))
            .andExpect(jsonPath("$.[*].proprietorName").value(hasItem(DEFAULT_PROPRIETOR_NAME)))
            .andExpect(jsonPath("$.[*].proprietorAddress").value(hasItem(DEFAULT_PROPRIETOR_ADDRESS)))
            .andExpect(jsonPath("$.[*].headOffice").value(hasItem(DEFAULT_HEAD_OFFICE.toString())))
            .andExpect(jsonPath("$.[*].imgUrl").value(hasItem(DEFAULT_IMG_URL)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].journalNo").value(hasItem(DEFAULT_JOURNAL_NO)))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].associatedTms").value(hasItem(DEFAULT_ASSOCIATED_TMS)))
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].renewalDate").value(hasItem(DEFAULT_RENEWAL_DATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].pageNo").value(hasItem(DEFAULT_PAGE_NO)));

        // Check, that the count call also returns 1
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPublishedTmShouldNotBeFound(String filter) throws Exception {
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPublishedTmMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPublishedTm() throws Exception {
        // Get the publishedTm
        restPublishedTmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPublishedTm() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTm
        PublishedTm updatedPublishedTm = publishedTmRepository.findById(publishedTm.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPublishedTm are not directly saved in db
        em.detach(updatedPublishedTm);
        updatedPublishedTm
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .applicationNo(UPDATED_APPLICATION_NO)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .agentName(UPDATED_AGENT_NAME)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .proprietorAddress(UPDATED_PROPRIETOR_ADDRESS)
            .headOffice(UPDATED_HEAD_OFFICE)
            .imgUrl(UPDATED_IMG_URL)
            .tmClass(UPDATED_TM_CLASS)
            .journalNo(UPDATED_JOURNAL_NO)
            .deleted(UPDATED_DELETED)
            .usage(UPDATED_USAGE)
            .associatedTms(UPDATED_ASSOCIATED_TMS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .type(UPDATED_TYPE)
            .pageNo(UPDATED_PAGE_NO);

        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPublishedTm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedPublishedTm))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPublishedTmToMatchAllProperties(updatedPublishedTm);
    }

    @Test
    @Transactional
    void putNonExistingPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publishedTm.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublishedTmWithPatch() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTm using partial update
        PublishedTm partialUpdatedPublishedTm = new PublishedTm();
        partialUpdatedPublishedTm.setId(publishedTm.getId());

        partialUpdatedPublishedTm
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .applicationNo(UPDATED_APPLICATION_NO)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .agentName(UPDATED_AGENT_NAME)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .proprietorAddress(UPDATED_PROPRIETOR_ADDRESS)
            .tmClass(UPDATED_TM_CLASS)
            .journalNo(UPDATED_JOURNAL_NO)
            .deleted(UPDATED_DELETED)
            .associatedTms(UPDATED_ASSOCIATED_TMS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .pageNo(UPDATED_PAGE_NO);

        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublishedTm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublishedTm))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublishedTmUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPublishedTm, publishedTm),
            getPersistedPublishedTm(publishedTm)
        );
    }

    @Test
    @Transactional
    void fullUpdatePublishedTmWithPatch() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTm using partial update
        PublishedTm partialUpdatedPublishedTm = new PublishedTm();
        partialUpdatedPublishedTm.setId(publishedTm.getId());

        partialUpdatedPublishedTm
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .applicationNo(UPDATED_APPLICATION_NO)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .agentName(UPDATED_AGENT_NAME)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .proprietorAddress(UPDATED_PROPRIETOR_ADDRESS)
            .headOffice(UPDATED_HEAD_OFFICE)
            .imgUrl(UPDATED_IMG_URL)
            .tmClass(UPDATED_TM_CLASS)
            .journalNo(UPDATED_JOURNAL_NO)
            .deleted(UPDATED_DELETED)
            .usage(UPDATED_USAGE)
            .associatedTms(UPDATED_ASSOCIATED_TMS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .renewalDate(UPDATED_RENEWAL_DATE)
            .type(UPDATED_TYPE)
            .pageNo(UPDATED_PAGE_NO);

        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPublishedTm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPublishedTm))
            )
            .andExpect(status().isOk());

        // Validate the PublishedTm in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPublishedTmUpdatableFieldsEquals(partialUpdatedPublishedTm, getPersistedPublishedTm(partialUpdatedPublishedTm));
    }

    @Test
    @Transactional
    void patchNonExistingPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publishedTm.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTm))
            )
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPublishedTm() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        publishedTm.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(publishedTm)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublishedTm() throws Exception {
        // Initialize the database
        insertedPublishedTm = publishedTmRepository.saveAndFlush(publishedTm);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the publishedTm
        restPublishedTmMockMvc
            .perform(delete(ENTITY_API_URL_ID, publishedTm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return publishedTmRepository.count();
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

    protected PublishedTm getPersistedPublishedTm(PublishedTm publishedTm) {
        return publishedTmRepository.findById(publishedTm.getId()).orElseThrow();
    }

    protected void assertPersistedPublishedTmToMatchAllProperties(PublishedTm expectedPublishedTm) {
        assertPublishedTmAllPropertiesEquals(expectedPublishedTm, getPersistedPublishedTm(expectedPublishedTm));
    }

    protected void assertPersistedPublishedTmToMatchUpdatableProperties(PublishedTm expectedPublishedTm) {
        assertPublishedTmAllUpdatablePropertiesEquals(expectedPublishedTm, getPersistedPublishedTm(expectedPublishedTm));
    }
}
