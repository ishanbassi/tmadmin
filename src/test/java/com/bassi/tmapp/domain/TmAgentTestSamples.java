package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TmAgentTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TmAgent getTmAgentSample1() {
        return new TmAgent()
            .id(1L)
            .agentCode("agentCode1")
            .firstName("firstName1")
            .lastName("lastName1")
            .address("address1")
            .companyName("companyName1");
    }

    public static TmAgent getTmAgentSample2() {
        return new TmAgent()
            .id(2L)
            .agentCode("agentCode2")
            .firstName("firstName2")
            .lastName("lastName2")
            .address("address2")
            .companyName("companyName2");
    }

    public static TmAgent getTmAgentRandomSampleGenerator() {
        return new TmAgent()
            .id(longCount.incrementAndGet())
            .agentCode(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .companyName(UUID.randomUUID().toString());
    }
}
