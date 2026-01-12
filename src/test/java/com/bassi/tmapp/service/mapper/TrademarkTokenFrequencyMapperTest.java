package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TrademarkTokenFrequencyAsserts.*;
import static com.bassi.tmapp.domain.TrademarkTokenFrequencyTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrademarkTokenFrequencyMapperTest {

    private TrademarkTokenFrequencyMapper trademarkTokenFrequencyMapper;

    @BeforeEach
    void setUp() {
        trademarkTokenFrequencyMapper = new TrademarkTokenFrequencyMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrademarkTokenFrequencySample1();
        var actual = trademarkTokenFrequencyMapper.toEntity(trademarkTokenFrequencyMapper.toDto(expected));
        assertTrademarkTokenFrequencyAllPropertiesEquals(expected, actual);
    }
}
