//package com.bassi.tmapp.service;
//
//
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PaymentGatewayService {
//
//    @Value("${razorpay.keyId}")
//    private String keyId;
//
//    @Value("${razorpay.keySecret}")
//    private String keySecret;
//
//    public Order createOrder(int amount, String currency, String receipt) throws RazorpayException {
//        RazorpayClient client = new RazorpayClient(keyId, keySecret);
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", amount); // amount in paise
//        orderRequest.put("currency", currency);
//        orderRequest.put("receipt", receipt);
//        orderRequest.put("payment_capture", 1);
//        return client.orders.create(orderRequest);
//    }
//    // Add more methods for payment verification as needed
//}
//
