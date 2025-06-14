package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.DocumentsTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Documents.class);
        Documents documents1 = getDocumentsSample1();
        Documents documents2 = new Documents();
        assertThat(documents1).isNotEqualTo(documents2);

        documents2.setId(documents1.getId());
        assertThat(documents1).isEqualTo(documents2);

        documents2 = getDocumentsSample2();
        assertThat(documents1).isNotEqualTo(documents2);
    }

    @Test
    void trademarkTest() {
        Documents documents = getDocumentsRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        documents.setTrademark(trademarkBack);
        assertThat(documents.getTrademark()).isEqualTo(trademarkBack);

        documents.trademark(null);
        assertThat(documents.getTrademark()).isNull();
    }

    @Test
    void userTest() {
        Documents documents = getDocumentsRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        documents.setUser(userProfileBack);
        assertThat(documents.getUser()).isEqualTo(userProfileBack);

        documents.user(null);
        assertThat(documents.getUser()).isNull();
    }
}
