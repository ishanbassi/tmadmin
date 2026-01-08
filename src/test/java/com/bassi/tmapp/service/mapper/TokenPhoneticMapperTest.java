package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.TokenPhoneticAsserts.*;
import static com.bassi.tmapp.domain.TokenPhoneticTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TokenPhoneticMapperTest {

    private TokenPhoneticMapper tokenPhoneticMapper;

    @BeforeEach
    void setUp() {
        tokenPhoneticMapper = new TokenPhoneticMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getTokenPhoneticSample1();
        var actual = tokenPhoneticMapper.toEntity(tokenPhoneticMapper.toDto(expected));
        assertTokenPhoneticAllPropertiesEquals(expected, actual);
    }
}
