package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.service.webScraping.OtpWaitingService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/otp")
public class OtpController {

    @Autowired
    private OtpWaitingService otpWaitingService;

    // You call this from your phone/Postman after receiving OTP
    @PostMapping("/submit")
    public ResponseEntity<Map<String, String>> submitOtp(@RequestBody OtpRequest request) {
        boolean success = otpWaitingService.submitOtp(request.getPhoneNumber(), request.getOtp());

        if (success) {
            return ResponseEntity.ok(Map.of("message", "OTP accepted, automation continuing..."));
        } else {
            return ResponseEntity.status(404).body(Map.of("message", "No pending OTP request for this number"));
        }
    }

    // Optional: check if server is waiting for OTP
    @GetMapping("/status/{phoneNumber}")
    public ResponseEntity<String> checkStatus(@PathVariable String phoneNumber) {
        boolean waiting = otpWaitingService.hasPendingOtp(phoneNumber);
        return ResponseEntity.ok(waiting ? "WAITING" : "NOT_WAITING");
    }
}

// Request DTO
class OtpRequest {

    private String phoneNumber;
    private String otp;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
