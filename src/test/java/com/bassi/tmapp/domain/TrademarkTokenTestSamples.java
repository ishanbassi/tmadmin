package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrademarkTokenTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrademarkToken getTrademarkTokenSample1() {
        return new TrademarkToken().id(1L).tokenText("tokenText1").position(1);
    }

    public static TrademarkToken getTrademarkTokenSample2() {
        return new TrademarkToken().id(2L).tokenText("tokenText2").position(2);
    }

    public static TrademarkToken getTrademarkTokenRandomSampleGenerator() {
        return new TrademarkToken()
            .id(longCount.incrementAndGet())
            .tokenText(UUID.randomUUID().toString())
            .position(intCount.incrementAndGet());
    }
}
