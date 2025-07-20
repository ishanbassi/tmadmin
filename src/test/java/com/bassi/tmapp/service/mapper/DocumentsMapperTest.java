package com.bassi.tmapp.service.mapper;

import static com.bassi.tmapp.domain.DocumentsAsserts.*;
import static com.bassi.tmapp.domain.DocumentsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DocumentsMapperTest {

    private DocumentsMapper documentsMapper;

    @BeforeEach
    void setUp() {
        documentsMapper = new DocumentsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDocumentsSample1();
        var actual = documentsMapper.toEntity(documentsMapper.toDto(expected));
        assertDocumentsAllPropertiesEquals(expected, actual);
    }
}
