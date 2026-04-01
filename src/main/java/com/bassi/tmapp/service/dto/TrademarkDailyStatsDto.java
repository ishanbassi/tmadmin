package com.bassi.tmapp.service.dto;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

public class TrademarkDailyStatsDto {

    private Long applicationsFiled;
    private Timestamp lastUpdated;
    private List<TrademarkSuggestionDto> recentFilings;
    private JournalStatsDto journalStatsDto;
    private Long totalTrademarks;

    public Long getApplicationsFiled() {
        return applicationsFiled;
    }

    public void setApplicationsFiled(Long applicationsFiled) {
        this.applicationsFiled = applicationsFiled;
    }

    public List<TrademarkSuggestionDto> getRecentFilings() {
        return recentFilings;
    }

    public void setRecentFilings(List<TrademarkSuggestionDto> recentFilings) {
        this.recentFilings = recentFilings;
    }

    public JournalStatsDto getJournalStatsDto() {
        return journalStatsDto;
    }

    public void setJournalStatsDto(JournalStatsDto journalStatsDto) {
        this.journalStatsDto = journalStatsDto;
    }

    public Long getTotalTrademarks() {
        return totalTrademarks;
    }

    public void setTotalTrademarks(Long totalTrademarks) {
        this.totalTrademarks = totalTrademarks;
    }

    public Timestamp getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Timestamp lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
