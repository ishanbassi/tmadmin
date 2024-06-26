package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TmAgentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TmAgent getTmAgentSample1() {
        return new TmAgent().id(1L).agentCode(1).firstName("firstName1").lastName("lastName1").address("address1");
    }

    public static TmAgent getTmAgentSample2() {
        return new TmAgent().id(2L).agentCode(2).firstName("firstName2").lastName("lastName2").address("address2");
    }

    public static TmAgent getTmAgentRandomSampleGenerator() {
        return new TmAgent()
            .id(longCount.incrementAndGet())
            .agentCode(intCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString());
    }
}
