package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.Trademark;

public class TrademarkSimiliarityResultDTO {

    public TrademarkSimiliarityResultDTO(Trademark clientTrademark, Trademark publishedTrademark, double score, Integer journalNum) {
        this.clientTrademark = clientTrademark;
        this.publishedTradmark = publishedTrademark;
        this.score = score;
        this.journalNum = journalNum;
    }

    private Trademark clientTrademark;
    private Trademark publishedTradmark;
    private double score;
    private Integer journalNum;

    public Trademark getClientTrademark() {
        return clientTrademark;
    }

    public void setClientTrademark(Trademark clientTrademark) {
        this.clientTrademark = clientTrademark;
    }

    public Trademark getPublishedTradmark() {
        return publishedTradmark;
    }

    public void setPublishedTradmark(Trademark publishedTradmark) {
        this.publishedTradmark = publishedTradmark;
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
}
