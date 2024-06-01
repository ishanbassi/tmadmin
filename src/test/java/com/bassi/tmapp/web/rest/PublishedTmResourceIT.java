package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.PublishedTmAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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

    private static final LocalDate DEFAULT_APPLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_APPLICATION_DATE = LocalDate.now(ZoneId.systemDefault());

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

    private static final Integer DEFAULT_JOURNAL_NO = 1;
    private static final Integer UPDATED_JOURNAL_NO = 2;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String DEFAULT_USAGE = "AAAAAAAAAA";
    private static final String UPDATED_USAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ASSOCIATED_TMS = "AAAAAAAAAA";
    private static final String UPDATED_ASSOCIATED_TMS = "BBBBBBBBBB";

    private static final TrademarkStatus DEFAULT_TRADEMARK_STATUS = TrademarkStatus.REGISTERED;
    private static final TrademarkStatus UPDATED_TRADEMARK_STATUS = TrademarkStatus.OPPOSED;

    private static final String ENTITY_API_URL = "/api/published-tms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PublishedTmRepository publishedTmRepository;

    @Autowired
    private PublishedTmMapper publishedTmMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPublishedTmMockMvc;

    private PublishedTm publishedTm;

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
            .trademarkStatus(DEFAULT_TRADEMARK_STATUS);
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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);
        return publishedTm;
    }

    @BeforeEach
    public void initTest() {
        publishedTm = createEntity(em);
    }

    @Test
    @Transactional
    void createPublishedTm() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);
        var returnedPublishedTmDTO = om.readValue(
            restPublishedTmMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PublishedTmDTO.class
        );

        // Validate the PublishedTm in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPublishedTm = publishedTmMapper.toEntity(returnedPublishedTmDTO);
        assertPublishedTmUpdatableFieldsEquals(returnedPublishedTm, getPersistedPublishedTm(returnedPublishedTm));
    }

    @Test
    @Transactional
    void createPublishedTmWithExistingId() throws Exception {
        // Create the PublishedTm with an existing ID
        publishedTm.setId(1L);
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPublishedTmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPublishedTms() throws Exception {
        // Initialize the database
        publishedTmRepository.saveAndFlush(publishedTm);

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
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS.toString())));
    }

    @Test
    @Transactional
    void getPublishedTm() throws Exception {
        // Initialize the database
        publishedTmRepository.saveAndFlush(publishedTm);

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
            .andExpect(jsonPath("$.trademarkStatus").value(DEFAULT_TRADEMARK_STATUS.toString()));
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
        publishedTmRepository.saveAndFlush(publishedTm);

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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(updatedPublishedTm);

        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publishedTmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmDTO))
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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, publishedTmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmDTO))
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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(publishedTmDTO))
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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(publishedTmDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePublishedTmWithPatch() throws Exception {
        // Initialize the database
        publishedTmRepository.saveAndFlush(publishedTm);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the publishedTm using partial update
        PublishedTm partialUpdatedPublishedTm = new PublishedTm();
        partialUpdatedPublishedTm.setId(publishedTm.getId());

        partialUpdatedPublishedTm
            .name(UPDATED_NAME)
            .applicationDate(UPDATED_APPLICATION_DATE)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .proprietorAddress(UPDATED_PROPRIETOR_ADDRESS)
            .imgUrl(UPDATED_IMG_URL)
            .tmClass(UPDATED_TM_CLASS)
            .usage(UPDATED_USAGE)
            .associatedTms(UPDATED_ASSOCIATED_TMS)
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);

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
        publishedTmRepository.saveAndFlush(publishedTm);

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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);

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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, publishedTmDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTmDTO))
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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(publishedTmDTO))
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

        // Create the PublishedTm
        PublishedTmDTO publishedTmDTO = publishedTmMapper.toDto(publishedTm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPublishedTmMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(publishedTmDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PublishedTm in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePublishedTm() throws Exception {
        // Initialize the database
        publishedTmRepository.saveAndFlush(publishedTm);

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
