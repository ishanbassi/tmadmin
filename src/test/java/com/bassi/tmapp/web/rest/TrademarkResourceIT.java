package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.TrademarkAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
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

    private static final String DEFAULT_TRADEMARK_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TRADEMARK_STATUS = "BBBBBBBBBB";

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
            .trademarkStatus(DEFAULT_TRADEMARK_STATUS);
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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);
        return trademark;
    }

    @BeforeEach
    public void initTest() {
        trademark = createEntity(em);
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
        trademarkRepository.saveAndFlush(trademark);

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
            .andExpect(jsonPath("$.[*].trademarkStatus").value(hasItem(DEFAULT_TRADEMARK_STATUS)));
    }

    @Test
    @Transactional
    void getTrademark() throws Exception {
        // Initialize the database
        trademarkRepository.saveAndFlush(trademark);

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
            .andExpect(jsonPath("$.trademarkStatus").value(DEFAULT_TRADEMARK_STATUS));
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
        trademarkRepository.saveAndFlush(trademark);

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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);
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
        trademarkRepository.saveAndFlush(trademark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the trademark using partial update
        Trademark partialUpdatedTrademark = new Trademark();
        partialUpdatedTrademark.setId(trademark.getId());

        partialUpdatedTrademark
            .name(UPDATED_NAME)
            .details(UPDATED_DETAILS)
            .agentAddress(UPDATED_AGENT_ADDRESS)
            .proprietorName(UPDATED_PROPRIETOR_NAME)
            .headOffice(UPDATED_HEAD_OFFICE)
            .journalNo(UPDATED_JOURNAL_NO)
            .usage(UPDATED_USAGE)
            .associatedTms(UPDATED_ASSOCIATED_TMS);

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
        trademarkRepository.saveAndFlush(trademark);

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
            .trademarkStatus(UPDATED_TRADEMARK_STATUS);

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
        trademarkRepository.saveAndFlush(trademark);

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
