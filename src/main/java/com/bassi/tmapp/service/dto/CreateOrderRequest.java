package com.bassi.tmapp.service.dto;

public class CreateOrderRequest {

    private TrademarkDTO trademarkDTO;
    private String currency = "INR";

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
}
