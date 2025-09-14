package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TrademarkPlanTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TrademarkPlan getTrademarkPlanSample1() {
        return new TrademarkPlan().id(1L).name("name1").notes("notes1");
    }

    public static TrademarkPlan getTrademarkPlanSample2() {
        return new TrademarkPlan().id(2L).name("name2").notes("notes2");
    }

    public static TrademarkPlan getTrademarkPlanRandomSampleGenerator() {
        return new TrademarkPlan().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString()).notes(UUID.randomUUID().toString());
    }
}
