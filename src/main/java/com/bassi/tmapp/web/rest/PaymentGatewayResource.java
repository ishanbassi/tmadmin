//package com.bassi.tmapp.web.rest;
//
//import com.bassi.tmapp.service.PaymentService;
//import java.util.HashMap;
//import java.util.Map;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/payments")
//public class PaymentGatewayResource {
//
//    private final PaymentService paymentService;
//
//    public PaymentGatewayResource(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    @PostMapping("/create-order")
//    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> payload) {
//        try {
//            int amount = (int) payload.getOrDefault("amount", 0);
//            String currency = (String) payload.getOrDefault("currency", "INR");
//            String receipt = (String) payload.getOrDefault("receipt", "receipt#1");
//            com.razorpay.Order order = paymentService.createOrder(amount, currency, receipt);
//            Map<String, Object> response = new HashMap<>();
//            response.put("id", order.get("id"));
//            response.put("amount", order.get("amount"));
//            response.put("currency", order.get("currency"));
//            response.put("receipt", order.get("receipt"));
//            response.put("status", order.get("status"));
//            return ResponseEntity.ok(response);
//        } catch (com.razorpay.RazorpayException e) {
//            return ResponseEntity.status(500).body(e.getMessage());
//        }
//    }
//
//    // Placeholder for payment verification endpoint
//    @PostMapping("/verify")
//    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, Object> payload) {
//        // TODO: Implement payment verification logic
//        return ResponseEntity.ok("Verification endpoint not implemented yet");
//    }
//}
