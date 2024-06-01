package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TrademarkAsserts.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrademarkMapperTest {

    private TrademarkMapper trademarkMapper;

    @BeforeEach
    void setUp() {
        trademarkMapper = new TrademarkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrademarkSample1();
        var actual = trademarkMapper.toEntity(trademarkMapper.toDto(expected));
        assertTrademarkAllPropertiesEquals(expected, actual);
    }
}
