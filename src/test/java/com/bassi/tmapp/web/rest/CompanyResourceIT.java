package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.CompanyAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Company;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.repository.CompanyRepository;
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
 * Integration tests for the {@link CompanyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompanyResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_GSTIN = "AAAAAAAAAA";
    private static final String UPDATED_GSTIN = "BBBBBBBBBB";

    private static final String DEFAULT_NATURE_OF_BUSINESS = "AAAAAAAAAA";
    private static final String UPDATED_NATURE_OF_BUSINESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_STATE = "AAAAAAAAAA";
    private static final String UPDATED_STATE = "BBBBBBBBBB";

    private static final String DEFAULT_PINCODE = "AAAAAAAAAA";
    private static final String UPDATED_PINCODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/companies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompanyMockMvc;

    private Company company;

    private Company insertedCompany;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createEntity() {
        return new Company()
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .cin(DEFAULT_CIN)
            .gstin(DEFAULT_GSTIN)
            .natureOfBusiness(DEFAULT_NATURE_OF_BUSINESS)
            .address(DEFAULT_ADDRESS)
            .state(DEFAULT_STATE)
            .pincode(DEFAULT_PINCODE)
            .city(DEFAULT_CITY)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Company createUpdatedEntity() {
        return new Company()
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .cin(UPDATED_CIN)
            .gstin(UPDATED_GSTIN)
            .natureOfBusiness(UPDATED_NATURE_OF_BUSINESS)
            .address(UPDATED_ADDRESS)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);
    }

    @BeforeEach
    void initTest() {
        company = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompany != null) {
            companyRepository.delete(insertedCompany);
            insertedCompany = null;
        }
    }

    @Test
    @Transactional
    void createCompany() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Company
        var returnedCompany = om.readValue(
            restCompanyMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Company.class
        );

        // Validate the Company in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCompanyUpdatableFieldsEquals(returnedCompany, getPersistedCompany(returnedCompany));

        insertedCompany = returnedCompany;
    }

    @Test
    @Transactional
    void createCompanyWithExistingId() throws Exception {
        // Create the Company with an existing ID
        company.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompanyMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompanies() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].gstin").value(hasItem(DEFAULT_GSTIN)))
            .andExpect(jsonPath("$.[*].natureOfBusiness").value(hasItem(DEFAULT_NATURE_OF_BUSINESS)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    void getCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get the company
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL_ID, company.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(company.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN))
            .andExpect(jsonPath("$.gstin").value(DEFAULT_GSTIN))
            .andExpect(jsonPath("$.natureOfBusiness").value(DEFAULT_NATURE_OF_BUSINESS))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE))
            .andExpect(jsonPath("$.pincode").value(DEFAULT_PINCODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    void getCompaniesByIdFiltering() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        Long id = company.getId();

        defaultCompanyFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCompanyFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCompanyFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where type equals to
        defaultCompanyFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where type in
        defaultCompanyFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where type is not null
        defaultCompanyFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where type contains
        defaultCompanyFiltering("type.contains=" + DEFAULT_TYPE, "type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllCompaniesByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where type does not contain
        defaultCompanyFiltering("type.doesNotContain=" + UPDATED_TYPE, "type.doesNotContain=" + DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where name equals to
        defaultCompanyFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where name in
        defaultCompanyFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where name is not null
        defaultCompanyFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where name contains
        defaultCompanyFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where name does not contain
        defaultCompanyFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllCompaniesByCinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where cin equals to
        defaultCompanyFiltering("cin.equals=" + DEFAULT_CIN, "cin.equals=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByCinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where cin in
        defaultCompanyFiltering("cin.in=" + DEFAULT_CIN + "," + UPDATED_CIN, "cin.in=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByCinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where cin is not null
        defaultCompanyFiltering("cin.specified=true", "cin.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCinContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where cin contains
        defaultCompanyFiltering("cin.contains=" + DEFAULT_CIN, "cin.contains=" + UPDATED_CIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByCinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where cin does not contain
        defaultCompanyFiltering("cin.doesNotContain=" + UPDATED_CIN, "cin.doesNotContain=" + DEFAULT_CIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByGstinIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where gstin equals to
        defaultCompanyFiltering("gstin.equals=" + DEFAULT_GSTIN, "gstin.equals=" + UPDATED_GSTIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByGstinIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where gstin in
        defaultCompanyFiltering("gstin.in=" + DEFAULT_GSTIN + "," + UPDATED_GSTIN, "gstin.in=" + UPDATED_GSTIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByGstinIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where gstin is not null
        defaultCompanyFiltering("gstin.specified=true", "gstin.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByGstinContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where gstin contains
        defaultCompanyFiltering("gstin.contains=" + DEFAULT_GSTIN, "gstin.contains=" + UPDATED_GSTIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByGstinNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where gstin does not contain
        defaultCompanyFiltering("gstin.doesNotContain=" + UPDATED_GSTIN, "gstin.doesNotContain=" + DEFAULT_GSTIN);
    }

    @Test
    @Transactional
    void getAllCompaniesByNatureOfBusinessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where natureOfBusiness equals to
        defaultCompanyFiltering(
            "natureOfBusiness.equals=" + DEFAULT_NATURE_OF_BUSINESS,
            "natureOfBusiness.equals=" + UPDATED_NATURE_OF_BUSINESS
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByNatureOfBusinessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where natureOfBusiness in
        defaultCompanyFiltering(
            "natureOfBusiness.in=" + DEFAULT_NATURE_OF_BUSINESS + "," + UPDATED_NATURE_OF_BUSINESS,
            "natureOfBusiness.in=" + UPDATED_NATURE_OF_BUSINESS
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByNatureOfBusinessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where natureOfBusiness is not null
        defaultCompanyFiltering("natureOfBusiness.specified=true", "natureOfBusiness.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByNatureOfBusinessContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where natureOfBusiness contains
        defaultCompanyFiltering(
            "natureOfBusiness.contains=" + DEFAULT_NATURE_OF_BUSINESS,
            "natureOfBusiness.contains=" + UPDATED_NATURE_OF_BUSINESS
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByNatureOfBusinessNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where natureOfBusiness does not contain
        defaultCompanyFiltering(
            "natureOfBusiness.doesNotContain=" + UPDATED_NATURE_OF_BUSINESS,
            "natureOfBusiness.doesNotContain=" + DEFAULT_NATURE_OF_BUSINESS
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where address equals to
        defaultCompanyFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where address in
        defaultCompanyFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where address is not null
        defaultCompanyFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where address contains
        defaultCompanyFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where address does not contain
        defaultCompanyFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where state equals to
        defaultCompanyFiltering("state.equals=" + DEFAULT_STATE, "state.equals=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where state in
        defaultCompanyFiltering("state.in=" + DEFAULT_STATE + "," + UPDATED_STATE, "state.in=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where state is not null
        defaultCompanyFiltering("state.specified=true", "state.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByStateContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where state contains
        defaultCompanyFiltering("state.contains=" + DEFAULT_STATE, "state.contains=" + UPDATED_STATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByStateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where state does not contain
        defaultCompanyFiltering("state.doesNotContain=" + UPDATED_STATE, "state.doesNotContain=" + DEFAULT_STATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByPincodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where pincode equals to
        defaultCompanyFiltering("pincode.equals=" + DEFAULT_PINCODE, "pincode.equals=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByPincodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where pincode in
        defaultCompanyFiltering("pincode.in=" + DEFAULT_PINCODE + "," + UPDATED_PINCODE, "pincode.in=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByPincodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where pincode is not null
        defaultCompanyFiltering("pincode.specified=true", "pincode.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByPincodeContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where pincode contains
        defaultCompanyFiltering("pincode.contains=" + DEFAULT_PINCODE, "pincode.contains=" + UPDATED_PINCODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByPincodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where pincode does not contain
        defaultCompanyFiltering("pincode.doesNotContain=" + UPDATED_PINCODE, "pincode.doesNotContain=" + DEFAULT_PINCODE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where city equals to
        defaultCompanyFiltering("city.equals=" + DEFAULT_CITY, "city.equals=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where city in
        defaultCompanyFiltering("city.in=" + DEFAULT_CITY + "," + UPDATED_CITY, "city.in=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where city is not null
        defaultCompanyFiltering("city.specified=true", "city.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCityContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where city contains
        defaultCompanyFiltering("city.contains=" + DEFAULT_CITY, "city.contains=" + UPDATED_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCityNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where city does not contain
        defaultCompanyFiltering("city.doesNotContain=" + UPDATED_CITY, "city.doesNotContain=" + DEFAULT_CITY);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate equals to
        defaultCompanyFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate in
        defaultCompanyFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate is not null
        defaultCompanyFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate is greater than or equal to
        defaultCompanyFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate is less than or equal to
        defaultCompanyFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate is less than
        defaultCompanyFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where createdDate is greater than
        defaultCompanyFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate equals to
        defaultCompanyFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate in
        defaultCompanyFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate is not null
        defaultCompanyFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate is greater than or equal to
        defaultCompanyFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate is less than or equal to
        defaultCompanyFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate is less than
        defaultCompanyFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where modifiedDate is greater than
        defaultCompanyFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllCompaniesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where deleted equals to
        defaultCompanyFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllCompaniesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where deleted in
        defaultCompanyFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllCompaniesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        // Get all the companyList where deleted is not null
        defaultCompanyFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllCompaniesByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            companyRepository.saveAndFlush(company);
            user = UserProfileResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        company.setUser(user);
        companyRepository.saveAndFlush(company);
        Long userId = user.getId();
        // Get all the companyList where user equals to userId
        defaultCompanyShouldBeFound("userId.equals=" + userId);

        // Get all the companyList where user equals to (userId + 1)
        defaultCompanyShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultCompanyFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCompanyShouldBeFound(shouldBeFound);
        defaultCompanyShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCompanyShouldBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(company.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN)))
            .andExpect(jsonPath("$.[*].gstin").value(hasItem(DEFAULT_GSTIN)))
            .andExpect(jsonPath("$.[*].natureOfBusiness").value(hasItem(DEFAULT_NATURE_OF_BUSINESS)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE)))
            .andExpect(jsonPath("$.[*].pincode").value(hasItem(DEFAULT_PINCODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));

        // Check, that the count call also returns 1
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCompanyShouldNotBeFound(String filter) throws Exception {
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCompanyMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCompany() throws Exception {
        // Get the company
        restCompanyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company
        Company updatedCompany = companyRepository.findById(company.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompany are not directly saved in db
        em.detach(updatedCompany);
        updatedCompany
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .cin(UPDATED_CIN)
            .gstin(UPDATED_GSTIN)
            .natureOfBusiness(UPDATED_NATURE_OF_BUSINESS)
            .address(UPDATED_ADDRESS)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);

        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompany.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompanyToMatchAllProperties(updatedCompany);
    }

    @Test
    @Transactional
    void putNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL_ID, company.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .cin(UPDATED_CIN)
            .gstin(UPDATED_GSTIN)
            .natureOfBusiness(UPDATED_NATURE_OF_BUSINESS)
            .city(UPDATED_CITY)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCompany, company), getPersistedCompany(company));
    }

    @Test
    @Transactional
    void fullUpdateCompanyWithPatch() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the company using partial update
        Company partialUpdatedCompany = new Company();
        partialUpdatedCompany.setId(company.getId());

        partialUpdatedCompany
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .cin(UPDATED_CIN)
            .gstin(UPDATED_GSTIN)
            .natureOfBusiness(UPDATED_NATURE_OF_BUSINESS)
            .address(UPDATED_ADDRESS)
            .state(UPDATED_STATE)
            .pincode(UPDATED_PINCODE)
            .city(UPDATED_CITY)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);

        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompany.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompany))
            )
            .andExpect(status().isOk());

        // Validate the Company in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompanyUpdatableFieldsEquals(partialUpdatedCompany, getPersistedCompany(partialUpdatedCompany));
    }

    @Test
    @Transactional
    void patchNonExistingCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, company.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(company))
            )
            .andExpect(status().isBadRequest());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompany() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        company.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompanyMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(company)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Company in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompany() throws Exception {
        // Initialize the database
        insertedCompany = companyRepository.saveAndFlush(company);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the company
        restCompanyMockMvc
            .perform(delete(ENTITY_API_URL_ID, company.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return companyRepository.count();
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

    protected Company getPersistedCompany(Company company) {
        return companyRepository.findById(company.getId()).orElseThrow();
    }

    protected void assertPersistedCompanyToMatchAllProperties(Company expectedCompany) {
        assertCompanyAllPropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }

    protected void assertPersistedCompanyToMatchUpdatableProperties(Company expectedCompany) {
        assertCompanyAllUpdatablePropertiesEquals(expectedCompany, getPersistedCompany(expectedCompany));
    }
}
