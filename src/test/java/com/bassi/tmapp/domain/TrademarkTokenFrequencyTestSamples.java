package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrademarkTokenFrequencyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrademarkTokenFrequency getTrademarkTokenFrequencySample1() {
        return new TrademarkTokenFrequency().id(1L).frequency(1).word("word1");
    }

    public static TrademarkTokenFrequency getTrademarkTokenFrequencySample2() {
        return new TrademarkTokenFrequency().id(2L).frequency(2).word("word2");
    }

    public static TrademarkTokenFrequency getTrademarkTokenFrequencyRandomSampleGenerator() {
        return new TrademarkTokenFrequency()
            .id(longCount.incrementAndGet())
            .frequency(intCount.incrementAndGet())
            .word(UUID.randomUUID().toString());
    }
}
