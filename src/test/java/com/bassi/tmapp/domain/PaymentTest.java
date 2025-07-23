package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.LeadTestSamples.*;
import static com.bassi.tmapp.domain.PaymentTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = getPaymentSample1();
        Payment payment2 = new Payment();
        assertThat(payment1).isNotEqualTo(payment2);

        payment2.setId(payment1.getId());
        assertThat(payment1).isEqualTo(payment2);

        payment2 = getPaymentSample2();
        assertThat(payment1).isNotEqualTo(payment2);
    }

    @Test
    void leadTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Lead leadBack = getLeadRandomSampleGenerator();

        payment.setLead(leadBack);
        assertThat(payment.getLead()).isEqualTo(leadBack);

        payment.lead(null);
        assertThat(payment.getLead()).isNull();
    }

    @Test
    void userTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        payment.setUser(userProfileBack);
        assertThat(payment.getUser()).isEqualTo(userProfileBack);

        payment.user(null);
        assertThat(payment.getUser()).isNull();
    }
}
