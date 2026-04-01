package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

public interface TrademarkStatsInterface {
    String getName();
    Long getApplicationNo();
    Integer getTmClass();
    String getImgUrl();
    TrademarkType getType();
    String getTrademarkStatus();
    Timestamp getCreatedDate();
}
