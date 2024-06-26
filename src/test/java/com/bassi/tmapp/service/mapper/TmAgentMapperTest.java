package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TmAgentAsserts.*;
import static com.bassi.tmapp.domain.TmAgentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TmAgentMapperTest {

    private TmAgentMapper tmAgentMapper;

    @BeforeEach
    void setUp() {
        tmAgentMapper = new TmAgentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTmAgentSample1();
        var actual = tmAgentMapper.toEntity(tmAgentMapper.toDto(expected));
        assertTmAgentAllPropertiesEquals(expected, actual);
    }
}
