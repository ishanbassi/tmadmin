package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.PhoneticAlgorithmType;

public class PartialTokenPhoneticDto {

    public PartialTokenPhoneticDto() {}

    public PartialTokenPhoneticDto(PhoneticAlgorithmType algorithm, String phoneticCode, String secondaryPhoneticCode, Long trademarkId) {
        this.algorithm = algorithm;
        this.phoneticCode = phoneticCode;
        this.secondaryPhoneticCode = secondaryPhoneticCode;
        this.trademarkId = trademarkId;
    }

    private PhoneticAlgorithmType algorithm;

    private String phoneticCode;

    private String secondaryPhoneticCode;

    private Long trademarkId;

    public PhoneticAlgorithmType getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(PhoneticAlgorithmType algorithm) {
        this.algorithm = algorithm;
    }

    public String getPhoneticCode() {
        return phoneticCode;
    }

    public void setPhoneticCode(String phoneticCode) {
        this.phoneticCode = phoneticCode;
    }

    public String getSecondaryPhoneticCode() {
        return secondaryPhoneticCode;
    }

    public void setSecondaryPhoneticCode(String secondaryPhoneticCode) {
        this.secondaryPhoneticCode = secondaryPhoneticCode;
    }

    public Long getTrademarkId() {
        return trademarkId;
    }

    public void setTrademarkId(Long trademarkId) {
        this.trademarkId = trademarkId;
    }
}
