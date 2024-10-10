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
            .firstName("firstName1")
            .lastName("lastName1")
            .address("address1")
            .companyName("companyName1")
            .agentCode("agentCode1")
            .email("email1");
    }

    public static TmAgent getTmAgentSample2() {
        return new TmAgent()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .address("address2")
            .companyName("companyName2")
            .agentCode("agentCode2")
            .email("email2");
    }

    public static TmAgent getTmAgentRandomSampleGenerator() {
        return new TmAgent()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .companyName(UUID.randomUUID().toString())
            .agentCode(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString());
    }
}
