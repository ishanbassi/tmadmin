package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.UserEventsTrackingAsserts.*;
import static com.bassi.tmapp.domain.UserEventsTrackingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserEventsTrackingMapperTest {

    private UserEventsTrackingMapper userEventsTrackingMapper;

    @BeforeEach
    void setUp() {
        userEventsTrackingMapper = new UserEventsTrackingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserEventsTrackingSample1();
        var actual = userEventsTrackingMapper.toEntity(userEventsTrackingMapper.toDto(expected));
        assertUserEventsTrackingAllPropertiesEquals(expected, actual);
    }
}
