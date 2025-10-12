package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.service.RazorPayService;
import com.bassi.tmapp.service.dto.CreateOrderRequest;
import com.bassi.tmapp.service.dto.CreateOrderResponse;
import com.bassi.tmapp.service.dto.PaymentConfirmationDto;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.RazorPayOrderResponse;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.ResponseUtil;

@RestController
@RequestMapping("/api/razor-pay/payments")
public class RazorPayResource {

    private static final Logger LOG = LoggerFactory.getLogger(RazorPayResource.class);

    private final RazorPayService razorPayService;

    public RazorPayResource(RazorPayService razorPayService) {
        this.razorPayService = razorPayService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderRequest payload) {
        try {
            CreateOrderResponse orderResponse = razorPayService.createOrder(payload);
            return ResponseEntity.status(HttpStatusCode.valueOf(201)).body(orderResponse);
        } catch (com.razorpay.RazorpayException e) {
            throw new InternalServerAlertException(e.getMessage());
        }
    }

    @PostMapping("/verify-signature")
    public ResponseEntity<PaymentConfirmationDto> verifyPayment(@RequestBody RazorPayOrderResponse payload) {
        PaymentConfirmationDto paymentConfirmationDto = razorPayService.verifySignature(payload);
        return ResponseEntity.ok(paymentConfirmationDto);
    }

    @GetMapping("/order-id/{orderId}")
    public ResponseEntity<TrademarkOrderSummary> getPayment(@PathVariable("orderId") String orderId) {
        LOG.debug("REST request to get Payment : {}", orderId);
        TrademarkOrderSummary trademarkOrderSummary = razorPayService.generateTrademarkOrderSummary(orderId);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(trademarkOrderSummary);
    }
}
