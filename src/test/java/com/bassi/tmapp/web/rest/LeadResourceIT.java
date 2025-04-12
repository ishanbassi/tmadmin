package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.LeadAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Employee;
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.enumeration.ContactMethod;
import com.bassi.tmapp.domain.enumeration.LeadStatus;
import com.bassi.tmapp.repository.LeadRepository;
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
 * Integration tests for the {@link LeadResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LeadResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_BRAND_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BRAND_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SELECTED_PACKAGE = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_PACKAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TM_CLASS = 1;
    private static final Integer UPDATED_TM_CLASS = 2;
    private static final Integer SMALLER_TM_CLASS = 1 - 1;

    private static final String DEFAULT_COMMENTS = "AAAAAAAAAA";
    private static final String UPDATED_COMMENTS = "BBBBBBBBBB";

    private static final ContactMethod DEFAULT_CONTACT_METHOD = ContactMethod.CALL;
    private static final ContactMethod UPDATED_CONTACT_METHOD = ContactMethod.MESSAGE;

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final LeadStatus DEFAULT_STATUS = LeadStatus.CONVERTED;
    private static final LeadStatus UPDATED_STATUS = LeadStatus.NEW;

    private static final String DEFAULT_LEAD_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_LEAD_SOURCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/leads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLeadMockMvc;

    private Lead lead;

    private Lead insertedLead;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createEntity() {
        return new Lead()
            .fullName(DEFAULT_FULL_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .city(DEFAULT_CITY)
            .brandName(DEFAULT_BRAND_NAME)
            .selectedPackage(DEFAULT_SELECTED_PACKAGE)
            .tmClass(DEFAULT_TM_CLASS)
            .comments(DEFAULT_COMMENTS)
            .contactMethod(DEFAULT_CONTACT_METHOD)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED)
            .status(DEFAULT_STATUS)
            .leadSource(DEFAULT_LEAD_SOURCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Lead createUpdatedEntity() {
        return new Lead()
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .city(UPDATED_CITY)
            .brandName(UPDATED_BRAND_NAME)
            .selectedPackage(UPDATED_SELECTED_PACKAGE)
            .tmClass(UPDATED_TM_CLASS)
            .comments(UPDATED_COMMENTS)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS)
            .leadSource(UPDATED_LEAD_SOURCE);
    }

    @BeforeEach
    void initTest() {
        lead = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLead != null) {
            leadRepository.delete(insertedLead);
            insertedLead = null;
        }
    }

    @Test
    @Transactional
    void createLead() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Lead
        var returnedLead = om.readValue(
            restLeadMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lead)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Lead.class
        );

        // Validate the Lead in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLeadUpdatableFieldsEquals(returnedLead, getPersistedLead(returnedLead));

        insertedLead = returnedLead;
    }

    @Test
    @Transactional
    void createLeadWithExistingId() throws Exception {
        // Create the Lead with an existing ID
        lead.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLeadMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lead)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllLeads() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].selectedPackage").value(hasItem(DEFAULT_SELECTED_PACKAGE)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].contactMethod").value(hasItem(DEFAULT_CONTACT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].leadSource").value(hasItem(DEFAULT_LEAD_SOURCE)));
    }

    @Test
    @Transactional
    void getLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get the lead
        restLeadMockMvc
            .perform(get(ENTITY_API_URL_ID, lead.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lead.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.brandName").value(DEFAULT_BRAND_NAME))
            .andExpect(jsonPath("$.selectedPackage").value(DEFAULT_SELECTED_PACKAGE))
            .andExpect(jsonPath("$.tmClass").value(DEFAULT_TM_CLASS))
            .andExpect(jsonPath("$.comments").value(DEFAULT_COMMENTS))
            .andExpect(jsonPath("$.contactMethod").value(DEFAULT_CONTACT_METHOD.toString()))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.leadSource").value(DEFAULT_LEAD_SOURCE));
    }

    @Test
    @Transactional
    void getLeadsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        Long id = lead.getId();

        defaultLeadFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLeadFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLeadFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLeadsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where fullName equals to
        defaultLeadFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where fullName in
        defaultLeadFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where fullName is not null
        defaultLeadFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where fullName contains
        defaultLeadFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where fullName does not contain
        defaultLeadFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where phoneNumber equals to
        defaultLeadFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where phoneNumber in
        defaultLeadFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllLeadsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where phoneNumber is not null
        defaultLeadFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where phoneNumber contains
        defaultLeadFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where phoneNumber does not contain
        defaultLeadFiltering("phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER, "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllLeadsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where email equals to
        defaultLeadFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLeadsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where email in
        defaultLeadFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLeadsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where email is not null
        defaultLeadFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where email contains
        defaultLeadFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllLeadsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where email does not contain
        defaultLeadFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllLeadsByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where city equals to
        defaultLeadFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLeadsByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where city in
        defaultLeadFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLeadsByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where city is not null
        defaultLeadFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where city contains
        defaultLeadFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllLeadsByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where city does not contain
        defaultLeadFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllLeadsByBrandNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where brandName equals to
        defaultLeadFiltering("brandName.equals=" + DEFAULT_BRAND_NAME, "brandName.equals=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByBrandNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where brandName in
        defaultLeadFiltering("brandName.in=" + DEFAULT_BRAND_NAME + "," + UPDATED_BRAND_NAME, "brandName.in=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByBrandNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where brandName is not null
        defaultLeadFiltering("brandName.specified=true", "brandName.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByBrandNameContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where brandName contains
        defaultLeadFiltering("brandName.contains=" + DEFAULT_BRAND_NAME, "brandName.contains=" + UPDATED_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsByBrandNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where brandName does not contain
        defaultLeadFiltering("brandName.doesNotContain=" + UPDATED_BRAND_NAME, "brandName.doesNotContain=" + DEFAULT_BRAND_NAME);
    }

    @Test
    @Transactional
    void getAllLeadsBySelectedPackageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where selectedPackage equals to
        defaultLeadFiltering("selectedPackage.equals=" + DEFAULT_SELECTED_PACKAGE, "selectedPackage.equals=" + UPDATED_SELECTED_PACKAGE);
    }

    @Test
    @Transactional
    void getAllLeadsBySelectedPackageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where selectedPackage in
        defaultLeadFiltering(
            "selectedPackage.in=" + DEFAULT_SELECTED_PACKAGE + "," + UPDATED_SELECTED_PACKAGE,
            "selectedPackage.in=" + UPDATED_SELECTED_PACKAGE
        );
    }

    @Test
    @Transactional
    void getAllLeadsBySelectedPackageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where selectedPackage is not null
        defaultLeadFiltering("selectedPackage.specified=true", "selectedPackage.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsBySelectedPackageContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where selectedPackage contains
        defaultLeadFiltering(
            "selectedPackage.contains=" + DEFAULT_SELECTED_PACKAGE,
            "selectedPackage.contains=" + UPDATED_SELECTED_PACKAGE
        );
    }

    @Test
    @Transactional
    void getAllLeadsBySelectedPackageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where selectedPackage does not contain
        defaultLeadFiltering(
            "selectedPackage.doesNotContain=" + UPDATED_SELECTED_PACKAGE,
            "selectedPackage.doesNotContain=" + DEFAULT_SELECTED_PACKAGE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass equals to
        defaultLeadFiltering("tmClass.equals=" + DEFAULT_TM_CLASS, "tmClass.equals=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass in
        defaultLeadFiltering("tmClass.in=" + DEFAULT_TM_CLASS + "," + UPDATED_TM_CLASS, "tmClass.in=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass is not null
        defaultLeadFiltering("tmClass.specified=true", "tmClass.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass is greater than or equal to
        defaultLeadFiltering("tmClass.greaterThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.greaterThanOrEqual=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass is less than or equal to
        defaultLeadFiltering("tmClass.lessThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.lessThanOrEqual=" + SMALLER_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass is less than
        defaultLeadFiltering("tmClass.lessThan=" + UPDATED_TM_CLASS, "tmClass.lessThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByTmClassIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where tmClass is greater than
        defaultLeadFiltering("tmClass.greaterThan=" + SMALLER_TM_CLASS, "tmClass.greaterThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllLeadsByCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where comments equals to
        defaultLeadFiltering("comments.equals=" + DEFAULT_COMMENTS, "comments.equals=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllLeadsByCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where comments in
        defaultLeadFiltering("comments.in=" + DEFAULT_COMMENTS + "," + UPDATED_COMMENTS, "comments.in=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllLeadsByCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where comments is not null
        defaultLeadFiltering("comments.specified=true", "comments.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByCommentsContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where comments contains
        defaultLeadFiltering("comments.contains=" + DEFAULT_COMMENTS, "comments.contains=" + UPDATED_COMMENTS);
    }

    @Test
    @Transactional
    void getAllLeadsByCommentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where comments does not contain
        defaultLeadFiltering("comments.doesNotContain=" + UPDATED_COMMENTS, "comments.doesNotContain=" + DEFAULT_COMMENTS);
    }

    @Test
    @Transactional
    void getAllLeadsByContactMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where contactMethod equals to
        defaultLeadFiltering("contactMethod.equals=" + DEFAULT_CONTACT_METHOD, "contactMethod.equals=" + UPDATED_CONTACT_METHOD);
    }

    @Test
    @Transactional
    void getAllLeadsByContactMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where contactMethod in
        defaultLeadFiltering(
            "contactMethod.in=" + DEFAULT_CONTACT_METHOD + "," + UPDATED_CONTACT_METHOD,
            "contactMethod.in=" + UPDATED_CONTACT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllLeadsByContactMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where contactMethod is not null
        defaultLeadFiltering("contactMethod.specified=true", "contactMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate equals to
        defaultLeadFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate in
        defaultLeadFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate is not null
        defaultLeadFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate is greater than or equal to
        defaultLeadFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate is less than or equal to
        defaultLeadFiltering("createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE, "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate is less than
        defaultLeadFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where createdDate is greater than
        defaultLeadFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate equals to
        defaultLeadFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate in
        defaultLeadFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate is not null
        defaultLeadFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate is greater than or equal to
        defaultLeadFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate is less than or equal to
        defaultLeadFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate is less than
        defaultLeadFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where modifiedDate is greater than
        defaultLeadFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllLeadsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where deleted equals to
        defaultLeadFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllLeadsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where deleted in
        defaultLeadFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllLeadsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where deleted is not null
        defaultLeadFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status equals to
        defaultLeadFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status in
        defaultLeadFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllLeadsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where status is not null
        defaultLeadFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadSource equals to
        defaultLeadFiltering("leadSource.equals=" + DEFAULT_LEAD_SOURCE, "leadSource.equals=" + UPDATED_LEAD_SOURCE);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadSource in
        defaultLeadFiltering("leadSource.in=" + DEFAULT_LEAD_SOURCE + "," + UPDATED_LEAD_SOURCE, "leadSource.in=" + UPDATED_LEAD_SOURCE);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadSource is not null
        defaultLeadFiltering("leadSource.specified=true", "leadSource.specified=false");
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadSource contains
        defaultLeadFiltering("leadSource.contains=" + DEFAULT_LEAD_SOURCE, "leadSource.contains=" + UPDATED_LEAD_SOURCE);
    }

    @Test
    @Transactional
    void getAllLeadsByLeadSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        // Get all the leadList where leadSource does not contain
        defaultLeadFiltering("leadSource.doesNotContain=" + UPDATED_LEAD_SOURCE, "leadSource.doesNotContain=" + DEFAULT_LEAD_SOURCE);
    }

    @Test
    @Transactional
    void getAllLeadsByAssignedToIsEqualToSomething() throws Exception {
        Employee assignedTo;
        if (TestUtil.findAll(em, Employee.class).isEmpty()) {
            leadRepository.saveAndFlush(lead);
            assignedTo = EmployeeResourceIT.createEntity();
        } else {
            assignedTo = TestUtil.findAll(em, Employee.class).get(0);
        }
        em.persist(assignedTo);
        em.flush();
        lead.setAssignedTo(assignedTo);
        leadRepository.saveAndFlush(lead);
        Long assignedToId = assignedTo.getId();
        // Get all the leadList where assignedTo equals to assignedToId
        defaultLeadShouldBeFound("assignedToId.equals=" + assignedToId);

        // Get all the leadList where assignedTo equals to (assignedToId + 1)
        defaultLeadShouldNotBeFound("assignedToId.equals=" + (assignedToId + 1));
    }

    private void defaultLeadFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLeadShouldBeFound(shouldBeFound);
        defaultLeadShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLeadShouldBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lead.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].brandName").value(hasItem(DEFAULT_BRAND_NAME)))
            .andExpect(jsonPath("$.[*].selectedPackage").value(hasItem(DEFAULT_SELECTED_PACKAGE)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].comments").value(hasItem(DEFAULT_COMMENTS)))
            .andExpect(jsonPath("$.[*].contactMethod").value(hasItem(DEFAULT_CONTACT_METHOD.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].leadSource").value(hasItem(DEFAULT_LEAD_SOURCE)));

        // Check, that the count call also returns 1
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLeadShouldNotBeFound(String filter) throws Exception {
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLeadMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLead() throws Exception {
        // Get the lead
        restLeadMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead
        Lead updatedLead = leadRepository.findById(lead.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLead are not directly saved in db
        em.detach(updatedLead);
        updatedLead
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .city(UPDATED_CITY)
            .brandName(UPDATED_BRAND_NAME)
            .selectedPackage(UPDATED_SELECTED_PACKAGE)
            .tmClass(UPDATED_TM_CLASS)
            .comments(UPDATED_COMMENTS)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS)
            .leadSource(UPDATED_LEAD_SOURCE);

        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLead.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLeadToMatchAllProperties(updatedLead);
    }

    @Test
    @Transactional
    void putNonExistingLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(put(ENTITY_API_URL_ID, lead.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lead)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lead))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lead)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead
            .fullName(UPDATED_FULL_NAME)
            .comments(UPDATED_COMMENTS)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS)
            .leadSource(UPDATED_LEAD_SOURCE);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedLead, lead), getPersistedLead(lead));
    }

    @Test
    @Transactional
    void fullUpdateLeadWithPatch() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lead using partial update
        Lead partialUpdatedLead = new Lead();
        partialUpdatedLead.setId(lead.getId());

        partialUpdatedLead
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .city(UPDATED_CITY)
            .brandName(UPDATED_BRAND_NAME)
            .selectedPackage(UPDATED_SELECTED_PACKAGE)
            .tmClass(UPDATED_TM_CLASS)
            .comments(UPDATED_COMMENTS)
            .contactMethod(UPDATED_CONTACT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS)
            .leadSource(UPDATED_LEAD_SOURCE);

        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLead.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLead))
            )
            .andExpect(status().isOk());

        // Validate the Lead in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLeadUpdatableFieldsEquals(partialUpdatedLead, getPersistedLead(partialUpdatedLead));
    }

    @Test
    @Transactional
    void patchNonExistingLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(patch(ENTITY_API_URL_ID, lead.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lead)))
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lead))
            )
            .andExpect(status().isBadRequest());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLead() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lead.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLeadMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lead)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Lead in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLead() throws Exception {
        // Initialize the database
        insertedLead = leadRepository.saveAndFlush(lead);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lead
        restLeadMockMvc
            .perform(delete(ENTITY_API_URL_ID, lead.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return leadRepository.count();
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

    protected Lead getPersistedLead(Lead lead) {
        return leadRepository.findById(lead.getId()).orElseThrow();
    }

    protected void assertPersistedLeadToMatchAllProperties(Lead expectedLead) {
        assertLeadAllPropertiesEquals(expectedLead, getPersistedLead(expectedLead));
    }

    protected void assertPersistedLeadToMatchUpdatableProperties(Lead expectedLead) {
        assertLeadAllUpdatablePropertiesEquals(expectedLead, getPersistedLead(expectedLead));
    }
}
