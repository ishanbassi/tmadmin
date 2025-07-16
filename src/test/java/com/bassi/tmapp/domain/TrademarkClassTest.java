package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TrademarkClassTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
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
}
