package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TokenPhoneticTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TokenPhonetic getTokenPhoneticSample1() {
        return new TokenPhonetic().id(1L).phoneticCode("phoneticCode1").secondaryPhoneticCode("secondaryPhoneticCode1");
    }

    public static TokenPhonetic getTokenPhoneticSample2() {
        return new TokenPhonetic().id(2L).phoneticCode("phoneticCode2").secondaryPhoneticCode("secondaryPhoneticCode2");
    }

    public static TokenPhonetic getTokenPhoneticRandomSampleGenerator() {
        return new TokenPhonetic()
            .id(longCount.incrementAndGet())
            .phoneticCode(UUID.randomUUID().toString())
            .secondaryPhoneticCode(UUID.randomUUID().toString());
    }
}
