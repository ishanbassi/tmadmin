package com.bassi.tmapp.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RazorPayOrderResponse {

    @JsonProperty("razorpay_order_id")
    private String razorpayOrderId;

    @JsonProperty("razorpay_payment_id")
    private String razorpayPaymentId;

    @JsonProperty("razorpay_signature")
    private String razorpaySignature;

    private LeadDTO leadDTO;

    private UserProfileDTO userProfileDTO;

    public UserProfileDTO getUserProfileDTO() {
        return userProfileDTO;
    }

    public void setUserProfileDTO(UserProfileDTO userProfileDTO) {
        this.userProfileDTO = userProfileDTO;
    }

    public LeadDTO getLeadDTO() {
        return leadDTO;
    }

    public void setLeadDTO(LeadDTO leadDTO) {
        this.leadDTO = leadDTO;
    }

    public String getRazorpayOrderId() {
        return razorpayOrderId;
    }

    public void setRazorpayOrderId(String razorpayOrderId) {
        this.razorpayOrderId = razorpayOrderId;
    }

    public String getRazorpayPaymentId() {
        return razorpayPaymentId;
    }

    public void setRazorpayPaymentId(String razorpayPaymentId) {
        this.razorpayPaymentId = razorpayPaymentId;
    }

    public String getRazorpaySignature() {
        return razorpaySignature;
    }

    public void setRazorpaySignature(String razorpaySignature) {
        this.razorpaySignature = razorpaySignature;
    }
}
