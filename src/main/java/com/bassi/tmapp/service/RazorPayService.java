package com.bassi.tmapp.service;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.service.dto.CreateOrderRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RazorPayService {

    private final ApplicationProperties applicationProperties;
    private final TrademarkService trademarkService;

    RazorPayService(ApplicationProperties applicationProperties, TrademarkService trademarkService) {
        this.applicationProperties = applicationProperties;
        this.trademarkService = trademarkService;
    }

    public Order createOrder(CreateOrderRequest request) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(
            applicationProperties.getRazorPay().getKeyId(),
            applicationProperties.getRazorPay().getKeySecret()
        );
        JSONObject orderRequest = new JSONObject();
        int amount = trademarkService.calculateTotalFees();
        orderRequest.put("amount", amount);
        orderRequest.put("currency", request.getCurrency());
        orderRequest.put("receipt", "receipt");
        return client.orders.create(orderRequest);
    }
}
