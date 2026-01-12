package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.UserEventsTrackingAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.UserEventsTracking;
import com.bassi.tmapp.repository.UserEventsTrackingRepository;
import com.bassi.tmapp.service.dto.UserEventsTrackingDTO;
import com.bassi.tmapp.service.mapper.UserEventsTrackingMapper;
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
 * Integration tests for the {@link UserEventsTrackingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserEventsTrackingResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PAGE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_TYPE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String ENTITY_API_URL = "/api/user-events-trackings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserEventsTrackingRepository userEventsTrackingRepository;

    @Autowired
    private UserEventsTrackingMapper userEventsTrackingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserEventsTrackingMockMvc;

    private UserEventsTracking userEventsTracking;

    private UserEventsTracking insertedUserEventsTracking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEventsTracking createEntity() {
        return new UserEventsTracking()
            .eventType(DEFAULT_EVENT_TYPE)
            .pageName(DEFAULT_PAGE_NAME)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .createdDate(DEFAULT_CREATED_DATE)
            .deleted(DEFAULT_DELETED)
            .modifiedDate(DEFAULT_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserEventsTracking createUpdatedEntity() {
        return new UserEventsTracking()
            .eventType(UPDATED_EVENT_TYPE)
            .pageName(UPDATED_PAGE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        userEventsTracking = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUserEventsTracking != null) {
            userEventsTrackingRepository.delete(insertedUserEventsTracking);
            insertedUserEventsTracking = null;
        }
    }

    @Test
    @Transactional
    void createUserEventsTracking() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);
        var returnedUserEventsTrackingDTO = om.readValue(
            restUserEventsTrackingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventsTrackingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserEventsTrackingDTO.class
        );

        // Validate the UserEventsTracking in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserEventsTracking = userEventsTrackingMapper.toEntity(returnedUserEventsTrackingDTO);
        assertUserEventsTrackingUpdatableFieldsEquals(
            returnedUserEventsTracking,
            getPersistedUserEventsTracking(returnedUserEventsTracking)
        );

        insertedUserEventsTracking = returnedUserEventsTracking;
    }

    @Test
    @Transactional
    void createUserEventsTrackingWithExistingId() throws Exception {
        // Create the UserEventsTracking with an existing ID
        userEventsTracking.setId(1L);
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserEventsTrackingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventsTrackingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserEventsTrackings() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        // Get all the userEventsTrackingList
        restUserEventsTrackingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userEventsTracking.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].pageName").value(hasItem(DEFAULT_PAGE_NAME)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))));
    }

    @Test
    @Transactional
    void getUserEventsTracking() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        // Get the userEventsTracking
        restUserEventsTrackingMockMvc
            .perform(get(ENTITY_API_URL_ID, userEventsTracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userEventsTracking.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.pageName").value(DEFAULT_PAGE_NAME))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)));
    }

    @Test
    @Transactional
    void getNonExistingUserEventsTracking() throws Exception {
        // Get the userEventsTracking
        restUserEventsTrackingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserEventsTracking() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEventsTracking
        UserEventsTracking updatedUserEventsTracking = userEventsTrackingRepository.findById(userEventsTracking.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserEventsTracking are not directly saved in db
        em.detach(updatedUserEventsTracking);
        updatedUserEventsTracking
            .eventType(UPDATED_EVENT_TYPE)
            .pageName(UPDATED_PAGE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(updatedUserEventsTracking);

        restUserEventsTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventsTrackingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventsTrackingDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserEventsTrackingToMatchAllProperties(updatedUserEventsTracking);
    }

    @Test
    @Transactional
    void putNonExistingUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userEventsTrackingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventsTrackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userEventsTrackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userEventsTrackingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserEventsTrackingWithPatch() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEventsTracking using partial update
        UserEventsTracking partialUpdatedUserEventsTracking = new UserEventsTracking();
        partialUpdatedUserEventsTracking.setId(userEventsTracking.getId());

        partialUpdatedUserEventsTracking
            .eventType(UPDATED_EVENT_TYPE)
            .pageName(UPDATED_PAGE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restUserEventsTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEventsTracking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserEventsTracking))
            )
            .andExpect(status().isOk());

        // Validate the UserEventsTracking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserEventsTrackingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserEventsTracking, userEventsTracking),
            getPersistedUserEventsTracking(userEventsTracking)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserEventsTrackingWithPatch() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userEventsTracking using partial update
        UserEventsTracking partialUpdatedUserEventsTracking = new UserEventsTracking();
        partialUpdatedUserEventsTracking.setId(userEventsTracking.getId());

        partialUpdatedUserEventsTracking
            .eventType(UPDATED_EVENT_TYPE)
            .pageName(UPDATED_PAGE_NAME)
            .deviceType(UPDATED_DEVICE_TYPE)
            .createdDate(UPDATED_CREATED_DATE)
            .deleted(UPDATED_DELETED)
            .modifiedDate(UPDATED_MODIFIED_DATE);

        restUserEventsTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserEventsTracking.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserEventsTracking))
            )
            .andExpect(status().isOk());

        // Validate the UserEventsTracking in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserEventsTrackingUpdatableFieldsEquals(
            partialUpdatedUserEventsTracking,
            getPersistedUserEventsTracking(partialUpdatedUserEventsTracking)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userEventsTrackingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userEventsTrackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userEventsTrackingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserEventsTracking() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userEventsTracking.setId(longCount.incrementAndGet());

        // Create the UserEventsTracking
        UserEventsTrackingDTO userEventsTrackingDTO = userEventsTrackingMapper.toDto(userEventsTracking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserEventsTrackingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userEventsTrackingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserEventsTracking in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserEventsTracking() throws Exception {
        // Initialize the database
        insertedUserEventsTracking = userEventsTrackingRepository.saveAndFlush(userEventsTracking);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userEventsTracking
        restUserEventsTrackingMockMvc
            .perform(delete(ENTITY_API_URL_ID, userEventsTracking.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userEventsTrackingRepository.count();
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

    protected UserEventsTracking getPersistedUserEventsTracking(UserEventsTracking userEventsTracking) {
        return userEventsTrackingRepository.findById(userEventsTracking.getId()).orElseThrow();
    }

    protected void assertPersistedUserEventsTrackingToMatchAllProperties(UserEventsTracking expectedUserEventsTracking) {
        assertUserEventsTrackingAllPropertiesEquals(expectedUserEventsTracking, getPersistedUserEventsTracking(expectedUserEventsTracking));
    }

    protected void assertPersistedUserEventsTrackingToMatchUpdatableProperties(UserEventsTracking expectedUserEventsTracking) {
        assertUserEventsTrackingAllUpdatablePropertiesEquals(
            expectedUserEventsTracking,
            getPersistedUserEventsTracking(expectedUserEventsTracking)
        );
    }
}
