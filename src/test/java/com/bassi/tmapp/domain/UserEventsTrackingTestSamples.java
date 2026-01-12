package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class UserEventsTrackingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserEventsTracking getUserEventsTrackingSample1() {
        return new UserEventsTracking().id(1L).eventType("eventType1").pageName("pageName1").deviceType("deviceType1");
    }

    public static UserEventsTracking getUserEventsTrackingSample2() {
        return new UserEventsTracking().id(2L).eventType("eventType2").pageName("pageName2").deviceType("deviceType2");
    }

    public static UserEventsTracking getUserEventsTrackingRandomSampleGenerator() {
        return new UserEventsTracking()
            .id(longCount.incrementAndGet())
            .eventType(UUID.randomUUID().toString())
            .pageName(UUID.randomUUID().toString())
            .deviceType(UUID.randomUUID().toString());
    }
}
