package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.TrademarkType;

public interface TrademarkSuggestionInterface {
    Long getId();
    String getName();
    String getDetails();
    Long getApplicationNo();
    Integer getTmClass();
    TrademarkType getType();
}
