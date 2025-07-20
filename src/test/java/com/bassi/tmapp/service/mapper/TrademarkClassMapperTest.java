package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TrademarkClassAsserts.*;
import static com.bassi.tmapp.domain.TrademarkClassTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrademarkClassMapperTest {

    private TrademarkClassMapper trademarkClassMapper;

    @BeforeEach
    void setUp() {
        trademarkClassMapper = new TrademarkClassMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrademarkClassSample1();
        var actual = trademarkClassMapper.toEntity(trademarkClassMapper.toDto(expected));
        assertTrademarkClassAllPropertiesEquals(expected, actual);
    }
}
