package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
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
 * Integration tests for the {@link TrademarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrademarkResourceIT {

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

    private static final TrademarkStatus DEFAULT_TRADEMARK_STATUS = TrademarkStatus.REGISTERED;
    private static final TrademarkStatus UPDATED_TRADEMARK_STATUS = TrademarkStatus.OPPOSED;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String ENTITY_API_URL = "/api/trademarks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrademarkRepository trademarkRepository;

    @Autowired
    private TrademarkMapper trademarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrademarkMockMvc;

    private Trademark trademark;

    private Trademark insertedTrademark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trademark createEntity(EntityManager em) {
        Trademark trademark = new Trademark()
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
            .modifiedDate(DEFAULT_MODIFIED_DATE);
        return trademark;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trademark createUpdatedEntity(EntityManager em) {
        Trademark trademark = new Trademark()
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
            .modifiedDate(UPDATED_MODIFIED_DATE);
        return trademark;
    }

    @BeforeEach
    public void initTest() {
        trademark = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTrademark != null) {
            trademarkRepository.delete(insertedTrademark);
            insertedTrademark = null;
        }
    }

    @Test
    @Transactional
    void createTrademark() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);
        var returnedTrademarkDTO = om.readValue(
            restTrademarkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrademarkDTO.class
        );

        // Validate the Trademark in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrademark = trademarkMapper.toEntity(returnedTrademarkDTO);
        assertTrademarkUpdatableFieldsEquals(returnedTrademark, getPersistedTrademark(returnedTrademark));

        insertedTrademark = returnedTrademark;
    }

    @Test
    @Transactional
    void createTrademarkWithExistingId() throws Exception {
        // Create the Trademark with an existing ID
        trademark.setId(1L);
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrademarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrademarks() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademark.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getTrademark() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get the trademark
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL_ID, trademark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trademark.getId().intValue()))
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
            .andExpect(jsonPath("$.trademarkStatus").value(DEFAULT_TRADEMARK_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getTrademarksByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        Long id = trademark.getId();

        defaultTrademarkFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrademarkFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrademarkFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrademarksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where name equals to
        defaultTrademarkFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where name in
        defaultTrademarkFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where name is not null
        defaultTrademarkFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where name contains
        defaultTrademarkFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where name does not contain
        defaultTrademarkFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByDetailsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where details equals to
        defaultTrademarkFiltering("details.equals=" + DEFAULT_DETAILS, "details.equals=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllTrademarksByDetailsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where details in
        defaultTrademarkFiltering("details.in=" + DEFAULT_DETAILS + "," + UPDATED_DETAILS, "details.in=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllTrademarksByDetailsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where details is not null
        defaultTrademarkFiltering("details.specified=true", "details.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByDetailsContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where details contains
        defaultTrademarkFiltering("details.contains=" + DEFAULT_DETAILS, "details.contains=" + UPDATED_DETAILS);
    }

    @Test
    @Transactional
    void getAllTrademarksByDetailsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where details does not contain
        defaultTrademarkFiltering("details.doesNotContain=" + UPDATED_DETAILS, "details.doesNotContain=" + DEFAULT_DETAILS);
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo equals to
        defaultTrademarkFiltering("applicationNo.equals=" + DEFAULT_APPLICATION_NO, "applicationNo.equals=" + UPDATED_APPLICATION_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo in
        defaultTrademarkFiltering(
            "applicationNo.in=" + DEFAULT_APPLICATION_NO + "," + UPDATED_APPLICATION_NO,
            "applicationNo.in=" + UPDATED_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo is not null
        defaultTrademarkFiltering("applicationNo.specified=true", "applicationNo.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo is greater than or equal to
        defaultTrademarkFiltering(
            "applicationNo.greaterThanOrEqual=" + DEFAULT_APPLICATION_NO,
            "applicationNo.greaterThanOrEqual=" + UPDATED_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo is less than or equal to
        defaultTrademarkFiltering(
            "applicationNo.lessThanOrEqual=" + DEFAULT_APPLICATION_NO,
            "applicationNo.lessThanOrEqual=" + SMALLER_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo is less than
        defaultTrademarkFiltering("applicationNo.lessThan=" + UPDATED_APPLICATION_NO, "applicationNo.lessThan=" + DEFAULT_APPLICATION_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationNo is greater than
        defaultTrademarkFiltering(
            "applicationNo.greaterThan=" + SMALLER_APPLICATION_NO,
            "applicationNo.greaterThan=" + DEFAULT_APPLICATION_NO
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate equals to
        defaultTrademarkFiltering(
            "applicationDate.equals=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.equals=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate in
        defaultTrademarkFiltering(
            "applicationDate.in=" + DEFAULT_APPLICATION_DATE + "," + UPDATED_APPLICATION_DATE,
            "applicationDate.in=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate is not null
        defaultTrademarkFiltering("applicationDate.specified=true", "applicationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate is greater than or equal to
        defaultTrademarkFiltering(
            "applicationDate.greaterThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.greaterThanOrEqual=" + UPDATED_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate is less than or equal to
        defaultTrademarkFiltering(
            "applicationDate.lessThanOrEqual=" + DEFAULT_APPLICATION_DATE,
            "applicationDate.lessThanOrEqual=" + SMALLER_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate is less than
        defaultTrademarkFiltering(
            "applicationDate.lessThan=" + UPDATED_APPLICATION_DATE,
            "applicationDate.lessThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByApplicationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where applicationDate is greater than
        defaultTrademarkFiltering(
            "applicationDate.greaterThan=" + SMALLER_APPLICATION_DATE,
            "applicationDate.greaterThan=" + DEFAULT_APPLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentName equals to
        defaultTrademarkFiltering("agentName.equals=" + DEFAULT_AGENT_NAME, "agentName.equals=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentName in
        defaultTrademarkFiltering("agentName.in=" + DEFAULT_AGENT_NAME + "," + UPDATED_AGENT_NAME, "agentName.in=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentName is not null
        defaultTrademarkFiltering("agentName.specified=true", "agentName.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentName contains
        defaultTrademarkFiltering("agentName.contains=" + DEFAULT_AGENT_NAME, "agentName.contains=" + UPDATED_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentName does not contain
        defaultTrademarkFiltering("agentName.doesNotContain=" + UPDATED_AGENT_NAME, "agentName.doesNotContain=" + DEFAULT_AGENT_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentAddress equals to
        defaultTrademarkFiltering("agentAddress.equals=" + DEFAULT_AGENT_ADDRESS, "agentAddress.equals=" + UPDATED_AGENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentAddress in
        defaultTrademarkFiltering(
            "agentAddress.in=" + DEFAULT_AGENT_ADDRESS + "," + UPDATED_AGENT_ADDRESS,
            "agentAddress.in=" + UPDATED_AGENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentAddress is not null
        defaultTrademarkFiltering("agentAddress.specified=true", "agentAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentAddress contains
        defaultTrademarkFiltering("agentAddress.contains=" + DEFAULT_AGENT_ADDRESS, "agentAddress.contains=" + UPDATED_AGENT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllTrademarksByAgentAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where agentAddress does not contain
        defaultTrademarkFiltering(
            "agentAddress.doesNotContain=" + UPDATED_AGENT_ADDRESS,
            "agentAddress.doesNotContain=" + DEFAULT_AGENT_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorName equals to
        defaultTrademarkFiltering("proprietorName.equals=" + DEFAULT_PROPRIETOR_NAME, "proprietorName.equals=" + UPDATED_PROPRIETOR_NAME);
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorName in
        defaultTrademarkFiltering(
            "proprietorName.in=" + DEFAULT_PROPRIETOR_NAME + "," + UPDATED_PROPRIETOR_NAME,
            "proprietorName.in=" + UPDATED_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorName is not null
        defaultTrademarkFiltering("proprietorName.specified=true", "proprietorName.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorName contains
        defaultTrademarkFiltering(
            "proprietorName.contains=" + DEFAULT_PROPRIETOR_NAME,
            "proprietorName.contains=" + UPDATED_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorName does not contain
        defaultTrademarkFiltering(
            "proprietorName.doesNotContain=" + UPDATED_PROPRIETOR_NAME,
            "proprietorName.doesNotContain=" + DEFAULT_PROPRIETOR_NAME
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorAddress equals to
        defaultTrademarkFiltering(
            "proprietorAddress.equals=" + DEFAULT_PROPRIETOR_ADDRESS,
            "proprietorAddress.equals=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorAddress in
        defaultTrademarkFiltering(
            "proprietorAddress.in=" + DEFAULT_PROPRIETOR_ADDRESS + "," + UPDATED_PROPRIETOR_ADDRESS,
            "proprietorAddress.in=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorAddress is not null
        defaultTrademarkFiltering("proprietorAddress.specified=true", "proprietorAddress.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorAddress contains
        defaultTrademarkFiltering(
            "proprietorAddress.contains=" + DEFAULT_PROPRIETOR_ADDRESS,
            "proprietorAddress.contains=" + UPDATED_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByProprietorAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where proprietorAddress does not contain
        defaultTrademarkFiltering(
            "proprietorAddress.doesNotContain=" + UPDATED_PROPRIETOR_ADDRESS,
            "proprietorAddress.doesNotContain=" + DEFAULT_PROPRIETOR_ADDRESS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByHeadOfficeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where headOffice equals to
        defaultTrademarkFiltering("headOffice.equals=" + DEFAULT_HEAD_OFFICE, "headOffice.equals=" + UPDATED_HEAD_OFFICE);
    }

    @Test
    @Transactional
    void getAllTrademarksByHeadOfficeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where headOffice in
        defaultTrademarkFiltering(
            "headOffice.in=" + DEFAULT_HEAD_OFFICE + "," + UPDATED_HEAD_OFFICE,
            "headOffice.in=" + UPDATED_HEAD_OFFICE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByHeadOfficeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where headOffice is not null
        defaultTrademarkFiltering("headOffice.specified=true", "headOffice.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByImgUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where imgUrl equals to
        defaultTrademarkFiltering("imgUrl.equals=" + DEFAULT_IMG_URL, "imgUrl.equals=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllTrademarksByImgUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where imgUrl in
        defaultTrademarkFiltering("imgUrl.in=" + DEFAULT_IMG_URL + "," + UPDATED_IMG_URL, "imgUrl.in=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllTrademarksByImgUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where imgUrl is not null
        defaultTrademarkFiltering("imgUrl.specified=true", "imgUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByImgUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where imgUrl contains
        defaultTrademarkFiltering("imgUrl.contains=" + DEFAULT_IMG_URL, "imgUrl.contains=" + UPDATED_IMG_URL);
    }

    @Test
    @Transactional
    void getAllTrademarksByImgUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where imgUrl does not contain
        defaultTrademarkFiltering("imgUrl.doesNotContain=" + UPDATED_IMG_URL, "imgUrl.doesNotContain=" + DEFAULT_IMG_URL);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass equals to
        defaultTrademarkFiltering("tmClass.equals=" + DEFAULT_TM_CLASS, "tmClass.equals=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass in
        defaultTrademarkFiltering("tmClass.in=" + DEFAULT_TM_CLASS + "," + UPDATED_TM_CLASS, "tmClass.in=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass is not null
        defaultTrademarkFiltering("tmClass.specified=true", "tmClass.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass is greater than or equal to
        defaultTrademarkFiltering("tmClass.greaterThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.greaterThanOrEqual=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass is less than or equal to
        defaultTrademarkFiltering("tmClass.lessThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.lessThanOrEqual=" + SMALLER_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass is less than
        defaultTrademarkFiltering("tmClass.lessThan=" + UPDATED_TM_CLASS, "tmClass.lessThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmClassIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where tmClass is greater than
        defaultTrademarkFiltering("tmClass.greaterThan=" + SMALLER_TM_CLASS, "tmClass.greaterThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo equals to
        defaultTrademarkFiltering("journalNo.equals=" + DEFAULT_JOURNAL_NO, "journalNo.equals=" + UPDATED_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo in
        defaultTrademarkFiltering("journalNo.in=" + DEFAULT_JOURNAL_NO + "," + UPDATED_JOURNAL_NO, "journalNo.in=" + UPDATED_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo is not null
        defaultTrademarkFiltering("journalNo.specified=true", "journalNo.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo is greater than or equal to
        defaultTrademarkFiltering(
            "journalNo.greaterThanOrEqual=" + DEFAULT_JOURNAL_NO,
            "journalNo.greaterThanOrEqual=" + UPDATED_JOURNAL_NO
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo is less than or equal to
        defaultTrademarkFiltering("journalNo.lessThanOrEqual=" + DEFAULT_JOURNAL_NO, "journalNo.lessThanOrEqual=" + SMALLER_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo is less than
        defaultTrademarkFiltering("journalNo.lessThan=" + UPDATED_JOURNAL_NO, "journalNo.lessThan=" + DEFAULT_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByJournalNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where journalNo is greater than
        defaultTrademarkFiltering("journalNo.greaterThan=" + SMALLER_JOURNAL_NO, "journalNo.greaterThan=" + DEFAULT_JOURNAL_NO);
    }

    @Test
    @Transactional
    void getAllTrademarksByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where deleted equals to
        defaultTrademarkFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTrademarksByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where deleted in
        defaultTrademarkFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTrademarksByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where deleted is not null
        defaultTrademarkFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where usage equals to
        defaultTrademarkFiltering("usage.equals=" + DEFAULT_USAGE, "usage.equals=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllTrademarksByUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where usage in
        defaultTrademarkFiltering("usage.in=" + DEFAULT_USAGE + "," + UPDATED_USAGE, "usage.in=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllTrademarksByUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where usage is not null
        defaultTrademarkFiltering("usage.specified=true", "usage.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByUsageContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where usage contains
        defaultTrademarkFiltering("usage.contains=" + DEFAULT_USAGE, "usage.contains=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllTrademarksByUsageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where usage does not contain
        defaultTrademarkFiltering("usage.doesNotContain=" + UPDATED_USAGE, "usage.doesNotContain=" + DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void getAllTrademarksByAssociatedTmsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where associatedTms equals to
        defaultTrademarkFiltering("associatedTms.equals=" + DEFAULT_ASSOCIATED_TMS, "associatedTms.equals=" + UPDATED_ASSOCIATED_TMS);
    }

    @Test
    @Transactional
    void getAllTrademarksByAssociatedTmsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where associatedTms in
        defaultTrademarkFiltering(
            "associatedTms.in=" + DEFAULT_ASSOCIATED_TMS + "," + UPDATED_ASSOCIATED_TMS,
            "associatedTms.in=" + UPDATED_ASSOCIATED_TMS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByAssociatedTmsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where associatedTms is not null
        defaultTrademarkFiltering("associatedTms.specified=true", "associatedTms.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByAssociatedTmsContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where associatedTms contains
        defaultTrademarkFiltering("associatedTms.contains=" + DEFAULT_ASSOCIATED_TMS, "associatedTms.contains=" + UPDATED_ASSOCIATED_TMS);
    }

    @Test
    @Transactional
    void getAllTrademarksByAssociatedTmsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where associatedTms does not contain
        defaultTrademarkFiltering(
            "associatedTms.doesNotContain=" + UPDATED_ASSOCIATED_TMS,
            "associatedTms.doesNotContain=" + DEFAULT_ASSOCIATED_TMS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByTrademarkStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where trademarkStatus equals to
        defaultTrademarkFiltering(
            "trademarkStatus.equals=" + DEFAULT_TRADEMARK_STATUS,
            "trademarkStatus.equals=" + UPDATED_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByTrademarkStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where trademarkStatus in
        defaultTrademarkFiltering(
            "trademarkStatus.in=" + DEFAULT_TRADEMARK_STATUS + "," + UPDATED_TRADEMARK_STATUS,
            "trademarkStatus.in=" + UPDATED_TRADEMARK_STATUS
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByTrademarkStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where trademarkStatus is not null
        defaultTrademarkFiltering("trademarkStatus.specified=true", "trademarkStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate equals to
        defaultTrademarkFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate in
        defaultTrademarkFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate is not null
        defaultTrademarkFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate is greater than or equal to
        defaultTrademarkFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate is less than or equal to
        defaultTrademarkFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate is less than
        defaultTrademarkFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where createdDate is greater than
        defaultTrademarkFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate equals to
        defaultTrademarkFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate in
        defaultTrademarkFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate is not null
        defaultTrademarkFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate is greater than or equal to
        defaultTrademarkFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate is less than or equal to
        defaultTrademarkFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate is less than
        defaultTrademarkFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        // Get all the trademarkList where modifiedDate is greater than
        defaultTrademarkFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarksByTmAgentIsEqualToSomething() throws Exception {
        TmAgent tmAgent;
        if (TestUtil.findAll(em, TmAgent.class).isEmpty()) {
            trademarkRepository.saveAndFlush(trademark);
            tmAgent = TmAgentResourceIT.createEntity(em);
        } else {
            tmAgent = TestUtil.findAll(em, TmAgent.class).get(0);
        }
        em.persist(tmAgent);
        em.flush();
        trademark.setTmAgent(tmAgent);
        trademarkRepository.saveAndFlush(trademark);
        Long tmAgentId = tmAgent.getId();
        // Get all the trademarkList where tmAgent equals to tmAgentId
        defaultTrademarkShouldBeFound("tmAgentId.equals=" + tmAgentId);

        // Get all the trademarkList where tmAgent equals to (tmAgentId + 1)
        defaultTrademarkShouldNotBeFound("tmAgentId.equals=" + (tmAgentId + 1));
    }

    private void defaultTrademarkFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrademarkShouldBeFound(shouldBeFound);
        defaultTrademarkShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrademarkShouldBeFound(String filter) throws Exception {
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademark.getId().intValue())))
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
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));

        // Check, that the count call also returns 1
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrademarkShouldNotBeFound(String filter) throws Exception {
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrademarkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrademark() throws Exception {
        // Get the trademark
        restTrademarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrademark() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademark
        Trademark updatedTrademark = trademarkRepository.findById(trademark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrademark are not directly saved in db
        em.detach(updatedTrademark);
        updatedTrademark
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
            .modifiedDate(UPDATED_MODIFIED_DATE);
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(updatedTrademark);

        restTrademarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrademarkToMatchAllProperties(updatedTrademark);
    }

    @Test
    @Transactional
    void putNonExistingTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrademarkWithPatch() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademark using partial update
        Trademark partialUpdatedTrademark = new Trademark();
        partialUpdatedTrademark.setId(trademark.getId());

        partialUpdatedTrademark
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .applicationNo(UPDATED_APPLICATION_NO)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .headOffice(UPDATED_HEAD_OFFICE)
            .tmClass(UPDATED_TM_CLASS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        restTrademarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademark))
            )
            .andExpect(status().isOk());

        // Validate the Trademark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrademark, trademark),
            getPersistedTrademark(trademark)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrademarkWithPatch() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademark using partial update
        Trademark partialUpdatedTrademark = new Trademark();
        partialUpdatedTrademark.setId(trademark.getId());

        partialUpdatedTrademark
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
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademark))
            )
            .andExpect(status().isOk());

        // Validate the Trademark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkUpdatableFieldsEquals(partialUpdatedTrademark, getPersistedTrademark(partialUpdatedTrademark));
    }

    @Test
    @Transactional
    void patchNonExistingTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trademarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrademark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademark.setId(longCount.incrementAndGet());

        // Create the Trademark
        TrademarkDTO trademarkDTO = trademarkMapper.toDto(trademark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trademarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trademark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrademark() throws Exception {
        // Initialize the database
        insertedTrademark = trademarkRepository.saveAndFlush(trademark);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trademark
        restTrademarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, trademark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trademarkRepository.count();
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

    protected Trademark getPersistedTrademark(Trademark trademark) {
        return trademarkRepository.findById(trademark.getId()).orElseThrow();
    }

    protected void assertPersistedTrademarkToMatchAllProperties(Trademark expectedTrademark) {
        assertTrademarkAllPropertiesEquals(expectedTrademark, getPersistedTrademark(expectedTrademark));
    }

    protected void assertPersistedTrademarkToMatchUpdatableProperties(Trademark expectedTrademark) {
        assertTrademarkAllUpdatablePropertiesEquals(expectedTrademark, getPersistedTrademark(expectedTrademark));
    }
}
