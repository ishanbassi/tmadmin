package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkClassAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.repository.TrademarkClassRepository;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.mapper.TrademarkClassMapper;
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
 * Integration tests for the {@link TrademarkClassResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrademarkClassResourceIT {

    private static final Integer DEFAULT_CODE = 1;
    private static final Integer UPDATED_CODE = 2;
    private static final Integer SMALLER_CODE = 1 - 1;

    private static final Integer DEFAULT_TM_CLASS = 1;
    private static final Integer UPDATED_TM_CLASS = 2;
    private static final Integer SMALLER_TM_CLASS = 1 - 1;

    private static final String DEFAULT_KEYWORD = "AAAAAAAAAA";
    private static final String UPDATED_KEYWORD = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/trademark-classes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TrademarkClassRepository trademarkClassRepository;

    @Autowired
    private TrademarkClassMapper trademarkClassMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrademarkClassMockMvc;

    private TrademarkClass trademarkClass;

    private TrademarkClass insertedTrademarkClass;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrademarkClass createEntity() {
        return new TrademarkClass()
            .code(DEFAULT_CODE)
            .tmClass(DEFAULT_TM_CLASS)
            .keyword(DEFAULT_KEYWORD)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
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
    public static TrademarkClass createUpdatedEntity() {
        return new TrademarkClass()
            .code(UPDATED_CODE)
            .tmClass(UPDATED_TM_CLASS)
            .keyword(UPDATED_KEYWORD)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);
    }

    @BeforeEach
    void initTest() {
        trademarkClass = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTrademarkClass != null) {
            trademarkClassRepository.delete(insertedTrademarkClass);
            insertedTrademarkClass = null;
        }
    }

    @Test
    @Transactional
    void createTrademarkClass() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);
        var returnedTrademarkClassDTO = om.readValue(
            restTrademarkClassMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkClassDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TrademarkClassDTO.class
        );

        // Validate the TrademarkClass in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTrademarkClass = trademarkClassMapper.toEntity(returnedTrademarkClassDTO);
        assertTrademarkClassUpdatableFieldsEquals(returnedTrademarkClass, getPersistedTrademarkClass(returnedTrademarkClass));

        insertedTrademarkClass = returnedTrademarkClass;
    }

    @Test
    @Transactional
    void createTrademarkClassWithExistingId() throws Exception {
        // Create the TrademarkClass with an existing ID
        trademarkClass.setId(1L);
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrademarkClassMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkClassDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTrademarkClasses() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademarkClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    void getTrademarkClass() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get the trademarkClass
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL_ID, trademarkClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trademarkClass.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.tmClass").value(DEFAULT_TM_CLASS))
            .andExpect(jsonPath("$.keyword").value(DEFAULT_KEYWORD))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    void getTrademarkClassesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        Long id = trademarkClass.getId();

        defaultTrademarkClassFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTrademarkClassFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTrademarkClassFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code equals to
        defaultTrademarkClassFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code in
        defaultTrademarkClassFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code is not null
        defaultTrademarkClassFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code is greater than or equal to
        defaultTrademarkClassFiltering("code.greaterThanOrEqual=" + DEFAULT_CODE, "code.greaterThanOrEqual=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code is less than or equal to
        defaultTrademarkClassFiltering("code.lessThanOrEqual=" + DEFAULT_CODE, "code.lessThanOrEqual=" + SMALLER_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code is less than
        defaultTrademarkClassFiltering("code.lessThan=" + UPDATED_CODE, "code.lessThan=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCodeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where code is greater than
        defaultTrademarkClassFiltering("code.greaterThan=" + SMALLER_CODE, "code.greaterThan=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass equals to
        defaultTrademarkClassFiltering("tmClass.equals=" + DEFAULT_TM_CLASS, "tmClass.equals=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass in
        defaultTrademarkClassFiltering("tmClass.in=" + DEFAULT_TM_CLASS + "," + UPDATED_TM_CLASS, "tmClass.in=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass is not null
        defaultTrademarkClassFiltering("tmClass.specified=true", "tmClass.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass is greater than or equal to
        defaultTrademarkClassFiltering("tmClass.greaterThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.greaterThanOrEqual=" + UPDATED_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass is less than or equal to
        defaultTrademarkClassFiltering("tmClass.lessThanOrEqual=" + DEFAULT_TM_CLASS, "tmClass.lessThanOrEqual=" + SMALLER_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass is less than
        defaultTrademarkClassFiltering("tmClass.lessThan=" + UPDATED_TM_CLASS, "tmClass.lessThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTmClassIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where tmClass is greater than
        defaultTrademarkClassFiltering("tmClass.greaterThan=" + SMALLER_TM_CLASS, "tmClass.greaterThan=" + DEFAULT_TM_CLASS);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByKeywordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where keyword equals to
        defaultTrademarkClassFiltering("keyword.equals=" + DEFAULT_KEYWORD, "keyword.equals=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByKeywordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where keyword in
        defaultTrademarkClassFiltering("keyword.in=" + DEFAULT_KEYWORD + "," + UPDATED_KEYWORD, "keyword.in=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByKeywordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where keyword is not null
        defaultTrademarkClassFiltering("keyword.specified=true", "keyword.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByKeywordContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where keyword contains
        defaultTrademarkClassFiltering("keyword.contains=" + DEFAULT_KEYWORD, "keyword.contains=" + UPDATED_KEYWORD);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByKeywordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where keyword does not contain
        defaultTrademarkClassFiltering("keyword.doesNotContain=" + UPDATED_KEYWORD, "keyword.doesNotContain=" + DEFAULT_KEYWORD);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where title equals to
        defaultTrademarkClassFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where title in
        defaultTrademarkClassFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where title is not null
        defaultTrademarkClassFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where title contains
        defaultTrademarkClassFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where title does not contain
        defaultTrademarkClassFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where description equals to
        defaultTrademarkClassFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where description in
        defaultTrademarkClassFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where description is not null
        defaultTrademarkClassFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where description contains
        defaultTrademarkClassFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where description does not contain
        defaultTrademarkClassFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate equals to
        defaultTrademarkClassFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate in
        defaultTrademarkClassFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate is not null
        defaultTrademarkClassFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate is greater than or equal to
        defaultTrademarkClassFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate is less than or equal to
        defaultTrademarkClassFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate is less than
        defaultTrademarkClassFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where createdDate is greater than
        defaultTrademarkClassFiltering(
            "createdDate.greaterThan=" + SMALLER_CREATED_DATE,
            "createdDate.greaterThan=" + DEFAULT_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate equals to
        defaultTrademarkClassFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate in
        defaultTrademarkClassFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate is not null
        defaultTrademarkClassFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate is greater than or equal to
        defaultTrademarkClassFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate is less than or equal to
        defaultTrademarkClassFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate is less than
        defaultTrademarkClassFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where modifiedDate is greater than
        defaultTrademarkClassFiltering(
            "modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE,
            "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where deleted equals to
        defaultTrademarkClassFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where deleted in
        defaultTrademarkClassFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllTrademarkClassesByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        // Get all the trademarkClassList where deleted is not null
        defaultTrademarkClassFiltering("deleted.specified=true", "deleted.specified=false");
    }

    private void defaultTrademarkClassFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTrademarkClassShouldBeFound(shouldBeFound);
        defaultTrademarkClassShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTrademarkClassShouldBeFound(String filter) throws Exception {
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trademarkClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].tmClass").value(hasItem(DEFAULT_TM_CLASS)))
            .andExpect(jsonPath("$.[*].keyword").value(hasItem(DEFAULT_KEYWORD)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));

        // Check, that the count call also returns 1
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTrademarkClassShouldNotBeFound(String filter) throws Exception {
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTrademarkClassMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTrademarkClass() throws Exception {
        // Get the trademarkClass
        restTrademarkClassMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTrademarkClass() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkClass
        TrademarkClass updatedTrademarkClass = trademarkClassRepository.findById(trademarkClass.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTrademarkClass are not directly saved in db
        em.detach(updatedTrademarkClass);
        updatedTrademarkClass
            .code(UPDATED_CODE)
            .tmClass(UPDATED_TM_CLASS)
            .keyword(UPDATED_KEYWORD)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(updatedTrademarkClass);

        restTrademarkClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkClassDTO))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTrademarkClassToMatchAllProperties(updatedTrademarkClass);
    }

    @Test
    @Transactional
    void putNonExistingTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trademarkClassDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(trademarkClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(trademarkClassDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrademarkClassWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkClass using partial update
        TrademarkClass partialUpdatedTrademarkClass = new TrademarkClass();
        partialUpdatedTrademarkClass.setId(trademarkClass.getId());

        partialUpdatedTrademarkClass.tmClass(UPDATED_TM_CLASS).keyword(UPDATED_KEYWORD).modifiedDate(UPDATED_MODIFIED_DATE);

        restTrademarkClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkClass))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkClass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkClassUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTrademarkClass, trademarkClass),
            getPersistedTrademarkClass(trademarkClass)
        );
    }

    @Test
    @Transactional
    void fullUpdateTrademarkClassWithPatch() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademarkClass using partial update
        TrademarkClass partialUpdatedTrademarkClass = new TrademarkClass();
        partialUpdatedTrademarkClass.setId(trademarkClass.getId());

        partialUpdatedTrademarkClass
            .code(UPDATED_CODE)
            .tmClass(UPDATED_TM_CLASS)
            .keyword(UPDATED_KEYWORD)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);

        restTrademarkClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrademarkClass.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTrademarkClass))
            )
            .andExpect(status().isOk());

        // Validate the TrademarkClass in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTrademarkClassUpdatableFieldsEquals(partialUpdatedTrademarkClass, getPersistedTrademarkClass(partialUpdatedTrademarkClass));
    }

    @Test
    @Transactional
    void patchNonExistingTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trademarkClassDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(trademarkClassDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrademarkClass() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        trademarkClass.setId(longCount.incrementAndGet());

        // Create the TrademarkClass
        TrademarkClassDTO trademarkClassDTO = trademarkClassMapper.toDto(trademarkClass);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrademarkClassMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(trademarkClassDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrademarkClass in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrademarkClass() throws Exception {
        // Initialize the database
        insertedTrademarkClass = trademarkClassRepository.saveAndFlush(trademarkClass);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the trademarkClass
        restTrademarkClassMockMvc
            .perform(delete(ENTITY_API_URL_ID, trademarkClass.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return trademarkClassRepository.count();
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

    protected TrademarkClass getPersistedTrademarkClass(TrademarkClass trademarkClass) {
        return trademarkClassRepository.findById(trademarkClass.getId()).orElseThrow();
    }

    protected void assertPersistedTrademarkClassToMatchAllProperties(TrademarkClass expectedTrademarkClass) {
        assertTrademarkClassAllPropertiesEquals(expectedTrademarkClass, getPersistedTrademarkClass(expectedTrademarkClass));
    }

    protected void assertPersistedTrademarkClassToMatchUpdatableProperties(TrademarkClass expectedTrademarkClass) {
        assertTrademarkClassAllUpdatablePropertiesEquals(expectedTrademarkClass, getPersistedTrademarkClass(expectedTrademarkClass));
    }
}
