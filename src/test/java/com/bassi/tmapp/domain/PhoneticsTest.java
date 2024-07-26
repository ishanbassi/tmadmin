package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.PhoneticsTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phonetics.class);
        Phonetics phonetics1 = getPhoneticsSample1();
        Phonetics phonetics2 = new Phonetics();
        assertThat(phonetics1).isNotEqualTo(phonetics2);

        phonetics2.setId(phonetics1.getId());
        assertThat(phonetics1).isEqualTo(phonetics2);

        phonetics2 = getPhoneticsSample2();
        assertThat(phonetics1).isNotEqualTo(phonetics2);
    }

    @Test
    void trademarkTest() {
        Phonetics phonetics = getPhoneticsRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        phonetics.setTrademark(trademarkBack);
        assertThat(phonetics.getTrademark()).isEqualTo(trademarkBack);

        phonetics.trademark(null);
        assertThat(phonetics.getTrademark()).isNull();
    }
}
