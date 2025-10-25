package com.bassi.tmapp.service.dto;

import java.math.BigDecimal;
import java.util.List;

public class TrademarkOrderSummary {

    private TrademarkDTO trademarkDTO;
    private LeadDTO leadDTO;
    private PaymentDTO paymentDTO;
    private List<OrderSummary> orderSummaries;
    private BigDecimal totalFees;
    private DocumentsDTO documentsDTO;
    private UserProfileDTO userProfileDTO;

    public UserProfileDTO getUserProfileDTO() {
        return userProfileDTO;
    }

    public void setUserProfileDTO(UserProfileDTO userProfileDTO) {
        this.userProfileDTO = userProfileDTO;
    }

    public DocumentsDTO getDocumentsDTO() {
        return documentsDTO;
    }

    public void setDocumentsDTO(DocumentsDTO documentsDTO) {
        this.documentsDTO = documentsDTO;
    }

    public LeadDTO getLeadDTO() {
        return leadDTO;
    }

    public void setLeadDTO(LeadDTO leadDTO) {
        this.leadDTO = leadDTO;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    public void setPaymentDTO(PaymentDTO paymentDTO) {
        this.paymentDTO = paymentDTO;
    }

    public TrademarkDTO getTrademarkDTO() {
        return trademarkDTO;
    }

    public void setTrademarkDTO(TrademarkDTO trademarkDTO) {
        this.trademarkDTO = trademarkDTO;
    }

    public List<OrderSummary> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(List<OrderSummary> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    public BigDecimal getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(BigDecimal totalFees) {
        this.totalFees = totalFees;
    }

    public static class OrderSummary {

        private Integer tmClass;
        private BigDecimal fees;

        public Integer getTmClass() {
            return tmClass;
        }

        public void setTmClass(Integer tmClass) {
            this.tmClass = tmClass;
        }

        public BigDecimal getFees() {
            return fees;
        }

        public void setFees(BigDecimal fees) {
            this.fees = fees;
        }
    }

    @Override
    public String toString() {
        return "TrademarkOrderSummary [trademarkDTO=" + trademarkDTO + ", orderSummaries=" + orderSummaries + "]";
    }
}
