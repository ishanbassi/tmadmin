package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class LeadTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Lead getLeadSample1() {
        return new Lead()
            .id(1L)
            .fullName("fullName1")
            .phoneNumber("phoneNumber1")
            .email("email1")
            .city("city1")
            .brandName("brandName1")
            .selectedPackage("selectedPackage1")
            .tmClass(1)
            .comments("comments1")
            .leadSource("leadSource1");
    }

    public static Lead getLeadSample2() {
        return new Lead()
            .id(2L)
            .fullName("fullName2")
            .phoneNumber("phoneNumber2")
            .email("email2")
            .city("city2")
            .brandName("brandName2")
            .selectedPackage("selectedPackage2")
            .tmClass(2)
            .comments("comments2")
            .leadSource("leadSource2");
    }

    public static Lead getLeadRandomSampleGenerator() {
        return new Lead()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .brandName(UUID.randomUUID().toString())
            .selectedPackage(UUID.randomUUID().toString())
            .tmClass(intCount.incrementAndGet())
            .comments(UUID.randomUUID().toString())
            .leadSource(UUID.randomUUID().toString());
    }
}
