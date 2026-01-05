package com.bassi.tmapp.service;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.service.dto.CreateOrderRequest;
import com.bassi.tmapp.service.dto.CreateOrderResponse;
import com.bassi.tmapp.service.dto.PaymentConfirmationDto;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.RazorPayOrderResponse;
import com.bassi.tmapp.service.dto.RazorpayPaymentResponseDTO;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import com.bassi.tmapp.service.extended.JwtTokenService;
import com.bassi.tmapp.web.rest.AuthenticateController.JWTToken;
import com.bassi.tmapp.web.rest.RazorPayResource;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RazorPayService {

    private final ApplicationProperties applicationProperties;
    private final TrademarkService trademarkService;
    private final PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(RazorPayService.class);
    private final RestTemplate restTemplate;
    private final UserProfileService userProfileService;
    private final JwtTokenService jwtTokenService;
    private final MailService mailService;

    RazorPayService(
        ApplicationProperties applicationProperties,
        TrademarkService trademarkService,
        PaymentService paymentService,
        RestTemplate restTemplate,
        UserProfileService userProfileService,
        JwtTokenService jwtTokenService,
        MailService mailService
    ) {
        this.applicationProperties = applicationProperties;
        this.trademarkService = trademarkService;
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
        this.userProfileService = userProfileService;
        this.jwtTokenService = jwtTokenService;
        this.mailService = mailService;
    }

    public CreateOrderResponse createOrder(CreateOrderRequest request) throws RazorpayException {
        if (request == null || request.getTrademarkDTO() == null || request.getPaymentDTO() == null) {
            throw new InternalServerAlertException("Failed to create Order request with Razor Pay");
        }
        RazorpayClient client = new RazorpayClient(
            applicationProperties.getRazorPay().getKeyId(),
            applicationProperties.getRazorPay().getKeySecret()
        );

        JSONObject orderRequest = new JSONObject();
        BigDecimal amount = paymentService.calculateTotalFees(request.getPaymentDTO().getOrderId());

        PaymentDTO paymentDTO = request.getPaymentDTO();
        int amountInPaisa = amount.intValue() * 100;
        orderRequest.put("amount", amountInPaisa);
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", paymentDTO.getOrderId());

        Order order = client.orders.create(orderRequest);
        paymentDTO.setAmount(amount);
        paymentDTO.setGatewayOrderId(order.get("id"));
        paymentDTO.setStatus(order.get("status"));
        paymentService.save(paymentDTO);

        return new CreateOrderResponse(
            order.get("id"),
            order.get("amount"),
            order.get("currency"),
            order.get("status"),
            applicationProperties.getRazorPay().getKeyId()
        );
    }

    public String generateOrderId(Long dbId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return "ORD-" + date + "-" + String.format("%05d", dbId);
    }

    public TrademarkOrderSummary generateTrademarkOrderSummary(String orderId) {
        TrademarkOrderSummary trademarkOrderSummary = paymentService.generateTrademarkOrderSummary(orderId);
        return trademarkOrderSummary;
    }

    public PaymentConfirmationDto verifySignature(RazorPayOrderResponse razorPayOrderResponse) {
        Optional<Payment> optionalPayment = paymentService.findByRazorpayOrderId(razorPayOrderResponse.getRazorpayOrderId());
        if (optionalPayment.isEmpty()) {
            throw new InternalServerAlertException("Payment Entity not found");
        }
        Payment payment = optionalPayment.get();

        payment.setGatewayPaymentId(razorPayOrderResponse.getRazorpayPaymentId());
        paymentService.save(payment);

        try {
            boolean signatureVerified = verifyPaymentSignature(
                razorPayOrderResponse.getRazorpayOrderId(),
                razorPayOrderResponse.getRazorpayPaymentId(),
                razorPayOrderResponse.getRazorpaySignature()
            );
            payment.setStatus(signatureVerified ? "SIGNATURE VERIFIED" : "SIGNATURE UNVERIFIED");
            paymentService.save(payment);
        } catch (Exception e) {
            log.error("Error while verifying signature, Error: {}", e.getLocalizedMessage());
            payment.setFailureReason(e.getLocalizedMessage());
            payment.setStatus("SIGNATURE UNVERIFIED");
            paymentService.save(payment);
        }
        payment = fetchPaymentStatus(razorPayOrderResponse, payment);
        PaymentConfirmationDto paymentConfirmationDto = new PaymentConfirmationDto();
        if (razorPayOrderResponse.getUserProfileDTO() == null) {
            log.info("Creating a user profile for anonymous user using leadDto: {}", razorPayOrderResponse.getLeadDTO());
            UserProfileDTO userProfileDTO = userProfileService.save(payment, razorPayOrderResponse.getLeadDTO());
            JWTToken token = jwtTokenService.createTokenWithoutPassword(userProfileDTO.getUser());
            paymentConfirmationDto.setToken(token);
            paymentConfirmationDto.setUserProfileDTO(userProfileDTO);
        }

        paymentConfirmationDto.setStatus(payment.getStatus());
        mailService.sendPaymentSuccessfulEmailToAdmin(payment);
        return paymentConfirmationDto;
    }

    private Payment fetchPaymentStatus(RazorPayOrderResponse razorPayOrderResponse, Payment payment) {
        String auth = applicationProperties.getRazorPay().getKeyId() + ":" + applicationProperties.getRazorPay().getKeySecret();
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        String paymentUrl = applicationProperties.getRazorPay().getBaseUrl() + "/payments/" + razorPayOrderResponse.getRazorpayPaymentId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<RazorpayPaymentResponseDTO> entity = new HttpEntity<>(headers);

        ResponseEntity<RazorpayPaymentResponseDTO> response = restTemplate.exchange(
            paymentUrl,
            HttpMethod.GET,
            entity,
            RazorpayPaymentResponseDTO.class
        );
        RazorpayPaymentResponseDTO razorpayPaymentResponseDTO = response.getBody();
        if (razorpayPaymentResponseDTO != null) {
            payment.setPaymentMethod(razorpayPaymentResponseDTO.getMethod());
            payment.setStatus(razorpayPaymentResponseDTO.getStatus());
            paymentService.save(payment);
        }

        log.info("Response: {}", response);

        return payment;
    }

    /**
     * Generate HMAC SHA256 signature
     */
    private String generateSignature(String payload, String secret) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    /**
     * Verify payment signature (for payment verification)
     * @param orderId Razorpay order ID
     * @param paymentId Razorpay payment ID
     * @param signature Razorpay signature
     * @return true if signature is valid
     */
    public boolean verifyPaymentSignature(String orderId, String paymentId, String signature) {
        try {
            String payload = orderId + "|" + paymentId;
            String generatedSignature = generateSignature(payload, applicationProperties.getRazorPay().getKeySecret());
            return generatedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
