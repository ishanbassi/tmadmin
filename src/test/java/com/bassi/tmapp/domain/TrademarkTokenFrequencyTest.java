package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TrademarkTokenFrequencyTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkTokenFrequencyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkTokenFrequency.class);
        TrademarkTokenFrequency trademarkTokenFrequency1 = getTrademarkTokenFrequencySample1();
        TrademarkTokenFrequency trademarkTokenFrequency2 = new TrademarkTokenFrequency();
        assertThat(trademarkTokenFrequency1).isNotEqualTo(trademarkTokenFrequency2);

        trademarkTokenFrequency2.setId(trademarkTokenFrequency1.getId());
        assertThat(trademarkTokenFrequency1).isEqualTo(trademarkTokenFrequency2);

        trademarkTokenFrequency2 = getTrademarkTokenFrequencySample2();
        assertThat(trademarkTokenFrequency1).isNotEqualTo(trademarkTokenFrequency2);
    }
}
