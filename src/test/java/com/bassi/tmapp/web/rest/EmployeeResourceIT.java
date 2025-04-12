package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.EmployeeAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Employee;
import com.bassi.tmapp.repository.EmployeeRepository;
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
 * Integration tests for the {@link EmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmployeeResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_DESIGNATION = "AAAAAAAAAA";
    private static final String UPDATED_DESIGNATION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_JOINING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_JOINING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_JOINING_DATE = LocalDate.ofEpochDay(-1L);

    private static final String ENTITY_API_URL = "/api/employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmployeeMockMvc;

    private Employee employee;

    private Employee insertedEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createEntity() {
        return new Employee()
            .fullName(DEFAULT_FULL_NAME)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .email(DEFAULT_EMAIL)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED)
            .designation(DEFAULT_DESIGNATION)
            .joiningDate(DEFAULT_JOINING_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Employee createUpdatedEntity() {
        return new Employee()
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .designation(UPDATED_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE);
    }

    @BeforeEach
    void initTest() {
        employee = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmployee != null) {
            employeeRepository.delete(insertedEmployee);
            insertedEmployee = null;
        }
    }

    @Test
    @Transactional
    void createEmployee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Employee
        var returnedEmployee = om.readValue(
            restEmployeeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Employee.class
        );

        // Validate the Employee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertEmployeeUpdatableFieldsEquals(returnedEmployee, getPersistedEmployee(returnedEmployee));

        insertedEmployee = returnedEmployee;
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        // Create the Employee with an existing ID
        employee.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmployeeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEmployees() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].joiningDate").value(hasItem(DEFAULT_JOINING_DATE.toString())));
    }

    @Test
    @Transactional
    void getEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get the employee
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, employee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(employee.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.designation").value(DEFAULT_DESIGNATION))
            .andExpect(jsonPath("$.joiningDate").value(DEFAULT_JOINING_DATE.toString()));
    }

    @Test
    @Transactional
    void getEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        Long id = employee.getId();

        defaultEmployeeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmployeeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmployeeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmployeesByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where fullName equals to
        defaultEmployeeFiltering("fullName.equals=" + DEFAULT_FULL_NAME, "fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where fullName in
        defaultEmployeeFiltering("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME, "fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where fullName is not null
        defaultEmployeeFiltering("fullName.specified=true", "fullName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByFullNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where fullName contains
        defaultEmployeeFiltering("fullName.contains=" + DEFAULT_FULL_NAME, "fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where fullName does not contain
        defaultEmployeeFiltering("fullName.doesNotContain=" + UPDATED_FULL_NAME, "fullName.doesNotContain=" + DEFAULT_FULL_NAME);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phoneNumber equals to
        defaultEmployeeFiltering("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER, "phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phoneNumber in
        defaultEmployeeFiltering(
            "phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER,
            "phoneNumber.in=" + UPDATED_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phoneNumber is not null
        defaultEmployeeFiltering("phoneNumber.specified=true", "phoneNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phoneNumber contains
        defaultEmployeeFiltering("phoneNumber.contains=" + DEFAULT_PHONE_NUMBER, "phoneNumber.contains=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    void getAllEmployeesByPhoneNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where phoneNumber does not contain
        defaultEmployeeFiltering(
            "phoneNumber.doesNotContain=" + UPDATED_PHONE_NUMBER,
            "phoneNumber.doesNotContain=" + DEFAULT_PHONE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email equals to
        defaultEmployeeFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email in
        defaultEmployeeFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email is not null
        defaultEmployeeFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email contains
        defaultEmployeeFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where email does not contain
        defaultEmployeeFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate equals to
        defaultEmployeeFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate in
        defaultEmployeeFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate is not null
        defaultEmployeeFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate is greater than or equal to
        defaultEmployeeFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate is less than or equal to
        defaultEmployeeFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate is less than
        defaultEmployeeFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where createdDate is greater than
        defaultEmployeeFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate equals to
        defaultEmployeeFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate in
        defaultEmployeeFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate is not null
        defaultEmployeeFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate is greater than or equal to
        defaultEmployeeFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate is less than or equal to
        defaultEmployeeFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate is less than
        defaultEmployeeFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where modifiedDate is greater than
        defaultEmployeeFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where deleted equals to
        defaultEmployeeFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where deleted in
        defaultEmployeeFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllEmployeesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where deleted is not null
        defaultEmployeeFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where designation equals to
        defaultEmployeeFiltering("designation.equals=" + DEFAULT_DESIGNATION, "designation.equals=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where designation in
        defaultEmployeeFiltering(
            "designation.in=" + DEFAULT_DESIGNATION + "," + UPDATED_DESIGNATION,
            "designation.in=" + UPDATED_DESIGNATION
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where designation is not null
        defaultEmployeeFiltering("designation.specified=true", "designation.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where designation contains
        defaultEmployeeFiltering("designation.contains=" + DEFAULT_DESIGNATION, "designation.contains=" + UPDATED_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByDesignationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where designation does not contain
        defaultEmployeeFiltering("designation.doesNotContain=" + UPDATED_DESIGNATION, "designation.doesNotContain=" + DEFAULT_DESIGNATION);
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate equals to
        defaultEmployeeFiltering("joiningDate.equals=" + DEFAULT_JOINING_DATE, "joiningDate.equals=" + UPDATED_JOINING_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate in
        defaultEmployeeFiltering(
            "joiningDate.in=" + DEFAULT_JOINING_DATE + "," + UPDATED_JOINING_DATE,
            "joiningDate.in=" + UPDATED_JOINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate is not null
        defaultEmployeeFiltering("joiningDate.specified=true", "joiningDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate is greater than or equal to
        defaultEmployeeFiltering(
            "joiningDate.greaterThanOrEqual=" + DEFAULT_JOINING_DATE,
            "joiningDate.greaterThanOrEqual=" + UPDATED_JOINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate is less than or equal to
        defaultEmployeeFiltering(
            "joiningDate.lessThanOrEqual=" + DEFAULT_JOINING_DATE,
            "joiningDate.lessThanOrEqual=" + SMALLER_JOINING_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate is less than
        defaultEmployeeFiltering("joiningDate.lessThan=" + UPDATED_JOINING_DATE, "joiningDate.lessThan=" + DEFAULT_JOINING_DATE);
    }

    @Test
    @Transactional
    void getAllEmployeesByJoiningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        // Get all the employeeList where joiningDate is greater than
        defaultEmployeeFiltering("joiningDate.greaterThan=" + SMALLER_JOINING_DATE, "joiningDate.greaterThan=" + DEFAULT_JOINING_DATE);
    }

    private void defaultEmployeeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmployeeShouldBeFound(shouldBeFound);
        defaultEmployeeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmployeeShouldBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(employee.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].designation").value(hasItem(DEFAULT_DESIGNATION)))
            .andExpect(jsonPath("$.[*].joiningDate").value(hasItem(DEFAULT_JOINING_DATE.toString())));

        // Check, that the count call also returns 1
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmployeeShouldNotBeFound(String filter) throws Exception {
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmployee() throws Exception {
        // Get the employee
        restEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee
        Employee updatedEmployee = employeeRepository.findById(employee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmployee are not directly saved in db
        em.detach(updatedEmployee);
        updatedEmployee
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .designation(UPDATED_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE);

        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEmployee.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmployeeToMatchAllProperties(updatedEmployee);
    }

    @Test
    @Transactional
    void putNonExistingEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, employee.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .designation(UPDATED_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEmployee, employee), getPersistedEmployee(employee));
    }

    @Test
    @Transactional
    void fullUpdateEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the employee using partial update
        Employee partialUpdatedEmployee = new Employee();
        partialUpdatedEmployee.setId(employee.getId());

        partialUpdatedEmployee
            .fullName(UPDATED_FULL_NAME)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .email(UPDATED_EMAIL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .designation(UPDATED_DESIGNATION)
            .joiningDate(UPDATED_JOINING_DATE);

        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmployee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmployee))
            )
            .andExpect(status().isOk());

        // Validate the Employee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmployeeUpdatableFieldsEquals(partialUpdatedEmployee, getPersistedEmployee(partialUpdatedEmployee));
    }

    @Test
    @Transactional
    void patchNonExistingEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, employee.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(employee))
            )
            .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        employee.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmployeeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(employee)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Employee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        insertedEmployee = employeeRepository.saveAndFlush(employee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the employee
        restEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, employee.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return employeeRepository.count();
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

    protected Employee getPersistedEmployee(Employee employee) {
        return employeeRepository.findById(employee.getId()).orElseThrow();
    }

    protected void assertPersistedEmployeeToMatchAllProperties(Employee expectedEmployee) {
        assertEmployeeAllPropertiesEquals(expectedEmployee, getPersistedEmployee(expectedEmployee));
    }

    protected void assertPersistedEmployeeToMatchUpdatableProperties(Employee expectedEmployee) {
        assertEmployeeAllUpdatablePropertiesEquals(expectedEmployee, getPersistedEmployee(expectedEmployee));
    }
}
