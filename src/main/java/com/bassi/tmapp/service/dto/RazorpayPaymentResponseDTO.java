package com.bassi.tmapp.service.dto;

import java.util.List;
import java.util.Map;

public class RazorpayPaymentResponseDTO {

    private String id;
    private String entity;
    private Integer amount;
    private String currency;
    private String status;
    private String orderId;
    private String invoiceId;
    private Boolean international;
    private String method;
    private Integer amountRefunded;
    private String refundStatus;
    private Boolean captured;
    private String description;
    private String cardId;
    private String bank;
    private String wallet;
    private String vpa;
    private String email;
    private String contact;
    private List<String> notes; // notes array, can be strings
    private Integer fee;
    private Integer tax;
    private String errorCode;
    private String errorDescription;
    private String errorSource;
    private String errorStep;
    private String errorReason;
    private AcquirerData acquirerData;
    private Long createdAt;
    private Upi upi;

    // Inner DTO for acquirer_data
    public static class AcquirerData {

        private String rrn;

        public String getRrn() {
            return rrn;
        }

        public void setRrn(String rrn) {
            this.rrn = rrn;
        }
    }

    // Inner DTO for upi
    public static class Upi {

        private String payerAccountType;
        private String vpa;
        private String flow;

        public String getPayerAccountType() {
            return payerAccountType;
        }

        public void setPayerAccountType(String payerAccountType) {
            this.payerAccountType = payerAccountType;
        }

        public String getVpa() {
            return vpa;
        }

        public void setVpa(String vpa) {
            this.vpa = vpa;
        }

        public String getFlow() {
            return flow;
        }

        public void setFlow(String flow) {
            this.flow = flow;
        }
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Boolean getInternational() {
        return international;
    }

    public void setInternational(Boolean international) {
        this.international = international;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getAmountRefunded() {
        return amountRefunded;
    }

    public void setAmountRefunded(Integer amountRefunded) {
        this.amountRefunded = amountRefunded;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public Boolean getCaptured() {
        return captured;
    }

    public void setCaptured(Boolean captured) {
        this.captured = captured;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getVpa() {
        return vpa;
    }

    public void setVpa(String vpa) {
        this.vpa = vpa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public Integer getFee() {
        return fee;
    }

    public void setFee(Integer fee) {
        this.fee = fee;
    }

    public Integer getTax() {
        return tax;
    }

    public void setTax(Integer tax) {
        this.tax = tax;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getErrorSource() {
        return errorSource;
    }

    public void setErrorSource(String errorSource) {
        this.errorSource = errorSource;
    }

    public String getErrorStep() {
        return errorStep;
    }

    public void setErrorStep(String errorStep) {
        this.errorStep = errorStep;
    }

    public String getErrorReason() {
        return errorReason;
    }

    public void setErrorReason(String errorReason) {
        this.errorReason = errorReason;
    }

    public AcquirerData getAcquirerData() {
        return acquirerData;
    }

    public void setAcquirerData(AcquirerData acquirerData) {
        this.acquirerData = acquirerData;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Upi getUpi() {
        return upi;
    }

    public void setUpi(Upi upi) {
        this.upi = upi;
    }

    @Override
    public String toString() {
        return (
            "RazorpayPaymentResponseDTO [id=" +
            id +
            ", entity=" +
            entity +
            ", amount=" +
            amount +
            ", currency=" +
            currency +
            ", status=" +
            status +
            ", orderId=" +
            orderId +
            ", invoiceId=" +
            invoiceId +
            ", international=" +
            international +
            ", method=" +
            method +
            ", amountRefunded=" +
            amountRefunded +
            ", refundStatus=" +
            refundStatus +
            ", captured=" +
            captured +
            ", description=" +
            description +
            ", cardId=" +
            cardId +
            ", bank=" +
            bank +
            ", wallet=" +
            wallet +
            ", vpa=" +
            vpa +
            ", email=" +
            email +
            ", contact=" +
            contact +
            ", notes=" +
            notes +
            ", fee=" +
            fee +
            ", tax=" +
            tax +
            ", errorCode=" +
            errorCode +
            ", errorDescription=" +
            errorDescription +
            ", errorSource=" +
            errorSource +
            ", errorStep=" +
            errorStep +
            ", errorReason=" +
            errorReason +
            ", acquirerData=" +
            acquirerData +
            ", createdAt=" +
            createdAt +
            ", upi=" +
            upi +
            "]"
        );
    }
}
