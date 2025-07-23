package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.LeadTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
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
}
