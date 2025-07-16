package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrademarkClassTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static TrademarkClass getTrademarkClassSample1() {
        return new TrademarkClass().id(1L).code(1).tmClass(1).keyword("keyword1").title("title1").description("description1");
    }

    public static TrademarkClass getTrademarkClassSample2() {
        return new TrademarkClass().id(2L).code(2).tmClass(2).keyword("keyword2").title("title2").description("description2");
    }

    public static TrademarkClass getTrademarkClassRandomSampleGenerator() {
        return new TrademarkClass()
            .id(longCount.incrementAndGet())
            .code(intCount.incrementAndGet())
            .tmClass(intCount.incrementAndGet())
            .keyword(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
