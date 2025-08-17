package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TrademarkClassTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TrademarkClassTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkClass.class);
        TrademarkClass trademarkClass1 = getTrademarkClassSample1();
        TrademarkClass trademarkClass2 = new TrademarkClass();
        assertThat(trademarkClass1).isNotEqualTo(trademarkClass2);

        trademarkClass2.setId(trademarkClass1.getId());
        assertThat(trademarkClass1).isEqualTo(trademarkClass2);

        trademarkClass2 = getTrademarkClassSample2();
        assertThat(trademarkClass1).isNotEqualTo(trademarkClass2);
    }

    @Test
    void trademarksTest() {
        TrademarkClass trademarkClass = getTrademarkClassRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        trademarkClass.addTrademarks(trademarkBack);
        assertThat(trademarkClass.getTrademarks()).containsOnly(trademarkBack);
        assertThat(trademarkBack.getTrademarkClasses()).containsOnly(trademarkClass);

        trademarkClass.removeTrademarks(trademarkBack);
        assertThat(trademarkClass.getTrademarks()).doesNotContain(trademarkBack);
        assertThat(trademarkBack.getTrademarkClasses()).doesNotContain(trademarkClass);

        trademarkClass.trademarks(new HashSet<>(Set.of(trademarkBack)));
        assertThat(trademarkClass.getTrademarks()).containsOnly(trademarkBack);
        assertThat(trademarkBack.getTrademarkClasses()).containsOnly(trademarkClass);

        trademarkClass.setTrademarks(new HashSet<>());
        assertThat(trademarkClass.getTrademarks()).doesNotContain(trademarkBack);
        assertThat(trademarkBack.getTrademarkClasses()).doesNotContain(trademarkClass);
    }
}
