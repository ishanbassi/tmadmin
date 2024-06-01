package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PhoneticsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Phonetics getPhoneticsSample1() {
        return new Phonetics().id(1L).sanitizedTm("sanitizedTm1").phoneticPk("phoneticPk1").phoneticSk("phoneticSk1");
    }

    public static Phonetics getPhoneticsSample2() {
        return new Phonetics().id(2L).sanitizedTm("sanitizedTm2").phoneticPk("phoneticPk2").phoneticSk("phoneticSk2");
    }

    public static Phonetics getPhoneticsRandomSampleGenerator() {
        return new Phonetics()
            .id(longCount.incrementAndGet())
            .sanitizedTm(UUID.randomUUID().toString())
            .phoneticPk(UUID.randomUUID().toString())
            .phoneticSk(UUID.randomUUID().toString());
    }
}
