package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.repository.PaymentRepository;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary.OrderSummary;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import com.bassi.tmapp.service.mapper.PaymentMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.security.RandomUtil;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Payment}.
 */
@Service
@Transactional
public class PaymentService {

    private static final Logger LOG = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final TrademarkService trademarkService;

    private final LeadService leadService;

    private final DocumentsService documentsService;

    public PaymentService(
        PaymentRepository paymentRepository,
        PaymentMapper paymentMapper,
        TrademarkService trademarkService,
        LeadService leadService,
        DocumentsService documentsService
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.trademarkService = trademarkService;
        this.leadService = leadService;
        this.documentsService = documentsService;
    }

    /**
     * Save a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentDTO save(PaymentDTO paymentDTO) {
        LOG.debug("Request to save Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Update a payment.
     *
     * @param paymentDTO the entity to save.
     * @return the persisted entity.
     */
    public PaymentDTO update(PaymentDTO paymentDTO) {
        LOG.debug("Request to update Payment : {}", paymentDTO);
        Payment payment = paymentMapper.toEntity(paymentDTO);
        payment = paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    /**
     * Partially update a payment.
     *
     * @param paymentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PaymentDTO> partialUpdate(PaymentDTO paymentDTO) {
        LOG.debug("Request to partially update Payment : {}", paymentDTO);

        return paymentRepository
            .findById(paymentDTO.getId())
            .map(existingPayment -> {
                paymentMapper.partialUpdate(existingPayment, paymentDTO);

                return existingPayment;
            })
            .map(paymentRepository::save)
            .map(paymentMapper::toDto);
    }

    /**
     * Get one payment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PaymentDTO> findOne(Long id) {
        LOG.debug("Request to get Payment : {}", id);
        return paymentRepository.findById(id).map(paymentMapper::toDto);
    }

    /**
     * Delete the payment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Payment : {}", id);
        paymentRepository.deleteById(id);
    }

    public PaymentDTO createPaymentFromTrademarkDTO(Long tmId) {
        Optional<TrademarkDTO> optionalTrademarkDTO = trademarkService.findOne(tmId);
        if (optionalTrademarkDTO.isEmpty()) {
            throw new InternalServerAlertException("Trademark Entity not found.");
        }
        TrademarkDTO trademarkDTO = optionalTrademarkDTO.get();
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setCurrency("INR");
        paymentDTO.setStatus("PENDING");
        paymentDTO.setTrademark(trademarkDTO);
        paymentDTO = save(paymentDTO);
        paymentDTO.setOrderId(UUID.randomUUID().toString());
        paymentDTO.setAmount(calculateTotalFees(paymentDTO));
        save(paymentDTO);
        return paymentDTO;
    }

    public String generateOrderId(Long dbId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%05d", dbId);
    }

    public BigDecimal calculateTotalFees(String orderId) {
        TrademarkOrderSummary trademarkOrderSummary = generateTrademarkOrderSummary(orderId);

        return trademarkOrderSummary.getTotalFees();
    }

    public BigDecimal calculateTotalFees(PaymentDTO paymentDTO) {
        TrademarkOrderSummary trademarkOrderSummary = generateTrademarkOrderSummary(paymentDTO);
        return trademarkOrderSummary.getTotalFees();
    }

    public TrademarkOrderSummary generateTrademarkOrderSummary(PaymentDTO paymentDTO) {
        if (paymentDTO.getTrademark() == null || paymentDTO.getTrademark().getId() == null) {
            throw new InternalServerAlertException("Trademark Entity is null");
        }
        Optional<TrademarkDTO> optionalTrademarkDTO = trademarkService.findOne(paymentDTO.getTrademark().getId());
        if (optionalTrademarkDTO.isEmpty()) {
            throw new InternalServerAlertException("Trademark Entity not found.");
        }
        TrademarkDTO trademarkDTO = optionalTrademarkDTO.get();
        if (trademarkDTO == null || trademarkDTO.getId() == null) {
            throw new InternalServerAlertException("Error Generating Order Summary. Please contact.");
        }
        if (trademarkDTO.getLead() == null) {
            throw new InternalServerAlertException("Error Generating Order Summary. No lead detail found");
        }
        Optional<LeadDTO> optionalLeadDTO = leadService.findOne(trademarkDTO.getLead().getId());
        if (optionalLeadDTO.isEmpty()) {
            throw new InternalServerAlertException("Error Generating Order Summary. No lead detail found");
        }

        TrademarkOrderSummary trademarkOrderSummary = new TrademarkOrderSummary();
        trademarkOrderSummary.setTrademarkDTO(trademarkDTO);
        trademarkOrderSummary.setLeadDTO(optionalLeadDTO.get());
        trademarkOrderSummary.setPaymentDTO(paymentDTO);

        Optional<DocumentsDTO> optionalDocument = documentsService.findByTrademark(trademarkDTO);
        if (optionalDocument.isPresent()) {
            trademarkOrderSummary.setDocumentsDTO(optionalDocument.get());
        }

        TrademarkPlanDTO trademarkPlanDTO = trademarkDTO.getTrademarkPlan();
        BigDecimal totalFees = BigDecimal.ZERO;

        if (trademarkDTO.getTrademarkClasses() == null || trademarkDTO.getTrademarkClasses().isEmpty()) {
            if (trademarkPlanDTO == null) {
                totalFees = BigDecimal.valueOf(999);
            } else {
                totalFees = trademarkPlanDTO.getFees();
            }
            trademarkOrderSummary.setOrderSummaries(List.of(generateOrderSummary(trademarkPlanDTO, null)));

            trademarkOrderSummary.setTotalFees(totalFees);

            return trademarkOrderSummary;
        }

        List<OrderSummary> orderSummaries = new ArrayList<>();
        List<Integer> uniqueClasses = new ArrayList(new HashSet<>(trademarkDTO.getTrademarkClasses()));
        for (Integer tmClass : uniqueClasses) {
            OrderSummary orderSummary = generateOrderSummary(trademarkPlanDTO, tmClass);
            orderSummaries.add(orderSummary);
            totalFees = totalFees.add(orderSummary.getFees());
        }

        trademarkOrderSummary.setOrderSummaries(orderSummaries);
        trademarkOrderSummary.setTotalFees(totalFees);

        trademarkOrderSummary.setPaymentDTO(paymentDTO);

        return trademarkOrderSummary;
    }

    private OrderSummary generateOrderSummary(TrademarkPlanDTO trademarkPlanDTO, Integer tmClass) {
        OrderSummary orderSummary = new OrderSummary();
        orderSummary.setTmClass(tmClass);
        if (trademarkPlanDTO == null) {
            orderSummary.setFees(BigDecimal.valueOf(999));
        } else {
            orderSummary.setFees(trademarkPlanDTO.getFees());
        }
        return orderSummary;
    }

    public TrademarkOrderSummary generateTrademarkOrderSummary(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new InternalServerAlertException("Error Generating Order Summary. Please contact. Reason: Order Id is null");
        }
        Optional<Payment> optionalPayment = paymentRepository.findPaymentByOrderId(orderId);
        if (optionalPayment.isEmpty()) {
            throw new InternalServerAlertException(
                "Error Generating Order Summary. Please contact. Reason: Payment entity with id " + orderId + " not found"
            );
        }
        PaymentDTO paymentDTO = paymentMapper.toDto(optionalPayment.get());
        if (paymentDTO.getTrademark() == null || paymentDTO.getTrademark().getId() == null) {
            throw new InternalServerAlertException("Trademark Entity is null");
        }
        return generateTrademarkOrderSummary(paymentDTO);
    }

    public Optional<Payment> findByOrderId(String razorpayOrderId) {
        return paymentRepository.findPaymentByOrderId(razorpayOrderId);
    }

    public PaymentDTO save(Payment payment) {
        return save(paymentMapper.toDto(payment));
    }

    public Optional<Payment> findByRazorpayOrderId(String razorpayOrderId) {
        return paymentRepository.findPaymentByGatewayOrderId(razorpayOrderId);
    }
}
