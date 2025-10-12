package com.bassi.tmapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DocumentsTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Documents getDocumentsSample1() {
        return new Documents().id(1L).fileContentType("fileContentType1").fileName("fileName1").fileUrl("fileUrl1").status("status1");
    }

    public static Documents getDocumentsSample2() {
        return new Documents().id(2L).fileContentType("fileContentType2").fileName("fileName2").fileUrl("fileUrl2").status("status2");
    }

    public static Documents getDocumentsRandomSampleGenerator() {
        return new Documents()
            .id(longCount.incrementAndGet())
            .fileContentType(UUID.randomUUID().toString())
            .fileName(UUID.randomUUID().toString())
            .fileUrl(UUID.randomUUID().toString())
            .status(UUID.randomUUID().toString());
    }
}
