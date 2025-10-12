package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class UserProfileTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static UserProfile getUserProfileSample1() {
        return new UserProfile()
            .id(1L)
            .firstName("firstName1")
            .lastName("lastName1")
            .email("email1")
            .phoneNumber("phoneNumber1")
            .addressLine1("addressLine11")
            .addressLine2("addressLine21")
            .city("city1")
            .zipCode(1)
            .state("state1")
            .utmCampaign("utmCampaign1")
            .utmSource("utmSource1")
            .utmMedium("utmMedium1")
            .utmContent("utmContent1");
    }

    public static UserProfile getUserProfileSample2() {
        return new UserProfile()
            .id(2L)
            .firstName("firstName2")
            .lastName("lastName2")
            .email("email2")
            .phoneNumber("phoneNumber2")
            .addressLine1("addressLine12")
            .addressLine2("addressLine22")
            .city("city2")
            .zipCode(2)
            .state("state2")
            .utmCampaign("utmCampaign2")
            .utmSource("utmSource2")
            .utmMedium("utmMedium2")
            .utmContent("utmContent2");
    }

    public static UserProfile getUserProfileRandomSampleGenerator() {
        return new UserProfile()
            .id(longCount.incrementAndGet())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .addressLine1(UUID.randomUUID().toString())
            .addressLine2(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString())
            .zipCode(intCount.incrementAndGet())
            .state(UUID.randomUUID().toString())
            .utmCampaign(UUID.randomUUID().toString())
            .utmSource(UUID.randomUUID().toString())
            .utmMedium(UUID.randomUUID().toString())
            .utmContent(UUID.randomUUID().toString());
    }
}
