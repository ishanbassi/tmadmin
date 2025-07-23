package com.bassi.tmapp.web.rest;

import static com.bassi.tmapp.domain.PaymentAsserts.*;
import static com.bassi.tmapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bassi.tmapp.web.rest.TestUtil.sameInstant;
import static com.bassi.tmapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bassi.tmapp.IntegrationTest;
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.repository.PaymentRepository;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.mapper.PaymentMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final String DEFAULT_GATEWAY = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY = "BBBBBBBBBB";

    private static final String DEFAULT_GATEWAY_PAYMENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_GATEWAY_PAYMENT_ID = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MODIFIED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_MODIFIED_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    private Payment insertedPayment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity() {
        return new Payment()
            .gateway(DEFAULT_GATEWAY)
            .gatewayPaymentId(DEFAULT_GATEWAY_PAYMENT_ID)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS)
            .paymentMethod(DEFAULT_PAYMENT_METHOD)
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
    public static Payment createUpdatedEntity() {
        return new Payment()
            .gateway(UPDATED_GATEWAY)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);
    }

    @BeforeEach
    void initTest() {
        payment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPayment != null) {
            paymentRepository.delete(insertedPayment);
            insertedPayment = null;
        }
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        var returnedPaymentDTO = om.readValue(
            restPaymentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaymentDTO.class
        );

        // Validate the Payment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPayment = paymentMapper.toEntity(returnedPaymentDTO);
        assertPaymentUpdatableFieldsEquals(returnedPayment, getPersistedPayment(returnedPayment));

        insertedPayment = returnedPayment;
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].gateway").value(hasItem(DEFAULT_GATEWAY)))
            .andExpect(jsonPath("$.[*].gatewayPaymentId").value(hasItem(DEFAULT_GATEWAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(payment.getId().intValue()))
            .andExpect(jsonPath("$.gateway").value(DEFAULT_GATEWAY))
            .andExpect(jsonPath("$.gatewayPaymentId").value(DEFAULT_GATEWAY_PAYMENT_ID))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD))
            .andExpect(jsonPath("$.createdDate").value(sameInstant(DEFAULT_CREATED_DATE)))
            .andExpect(jsonPath("$.modifiedDate").value(sameInstant(DEFAULT_MODIFIED_DATE)))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        Long id = payment.getId();

        defaultPaymentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPaymentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPaymentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway equals to
        defaultPaymentFiltering("gateway.equals=" + DEFAULT_GATEWAY, "gateway.equals=" + UPDATED_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway in
        defaultPaymentFiltering("gateway.in=" + DEFAULT_GATEWAY + "," + UPDATED_GATEWAY, "gateway.in=" + UPDATED_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway is not null
        defaultPaymentFiltering("gateway.specified=true", "gateway.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway contains
        defaultPaymentFiltering("gateway.contains=" + DEFAULT_GATEWAY, "gateway.contains=" + UPDATED_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gateway does not contain
        defaultPaymentFiltering("gateway.doesNotContain=" + UPDATED_GATEWAY, "gateway.doesNotContain=" + DEFAULT_GATEWAY);
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId equals to
        defaultPaymentFiltering(
            "gatewayPaymentId.equals=" + DEFAULT_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.equals=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId in
        defaultPaymentFiltering(
            "gatewayPaymentId.in=" + DEFAULT_GATEWAY_PAYMENT_ID + "," + UPDATED_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.in=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId is not null
        defaultPaymentFiltering("gatewayPaymentId.specified=true", "gatewayPaymentId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId contains
        defaultPaymentFiltering(
            "gatewayPaymentId.contains=" + DEFAULT_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.contains=" + UPDATED_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByGatewayPaymentIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where gatewayPaymentId does not contain
        defaultPaymentFiltering(
            "gatewayPaymentId.doesNotContain=" + UPDATED_GATEWAY_PAYMENT_ID,
            "gatewayPaymentId.doesNotContain=" + DEFAULT_GATEWAY_PAYMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to
        defaultPaymentFiltering("amount.equals=" + DEFAULT_AMOUNT, "amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in
        defaultPaymentFiltering("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT, "amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentFiltering("amount.specified=true", "amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to
        defaultPaymentFiltering("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT, "amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to
        defaultPaymentFiltering("amount.lessThanOrEqual=" + DEFAULT_AMOUNT, "amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than
        defaultPaymentFiltering("amount.lessThan=" + UPDATED_AMOUNT, "amount.lessThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than
        defaultPaymentFiltering("amount.greaterThan=" + SMALLER_AMOUNT, "amount.greaterThan=" + DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency equals to
        defaultPaymentFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency in
        defaultPaymentFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency is not null
        defaultPaymentFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency contains
        defaultPaymentFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where currency does not contain
        defaultPaymentFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status equals to
        defaultPaymentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status in
        defaultPaymentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status is not null
        defaultPaymentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status contains
        defaultPaymentFiltering("status.contains=" + DEFAULT_STATUS, "status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where status does not contain
        defaultPaymentFiltering("status.doesNotContain=" + UPDATED_STATUS, "status.doesNotContain=" + DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod equals to
        defaultPaymentFiltering("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod in
        defaultPaymentFiltering(
            "paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD,
            "paymentMethod.in=" + UPDATED_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod is not null
        defaultPaymentFiltering("paymentMethod.specified=true", "paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod contains
        defaultPaymentFiltering("paymentMethod.contains=" + DEFAULT_PAYMENT_METHOD, "paymentMethod.contains=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod does not contain
        defaultPaymentFiltering(
            "paymentMethod.doesNotContain=" + UPDATED_PAYMENT_METHOD,
            "paymentMethod.doesNotContain=" + DEFAULT_PAYMENT_METHOD
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate equals to
        defaultPaymentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate in
        defaultPaymentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is not null
        defaultPaymentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is greater than or equal to
        defaultPaymentFiltering(
            "createdDate.greaterThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.greaterThanOrEqual=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is less than or equal to
        defaultPaymentFiltering(
            "createdDate.lessThanOrEqual=" + DEFAULT_CREATED_DATE,
            "createdDate.lessThanOrEqual=" + SMALLER_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is less than
        defaultPaymentFiltering("createdDate.lessThan=" + UPDATED_CREATED_DATE, "createdDate.lessThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByCreatedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where createdDate is greater than
        defaultPaymentFiltering("createdDate.greaterThan=" + SMALLER_CREATED_DATE, "createdDate.greaterThan=" + DEFAULT_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate equals to
        defaultPaymentFiltering("modifiedDate.equals=" + DEFAULT_MODIFIED_DATE, "modifiedDate.equals=" + UPDATED_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate in
        defaultPaymentFiltering(
            "modifiedDate.in=" + DEFAULT_MODIFIED_DATE + "," + UPDATED_MODIFIED_DATE,
            "modifiedDate.in=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate is not null
        defaultPaymentFiltering("modifiedDate.specified=true", "modifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate is greater than or equal to
        defaultPaymentFiltering(
            "modifiedDate.greaterThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.greaterThanOrEqual=" + UPDATED_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate is less than or equal to
        defaultPaymentFiltering(
            "modifiedDate.lessThanOrEqual=" + DEFAULT_MODIFIED_DATE,
            "modifiedDate.lessThanOrEqual=" + SMALLER_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate is less than
        defaultPaymentFiltering("modifiedDate.lessThan=" + UPDATED_MODIFIED_DATE, "modifiedDate.lessThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByModifiedDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where modifiedDate is greater than
        defaultPaymentFiltering("modifiedDate.greaterThan=" + SMALLER_MODIFIED_DATE, "modifiedDate.greaterThan=" + DEFAULT_MODIFIED_DATE);
    }

    @Test
    @Transactional
    void getAllPaymentsByDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where deleted equals to
        defaultPaymentFiltering("deleted.equals=" + DEFAULT_DELETED, "deleted.equals=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllPaymentsByDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where deleted in
        defaultPaymentFiltering("deleted.in=" + DEFAULT_DELETED + "," + UPDATED_DELETED, "deleted.in=" + UPDATED_DELETED);
    }

    @Test
    @Transactional
    void getAllPaymentsByDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where deleted is not null
        defaultPaymentFiltering("deleted.specified=true", "deleted.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByLeadIsEqualToSomething() throws Exception {
        Lead lead;
        if (TestUtil.findAll(em, Lead.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            lead = LeadResourceIT.createEntity();
        } else {
            lead = TestUtil.findAll(em, Lead.class).get(0);
        }
        em.persist(lead);
        em.flush();
        payment.setLead(lead);
        paymentRepository.saveAndFlush(payment);
        Long leadId = lead.getId();
        // Get all the paymentList where lead equals to leadId
        defaultPaymentShouldBeFound("leadId.equals=" + leadId);

        // Get all the paymentList where lead equals to (leadId + 1)
        defaultPaymentShouldNotBeFound("leadId.equals=" + (leadId + 1));
    }

    @Test
    @Transactional
    void getAllPaymentsByUserIsEqualToSomething() throws Exception {
        UserProfile user;
        if (TestUtil.findAll(em, UserProfile.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            user = UserProfileResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, UserProfile.class).get(0);
        }
        em.persist(user);
        em.flush();
        payment.setUser(user);
        paymentRepository.saveAndFlush(payment);
        Long userId = user.getId();
        // Get all the paymentList where user equals to userId
        defaultPaymentShouldBeFound("userId.equals=" + userId);

        // Get all the paymentList where user equals to (userId + 1)
        defaultPaymentShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultPaymentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPaymentShouldBeFound(shouldBeFound);
        defaultPaymentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(payment.getId().intValue())))
            .andExpect(jsonPath("$.[*].gateway").value(hasItem(DEFAULT_GATEWAY)))
            .andExpect(jsonPath("$.[*].gatewayPaymentId").value(hasItem(DEFAULT_GATEWAY_PAYMENT_ID)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(sameInstant(DEFAULT_CREATED_DATE))))
            .andExpect(jsonPath("$.[*].modifiedDate").value(hasItem(sameInstant(DEFAULT_MODIFIED_DATE))))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .gateway(UPDATED_GATEWAY)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaymentToMatchAllProperties(updatedPayment);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment.gateway(UPDATED_GATEWAY).paymentMethod(UPDATED_PAYMENT_METHOD).modifiedDate(UPDATED_MODIFIED_DATE);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPayment, payment), getPersistedPayment(payment));
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setId(payment.getId());

        partialUpdatedPayment
            .gateway(UPDATED_GATEWAY)
            .gatewayPaymentId(UPDATED_GATEWAY_PAYMENT_ID)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .paymentMethod(UPDATED_PAYMENT_METHOD)
            .createdDate(UPDATED_CREATED_DATE)
            .modifiedDate(UPDATED_MODIFIED_DATE)
            .deleted(UPDATED_DELETED);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaymentUpdatableFieldsEquals(partialUpdatedPayment, getPersistedPayment(partialUpdatedPayment));
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        payment.setId(longCount.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        insertedPayment = paymentRepository.saveAndFlush(payment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paymentRepository.count();
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

    protected Payment getPersistedPayment(Payment payment) {
        return paymentRepository.findById(payment.getId()).orElseThrow();
    }

    protected void assertPersistedPaymentToMatchAllProperties(Payment expectedPayment) {
        assertPaymentAllPropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }

    protected void assertPersistedPaymentToMatchUpdatableProperties(Payment expectedPayment) {
        assertPaymentAllUpdatablePropertiesEquals(expectedPayment, getPersistedPayment(expectedPayment));
    }
}
