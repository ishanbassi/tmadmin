package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.service.RazorPayService;
import com.bassi.tmapp.service.dto.CreateOrderRequest;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/razor-pay/payments")
public class RazorPayResource {

    private final RazorPayService razorPayService;

    public RazorPayResource(RazorPayService razorPayService) {
        this.razorPayService = razorPayService;
    }

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderRequest payload) {
        try {
            com.razorpay.Order order = razorPayService.createOrder(payload);
            Map<String, Object> response = new HashMap<>();
            response.put("id", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("receipt", order.get("receipt"));
            response.put("status", order.get("status"));
            return ResponseEntity.ok(response);
        } catch (com.razorpay.RazorpayException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    // Placeholder for payment verification endpoint
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> payload) {
        // TODO: Implement payment verification logic
        return ResponseEntity.ok("Verification endpoint not implemented yet");
    }
}
