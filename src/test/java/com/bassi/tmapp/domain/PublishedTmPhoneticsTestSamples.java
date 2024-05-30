package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PublishedTmPhoneticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PublishedTmPhonetics getPublishedTmPhoneticsSample1() {
        return new PublishedTmPhonetics().id(1L).sanitizedTm("sanitizedTm1").phoneticPk("phoneticPk1").phoneticSk("phoneticSk1");
    }

    public static PublishedTmPhonetics getPublishedTmPhoneticsSample2() {
        return new PublishedTmPhonetics().id(2L).sanitizedTm("sanitizedTm2").phoneticPk("phoneticPk2").phoneticSk("phoneticSk2");
    }

    public static PublishedTmPhonetics getPublishedTmPhoneticsRandomSampleGenerator() {
        return new PublishedTmPhonetics()
            .id(longCount.incrementAndGet())
            .sanitizedTm(UUID.randomUUID().toString())
            .phoneticPk(UUID.randomUUID().toString())
            .phoneticSk(UUID.randomUUID().toString());
    }
}
