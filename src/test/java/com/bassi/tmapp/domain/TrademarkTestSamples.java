package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TrademarkTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Trademark getTrademarkSample1() {
        return new Trademark()
            .id(1L)
            .name("name1")
            .details("details1")
            .applicationNo(1L)
            .agentName("agentName1")
            .agentAddress("agentAddress1")
            .proprietorName("proprietorName1")
            .proprietorAddress("proprietorAddress1")
            .imgUrl("imgUrl1")
            .tmClass(1)
            .journalNo(1)
            .usage("usage1")
            .associatedTms("associatedTms1")
            .pageNo(1);
    }

    public static Trademark getTrademarkSample2() {
        return new Trademark()
            .id(2L)
            .name("name2")
            .details("details2")
            .applicationNo(2L)
            .agentName("agentName2")
            .agentAddress("agentAddress2")
            .proprietorName("proprietorName2")
            .proprietorAddress("proprietorAddress2")
            .imgUrl("imgUrl2")
            .tmClass(2)
            .journalNo(2)
            .usage("usage2")
            .associatedTms("associatedTms2")
            .pageNo(2);
    }

    public static Trademark getTrademarkRandomSampleGenerator() {
        return new Trademark()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .details(UUID.randomUUID().toString())
            .applicationNo(longCount.incrementAndGet())
            .agentName(UUID.randomUUID().toString())
            .agentAddress(UUID.randomUUID().toString())
            .proprietorName(UUID.randomUUID().toString())
            .proprietorAddress(UUID.randomUUID().toString())
            .imgUrl(UUID.randomUUID().toString())
            .tmClass(intCount.incrementAndGet())
            .journalNo(intCount.incrementAndGet())
            .usage(UUID.randomUUID().toString())
            .associatedTms(UUID.randomUUID().toString())
            .pageNo(intCount.incrementAndGet());
    }
}
