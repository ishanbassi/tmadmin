package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTokenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkTokenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkToken.class);
        TrademarkToken trademarkToken1 = getTrademarkTokenSample1();
        TrademarkToken trademarkToken2 = new TrademarkToken();
        assertThat(trademarkToken1).isNotEqualTo(trademarkToken2);

        trademarkToken2.setId(trademarkToken1.getId());
        assertThat(trademarkToken1).isEqualTo(trademarkToken2);

        trademarkToken2 = getTrademarkTokenSample2();
        assertThat(trademarkToken1).isNotEqualTo(trademarkToken2);
    }

    @Test
    void trademarkTest() {
        TrademarkToken trademarkToken = getTrademarkTokenRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        trademarkToken.setTrademark(trademarkBack);
        assertThat(trademarkToken.getTrademark()).isEqualTo(trademarkBack);

        trademarkToken.trademark(null);
        assertThat(trademarkToken.getTrademark()).isNull();
    }
}
