package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.PhoneticsAsserts.*;
import static com.bassi.tmapp.domain.PhoneticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PhoneticsMapperTest {

    private PhoneticsMapper phoneticsMapper;

    @BeforeEach
    void setUp() {
        phoneticsMapper = new PhoneticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPhoneticsSample1();
        var actual = phoneticsMapper.toEntity(phoneticsMapper.toDto(expected));
        assertPhoneticsAllPropertiesEquals(expected, actual);
    }
}
