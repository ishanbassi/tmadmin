package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.PublishedTmAsserts.*;
import static com.bassi.tmapp.domain.PublishedTmTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PublishedTmMapperTest {

    private PublishedTmMapper publishedTmMapper;

    @BeforeEach
    void setUp() {
        publishedTmMapper = new PublishedTmMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPublishedTmSample1();
        var actual = publishedTmMapper.toEntity(publishedTmMapper.toDto(expected));
        assertPublishedTmAllPropertiesEquals(expected, actual);
    }
}
