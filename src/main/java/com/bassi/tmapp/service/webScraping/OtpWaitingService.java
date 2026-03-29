package com.bassi.tmapp.service.webScraping;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class OtpWaitingService {

    // Stores a CompletableFuture per phone number
    private final Map<String, CompletableFuture<String>> pendingOtps = new ConcurrentHashMap<>();
    private static final Logger log = LoggerFactory.getLogger(OtpWaitingService.class);

    // Called by Selenium flow — blocks until OTP is submitted
    public String waitForOtp(String phoneNumber, int timeoutSeconds) throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        pendingOtps.put(phoneNumber, future);

        log.info("Waiting for OTP for: " + phoneNumber);

        try {
            return future.get(timeoutSeconds, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new RuntimeException("OTP timeout for: " + phoneNumber);
        } finally {
            pendingOtps.remove(phoneNumber);
        }
    }

    // Called by REST endpoint when you submit OTP
    public boolean submitOtp(String optReceiverAddress, String otp) {
        CompletableFuture<String> future = pendingOtps.get(optReceiverAddress);
        if (future != null) {
            future.complete(otp);
            return true;
        }
        return false; // no pending request for this number
    }

    public boolean hasPendingOtp(String optReceiverAddress) {
        return pendingOtps.containsKey(optReceiverAddress);
    }
}
