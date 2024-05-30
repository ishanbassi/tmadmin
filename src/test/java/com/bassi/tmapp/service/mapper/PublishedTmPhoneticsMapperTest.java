package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.PublishedTmPhoneticsAsserts.*;
import static com.bassi.tmapp.domain.PublishedTmPhoneticsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublishedTmPhoneticsMapperTest {

    private PublishedTmPhoneticsMapper publishedTmPhoneticsMapper;

    @BeforeEach
    void setUp() {
        publishedTmPhoneticsMapper = new PublishedTmPhoneticsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPublishedTmPhoneticsSample1();
        var actual = publishedTmPhoneticsMapper.toEntity(publishedTmPhoneticsMapper.toDto(expected));
        assertPublishedTmPhoneticsAllPropertiesEquals(expected, actual);
    }
}
