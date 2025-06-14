package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CompanyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Company getCompanySample1() {
        return new Company()
            .id(1L)
            .type("type1")
            .name("name1")
            .cin("cin1")
            .gstin("gstin1")
            .natureOfBusiness("natureOfBusiness1")
            .address("address1")
            .state("state1")
            .pincode("pincode1")
            .city("city1");
    }

    public static Company getCompanySample2() {
        return new Company()
            .id(2L)
            .type("type2")
            .name("name2")
            .cin("cin2")
            .gstin("gstin2")
            .natureOfBusiness("natureOfBusiness2")
            .address("address2")
            .state("state2")
            .pincode("pincode2")
            .city("city2");
    }

    public static Company getCompanyRandomSampleGenerator() {
        return new Company()
            .id(longCount.incrementAndGet())
            .type(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .cin(UUID.randomUUID().toString())
            .gstin(UUID.randomUUID().toString())
            .natureOfBusiness(UUID.randomUUID().toString())
            .address(UUID.randomUUID().toString())
            .state(UUID.randomUUID().toString())
            .pincode(UUID.randomUUID().toString())
            .city(UUID.randomUUID().toString());
    }
}
