package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.Trademark;

public class TrademarkSimiliarityResultDTO {

    public TrademarkSimiliarityResultDTO(
        Trademark clientTrademark,
        TrademarkSimilarityCandidateDto candidateTrademark,
        double score,
        Integer journalNum
    ) {
        this.clientTrademark = clientTrademark;
        this.candidateTrademark = candidateTrademark;
        this.score = score;
        this.journalNum = journalNum;
    }

    private Trademark clientTrademark;
    private TrademarkSimilarityCandidateDto candidateTrademark;
    private double score;
    private Integer journalNum;

    public Trademark getClientTrademark() {
        return clientTrademark;
    }

    public void setClientTrademark(Trademark clientTrademark) {
        this.clientTrademark = clientTrademark;
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

    public TrademarkSimilarityCandidateDto getCandidateTrademark() {
        return candidateTrademark;
    }

    public void setCandidateTrademark(TrademarkSimilarityCandidateDto candidateTrademark) {
        this.candidateTrademark = candidateTrademark;
    }
}
