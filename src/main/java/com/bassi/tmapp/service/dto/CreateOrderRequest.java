package com.bassi.tmapp.service.dto;

public class CreateOrderRequest {

    private TrademarkDTO trademarkDTO;
    private String currency = "INR";
    private PaymentDTO paymentDTO;

    public TrademarkDTO getTrademarkDTO() {
        return trademarkDTO;
    }

    public void setTrademarkDTO(TrademarkDTO trademarkDTO) {
        this.trademarkDTO = trademarkDTO;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentDTO getPaymentDTO() {
        return paymentDTO;
    }

    public void setPaymentDTO(PaymentDTO paymentDTO) {
        this.paymentDTO = paymentDTO;
    }

    @Override
    public String toString() {
        return (
            "CreateOrderRequest [trademarkDTO=" +
            trademarkDTO +
            ", currency=" +
            currency +
            ", paymentDTO=" +
            paymentDTO +
            ", getTrademarkDTO()=" +
            getTrademarkDTO() +
            ", getCurrency()=" +
            getCurrency() +
            ", getPaymentDTO()=" +
            getPaymentDTO() +
            ", getClass()=" +
            getClass() +
            ", hashCode()=" +
            hashCode() +
            ", toString()=" +
            super.toString() +
            "]"
        );
    }
}
