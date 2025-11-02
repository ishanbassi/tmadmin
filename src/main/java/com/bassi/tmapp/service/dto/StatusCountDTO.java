package com.bassi.tmapp.service.dto;

public class StatusCountDTO {

    private String trademarkStatus;
    private Long count;

    public StatusCountDTO(String trademarkStatus, Long count) {
        this.trademarkStatus = trademarkStatus;
        this.count = count;
    }

    public void setTrademarkStatus(String trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getTrademarkStatus() {
        return trademarkStatus;
    }

    public Long getCount() {
        return count;
    }
}
