package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.DocumentsTestSamples.*;
import static com.bassi.tmapp.domain.LeadTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkClassTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkPlanTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrademarkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Trademark.class);
        Trademark trademark1 = getTrademarkSample1();
        Trademark trademark2 = new Trademark();
        assertThat(trademark1).isNotEqualTo(trademark2);

        trademark2.setId(trademark1.getId());
        assertThat(trademark1).isEqualTo(trademark2);

        trademark2 = getTrademarkSample2();
        assertThat(trademark1).isNotEqualTo(trademark2);
    }

    @Test
    void leadTest() {
        Trademark trademark = getTrademarkRandomSampleGenerator();
        Lead leadBack = getLeadRandomSampleGenerator();

        trademark.setLead(leadBack);
        assertThat(trademark.getLead()).isEqualTo(leadBack);

        trademark.lead(null);
        assertThat(trademark.getLead()).isNull();
    }

    @Test
    void userTest() {
        Trademark trademark = getTrademarkRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        trademark.setUser(userProfileBack);
        assertThat(trademark.getUser()).isEqualTo(userProfileBack);

        trademark.user(null);
        assertThat(trademark.getUser()).isNull();
    }

    @Test
    void trademarkPlanTest() {
        Trademark trademark = getTrademarkRandomSampleGenerator();
        TrademarkPlan trademarkPlanBack = getTrademarkPlanRandomSampleGenerator();

        trademark.setTrademarkPlan(trademarkPlanBack);
        assertThat(trademark.getTrademarkPlan()).isEqualTo(trademarkPlanBack);

        trademark.trademarkPlan(null);
        assertThat(trademark.getTrademarkPlan()).isNull();
    }

    @Test
    void trademarkClassesTest() {
        Trademark trademark = getTrademarkRandomSampleGenerator();
        TrademarkClass trademarkClassBack = getTrademarkClassRandomSampleGenerator();

        trademark.addTrademarkClasses(trademarkClassBack);
        assertThat(trademark.getTrademarkClasses()).containsOnly(trademarkClassBack);

        trademark.removeTrademarkClasses(trademarkClassBack);
        assertThat(trademark.getTrademarkClasses()).doesNotContain(trademarkClassBack);

        trademark.trademarkClasses(new HashSet<>(Set.of(trademarkClassBack)));
        assertThat(trademark.getTrademarkClasses()).containsOnly(trademarkClassBack);

        trademark.setTrademarkClasses(new HashSet<>());
        assertThat(trademark.getTrademarkClasses()).doesNotContain(trademarkClassBack);
    }

    @Test
    void documentsTest() {
        Trademark trademark = getTrademarkRandomSampleGenerator();
        Documents documentsBack = getDocumentsRandomSampleGenerator();

        trademark.addDocuments(documentsBack);
        assertThat(trademark.getDocuments()).containsOnly(documentsBack);
        assertThat(documentsBack.getTrademark()).isEqualTo(trademark);

        trademark.removeDocuments(documentsBack);
        assertThat(trademark.getDocuments()).doesNotContain(documentsBack);
        assertThat(documentsBack.getTrademark()).isNull();

        trademark.documents(new HashSet<>(Set.of(documentsBack)));
        assertThat(trademark.getDocuments()).containsOnly(documentsBack);
        assertThat(documentsBack.getTrademark()).isEqualTo(trademark);

        trademark.setDocuments(new HashSet<>());
        assertThat(trademark.getDocuments()).doesNotContain(documentsBack);
        assertThat(documentsBack.getTrademark()).isNull();
    }
}
