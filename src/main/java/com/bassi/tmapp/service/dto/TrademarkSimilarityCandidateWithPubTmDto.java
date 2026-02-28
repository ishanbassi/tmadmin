package com.bassi.tmapp.service.dto;

public class TrademarkSimilarityCandidateWithPubTmDto {

    private Long clientId;
    private String clientTrademark;
    private String clientNormalizedTrademark;
    private Long publishedId;
    private String publishedTrademark;
    private String publishedNormalizedTrademark;
    private Long publishedApplicationNo;
    private Integer publishedTrademarkClass;
    private String publishedImgUrl;
    private String proprietorName;
    private String proprietorAddress;
    private String details;
    private double score;
    private Integer journalNum;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public Long getPublishedId() {
        return publishedId;
    }

    public void setPublishedId(Long publishedId) {
        this.publishedId = publishedId;
    }

    public String getClientTrademark() {
        return clientTrademark;
    }

    public void setClientTrademark(String clientTrademark) {
        this.clientTrademark = clientTrademark;
    }

    public String getPublishedTrademark() {
        return publishedTrademark;
    }

    public void setPublishedTrademark(String publishedTrademark) {
        this.publishedTrademark = publishedTrademark;
    }

    public String getClientNormalizedTrademark() {
        return clientNormalizedTrademark;
    }

    public void setClientNormalizedTrademark(String clientNormalizedTrademark) {
        this.clientNormalizedTrademark = clientNormalizedTrademark;
    }

    public String getPublishedNormalizedTrademark() {
        return publishedNormalizedTrademark;
    }

    public void setPublishedNormalizedTrademark(String publishedNormalizedTrademark) {
        this.publishedNormalizedTrademark = publishedNormalizedTrademark;
    }

    public Long getPublishedApplicationNo() {
        return publishedApplicationNo;
    }

    public void setPublishedApplicationNo(Long publishedApplicationNo) {
        this.publishedApplicationNo = publishedApplicationNo;
    }

    public Integer getPublishedTrademarkClass() {
        return publishedTrademarkClass;
    }

    public void setPublishedTrademarkClass(Integer publishedTrademarkClass) {
        this.publishedTrademarkClass = publishedTrademarkClass;
    }

    public String getPublishedImgUrl() {
        return publishedImgUrl;
    }

    public void setPublishedImgUrl(String publishedImgUrl) {
        this.publishedImgUrl = publishedImgUrl;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorAddress() {
        return proprietorAddress;
    }

    public void setProprietorAddress(String proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public Integer getJournalNum() {
        return journalNum;
    }

    public void setJournalNum(Integer journalNum) {
        this.journalNum = journalNum;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public TrademarkSimilarityCandidateWithPubTmDto(
        Long clientId,
        String clientTrademark,
        String clientNormalizedTrademark,
        Long publishedId,
        String publishedTrademark,
        String publishedNormalizedTrademark,
        Long publishedApplicationNo,
        Integer publishedTrademarkClass,
        String publishedImgUrl,
        String proprietorName,
        String proprietorAddress,
        String details
    ) {
        this.clientId = clientId;
        this.clientTrademark = clientTrademark;
        this.clientNormalizedTrademark = clientNormalizedTrademark;
        this.publishedId = publishedId;
        this.publishedTrademark = publishedTrademark;
        this.publishedNormalizedTrademark = publishedNormalizedTrademark;
        this.publishedApplicationNo = publishedApplicationNo;
        this.publishedTrademarkClass = publishedTrademarkClass;
        this.publishedImgUrl = publishedImgUrl;
        this.proprietorName = proprietorName;
        this.proprietorAddress = proprietorAddress;
        this.details = details;
    }
}
