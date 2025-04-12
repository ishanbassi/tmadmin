package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmployeeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Employee getEmployeeSample1() {
        return new Employee().id(1L).fullName("fullName1").phoneNumber("phoneNumber1").email("email1").designation("designation1");
    }

    public static Employee getEmployeeSample2() {
        return new Employee().id(2L).fullName("fullName2").phoneNumber("phoneNumber2").email("email2").designation("designation2");
    }

    public static Employee getEmployeeRandomSampleGenerator() {
        return new Employee()
            .id(longCount.incrementAndGet())
            .fullName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .designation(UUID.randomUUID().toString());
    }
}
