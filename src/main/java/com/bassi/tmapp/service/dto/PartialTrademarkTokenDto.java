package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;

public class PartialTrademarkTokenDto {

    public PartialTrademarkTokenDto(String tokenText, TrademarkTokenType tokenType, Integer position, Long trademarkId) {
        this.tokenText = tokenText;
        this.tokenType = tokenType;
        this.position = position;
        this.trademarkId = trademarkId;
    }

    public PartialTrademarkTokenDto() {}

    private String tokenText;

    private TrademarkTokenType tokenType;

    private Integer position;

    private Long trademarkId;

    public String getTokenText() {
        return tokenText;
    }

    public void setTokenText(String tokenText) {
        this.tokenText = tokenText;
    }

    public TrademarkTokenType getTokenType() {
        return tokenType;
    }

    public void setTokenType(TrademarkTokenType tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getTrademarkId() {
        return trademarkId;
    }

    public void setTrademarkId(Long trademarkId) {
        this.trademarkId = trademarkId;
    }
}
