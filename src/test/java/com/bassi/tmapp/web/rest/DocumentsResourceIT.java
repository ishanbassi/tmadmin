package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.DocumentsAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.DocumentStatus;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.bassi.tmapp.repository.DocumentsRepository;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.mapper.DocumentsMapper;
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
 * Integration tests for the {@link DocumentsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentsResourceIT {

    private static final DocumentType DEFAULT_DOCUMENT_TYPE = DocumentType.OTHERS;
    private static final DocumentType UPDATED_DOCUMENT_TYPE = DocumentType.LOGO;

    private static final String DEFAULT_FILE_CONTENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_CONTENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_URL = "AAAAAAAAAA";
    private static final String UPDATED_FILE_URL = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final DocumentStatus DEFAULT_STATUS = DocumentStatus.UPLOADED;
    private static final DocumentStatus UPDATED_STATUS = DocumentStatus.UNDER_REVIEW;

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentsRepository documentsRepository;

    @Autowired
    private DocumentsMapper documentsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentsMockMvc;

    private Documents documents;

    private Documents insertedDocuments;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documents createEntity() {
        return new Documents()
            .documentType(DEFAULT_DOCUMENT_TYPE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .fileName(DEFAULT_FILE_NAME)
            .fileUrl(DEFAULT_FILE_URL)
            .createdDate(DEFAULT_CREATED_DATE)
            .modifiedDate(DEFAULT_MODIFIED_DATE)
            .deleted(DEFAULT_DELETED)
            .status(DEFAULT_STATUS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documents createUpdatedEntity() {
        return new Documents()
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
    }

    @BeforeEach
    void initTest() {
        documents = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocuments != null) {
            documentsRepository.delete(insertedDocuments);
            insertedDocuments = null;
        }
    }

    @Test
    @Transactional
    void createDocuments() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);
        var returnedDocumentsDTO = om.readValue(
            restDocumentsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentsDTO.class
        );

        // Validate the Documents in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocuments = documentsMapper.toEntity(returnedDocumentsDTO);
        assertDocumentsUpdatableFieldsEquals(returnedDocuments, getPersistedDocuments(returnedDocuments));

        insertedDocuments = returnedDocuments;
    }

    @Test
    @Transactional
    void createDocumentsWithExistingId() throws Exception {
        // Create the Documents with an existing ID
        documents.setId(1L);
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getDocuments() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get the documents
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL_ID, documents.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documents.getId().intValue()))
            .andExpect(jsonPath("$.documentType").value(DEFAULT_DOCUMENT_TYPE.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileUrl").value(DEFAULT_FILE_URL))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        Long id = documents.getId();

        defaultDocumentsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where documentType equals to
        defaultDocumentsFiltering("documentType.equals=" + DEFAULT_DOCUMENT_TYPE, "documentType.equals=" + UPDATED_DOCUMENT_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByDocumentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where documentType in
        defaultDocumentsFiltering(
            "documentType.in=" + DEFAULT_DOCUMENT_TYPE + "," + UPDATED_DOCUMENT_TYPE,
            "documentType.in=" + UPDATED_DOCUMENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDocumentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where documentType is not null
        defaultDocumentsFiltering("documentType.specified=true", "documentType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileContentTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileContentType equals to
        defaultDocumentsFiltering(
            "fileContentType.equals=" + DEFAULT_FILE_CONTENT_TYPE,
            "fileContentType.equals=" + UPDATED_FILE_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByFileContentTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileContentType in
        defaultDocumentsFiltering(
            "fileContentType.in=" + DEFAULT_FILE_CONTENT_TYPE + "," + UPDATED_FILE_CONTENT_TYPE,
            "fileContentType.in=" + UPDATED_FILE_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByFileContentTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileContentType is not null
        defaultDocumentsFiltering("fileContentType.specified=true", "fileContentType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileContentTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileContentType contains
        defaultDocumentsFiltering(
            "fileContentType.contains=" + DEFAULT_FILE_CONTENT_TYPE,
            "fileContentType.contains=" + UPDATED_FILE_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByFileContentTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileContentType does not contain
        defaultDocumentsFiltering(
            "fileContentType.doesNotContain=" + UPDATED_FILE_CONTENT_TYPE,
            "fileContentType.doesNotContain=" + DEFAULT_FILE_CONTENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName equals to
        defaultDocumentsFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName in
        defaultDocumentsFiltering("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME, "fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName is not null
        defaultDocumentsFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName contains
        defaultDocumentsFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileName does not contain
        defaultDocumentsFiltering("fileName.doesNotContain=" + UPDATED_FILE_NAME, "fileName.doesNotContain=" + DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileUrl equals to
        defaultDocumentsFiltering("fileUrl.equals=" + DEFAULT_FILE_URL, "fileUrl.equals=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileUrl in
        defaultDocumentsFiltering("fileUrl.in=" + DEFAULT_FILE_URL + "," + UPDATED_FILE_URL, "fileUrl.in=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileUrl is not null
        defaultDocumentsFiltering("fileUrl.specified=true", "fileUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileUrl contains
        defaultDocumentsFiltering("fileUrl.contains=" + DEFAULT_FILE_URL, "fileUrl.contains=" + UPDATED_FILE_URL);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where fileUrl does not contain
        defaultDocumentsFiltering("fileUrl.doesNotContain=" + UPDATED_FILE_URL, "fileUrl.doesNotContain=" + DEFAULT_FILE_URL);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate equals to
        defaultDocumentsFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate in
        defaultDocumentsFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate is not null
        defaultDocumentsFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate is greater than or equal to
        defaultDocumentsFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate is less than or equal to
        defaultDocumentsFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate is less than
        defaultDocumentsFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where createdDate is greater than
        defaultDocumentsFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate equals to
        defaultDocumentsFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate in
        defaultDocumentsFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate is not null
        defaultDocumentsFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate is greater than or equal to
        defaultDocumentsFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate is less than or equal to
        defaultDocumentsFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate is less than
        defaultDocumentsFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where modifiedDate is greater than
        defaultDocumentsFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where deleted equals to
        defaultDocumentsFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllDocumentsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where deleted in
        defaultDocumentsFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllDocumentsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where deleted is not null
        defaultDocumentsFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where status equals to
        defaultDocumentsFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where status in
        defaultDocumentsFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        // Get all the documentsList where status is not null
        defaultDocumentsFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByTrademarkIsEqualToSomething() throws Exception {
        Trademark trademark;
        if (TestUtil.findAll(em, Trademark.class).isEmpty()) {
            documentsRepository.saveAndFlush(documents);
            trademark = TrademarkResourceIT.createEntity();
        } else {
            trademark = TestUtil.findAll(em, Trademark.class).get(0);
        }
        em.persist(trademark);
        em.flush();
        documents.setTrademark(trademark);
        documentsRepository.saveAndFlush(documents);
        Long trademarkId = trademark.getId();
        // Get all the documentsList where trademark equals to trademarkId
        defaultDocumentsShouldBeFound("trademarkId.equals=" + trademarkId);

        // Get all the documentsList where trademark equals to (trademarkId + 1)
        defaultDocumentsShouldNotBeFound("trademarkId.equals=" + (trademarkId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentsByUserProfileIsEqualToSomething() throws Exception {
        UserProfile userProfile;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            documentsRepository.saveAndFlush(documents);
            userProfile = UserProfileResourceIT.createEntity();
        } else {
            userProfile = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(userProfile);
        em.flush();
        documents.setUserProfile(userProfile);
        documentsRepository.saveAndFlush(documents);
        Long userProfileId = userProfile.getId();
        // Get all the documentsList where userProfile equals to userProfileId
        defaultDocumentsShouldBeFound("userProfileId.equals=" + userProfileId);

        // Get all the documentsList where userProfile equals to (userProfileId + 1)
        defaultDocumentsShouldNotBeFound("userProfileId.equals=" + (userProfileId + 1));
    }

    private void defaultDocumentsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentsShouldBeFound(shouldBeFound);
        defaultDocumentsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentsShouldBeFound(String filter) throws Exception {
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documents.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentType").value(hasItem(DEFAULT_DOCUMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileUrl").value(hasItem(DEFAULT_FILE_URL)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentsShouldNotBeFound(String filter) throws Exception {
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocuments() throws Exception {
        // Get the documents
        restDocumentsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocuments() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documents
        Documents updatedDocuments = documentsRepository.findById(documents.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocuments are not directly saved in db
        em.detach(updatedDocuments);
        updatedDocuments
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
        DocumentsDTO documentsDTO = documentsMapper.toDto(updatedDocuments);

        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentsToMatchAllProperties(updatedDocuments);
    }

    @Test
    @Transactional
    void putNonExistingDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentsWithPatch() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documents using partial update
        Documents partialUpdatedDocuments = new Documents();
        partialUpdatedDocuments.setId(documents.getId());

        partialUpdatedDocuments
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileUrl(UPDATED_FILE_URL)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .status(UPDATED_STATUS);

        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocuments))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocuments, documents),
            getPersistedDocuments(documents)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentsWithPatch() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documents using partial update
        Documents partialUpdatedDocuments = new Documents();
        partialUpdatedDocuments.setId(documents.getId());

        partialUpdatedDocuments
            .documentType(UPDATED_DOCUMENT_TYPE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .fileName(UPDATED_FILE_NAME)
            .fileUrl(UPDATED_FILE_URL)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);

        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocuments.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocuments))
            )
            .andExpect(status().isOk());

        // Validate the Documents in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentsUpdatableFieldsEquals(partialUpdatedDocuments, getPersistedDocuments(partialUpdatedDocuments));
    }

    @Test
    @Transactional
    void patchNonExistingDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocuments() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documents.setId(longCount.incrementAndGet());

        // Create the Documents
        DocumentsDTO documentsDTO = documentsMapper.toDto(documents);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documents in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocuments() throws Exception {
        // Initialize the database
        insertedDocuments = documentsRepository.saveAndFlush(documents);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documents
        restDocumentsMockMvc
            .perform(delete(ENTITY_API_URL_ID, documents.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentsRepository.count();
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

    protected Documents getPersistedDocuments(Documents documents) {
        return documentsRepository.findById(documents.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentsToMatchAllProperties(Documents expectedDocuments) {
        assertDocumentsAllPropertiesEquals(expectedDocuments, getPersistedDocuments(expectedDocuments));
    }

    protected void assertPersistedDocumentsToMatchUpdatableProperties(Documents expectedDocuments) {
        assertDocumentsAllUpdatablePropertiesEquals(expectedDocuments, getPersistedDocuments(expectedDocuments));
    }
}
