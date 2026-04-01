package com.bassi.tmapp.service.dto;

import java.time.ZonedDateTime;

public interface JournalStatsDto {
    Integer getJournalNo();
    Long getCount();
    ZonedDateTime getPublishedDate();
}
