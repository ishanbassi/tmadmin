package com.bassi.tmapp.service.dto;

public class MatchingTrademarkDto {

    private String matchingTrademark;
    private String registeredTrademark;
    private Integer tmClass;
    private Long applicationNo;
    private String details;
    private Integer journalNo;
    private String proprietorName;
    private String proprietorAddress;
    private String agentName;
    private String agentAddress;
    private Integer distance;

    public MatchingTrademarkDto(
        String matchingTrademark,
        String registeredTrademark,
        Integer tmClass,
        Long applicationNo,
        String details,
        Integer journalNo,
        String proprietorName,
        String proprietorAddress,
        String agentName,
        String agentAddress,
        Integer distance
    ) {
        this.matchingTrademark = matchingTrademark;
        this.registeredTrademark = registeredTrademark;
        this.tmClass = tmClass;
        this.applicationNo = applicationNo;
        this.details = details;
        this.journalNo = journalNo;
        this.proprietorName = proprietorName;
        this.proprietorAddress = proprietorAddress;
        this.agentName = agentName;
        this.agentAddress = agentAddress;
        this.distance = distance;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getMatchingTrademark() {
        return matchingTrademark;
    }

    public void setMatchingTrademark(String matchingTrademark) {
        this.matchingTrademark = matchingTrademark;
    }

    public String getRegisteredTrademark() {
        return registeredTrademark;
    }

    public void setRegisteredTrademark(String registeredTrademark) {
        this.registeredTrademark = registeredTrademark;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public Long getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getJournalNo() {
        return journalNo;
    }

    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
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

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return (
            "MatchingTrademarkDto [matchingTrademark=" +
            matchingTrademark +
            ", registeredTrademark=" +
            registeredTrademark +
            ", tmClass=" +
            tmClass +
            ", applicationNo=" +
            applicationNo +
            ", details=" +
            details +
            ", journalNo=" +
            journalNo +
            ", proprietorName=" +
            proprietorName +
            ", proprietorAddress=" +
            proprietorAddress +
            ", agentName=" +
            agentName +
            ", agentAddress=" +
            agentAddress +
            ", distance=" +
            distance +
            "]"
        );
    }
}
