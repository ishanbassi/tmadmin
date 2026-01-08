package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TrademarkTokenAsserts.*;
import static com.bassi.tmapp.domain.TrademarkTokenTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrademarkTokenMapperTest {

    private TrademarkTokenMapper trademarkTokenMapper;

    @BeforeEach
    void setUp() {
        trademarkTokenMapper = new TrademarkTokenMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrademarkTokenSample1();
        var actual = trademarkTokenMapper.toEntity(trademarkTokenMapper.toDto(expected));
        assertTrademarkTokenAllPropertiesEquals(expected, actual);
    }
}
