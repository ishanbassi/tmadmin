package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.PaymentTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
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
    void trademarkTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        payment.setTrademark(trademarkBack);
        assertThat(payment.getTrademark()).isEqualTo(trademarkBack);

        payment.trademark(null);
        assertThat(payment.getTrademark()).isNull();
    }

    @Test
    void userProfileTest() {
        Payment payment = getPaymentRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        payment.setUserProfile(userProfileBack);
        assertThat(payment.getUserProfile()).isEqualTo(userProfileBack);

        payment.userProfile(null);
        assertThat(payment.getUserProfile()).isNull();
    }
}
