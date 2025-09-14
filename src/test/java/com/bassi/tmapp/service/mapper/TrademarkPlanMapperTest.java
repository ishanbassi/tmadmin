package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TrademarkPlanAsserts.*;
import static com.bassi.tmapp.domain.TrademarkPlanTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrademarkPlanMapperTest {

    private TrademarkPlanMapper trademarkPlanMapper;

    @BeforeEach
    void setUp() {
        trademarkPlanMapper = new TrademarkPlanMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTrademarkPlanSample1();
        var actual = trademarkPlanMapper.toEntity(trademarkPlanMapper.toDto(expected));
        assertTrademarkPlanAllPropertiesEquals(expected, actual);
    }
}
