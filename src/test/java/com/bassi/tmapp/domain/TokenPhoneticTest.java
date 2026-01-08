package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TokenPhoneticTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTokenTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TokenPhoneticTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenPhonetic.class);
        TokenPhonetic tokenPhonetic1 = getTokenPhoneticSample1();
        TokenPhonetic tokenPhonetic2 = new TokenPhonetic();
        assertThat(tokenPhonetic1).isNotEqualTo(tokenPhonetic2);

        tokenPhonetic2.setId(tokenPhonetic1.getId());
        assertThat(tokenPhonetic1).isEqualTo(tokenPhonetic2);

        tokenPhonetic2 = getTokenPhoneticSample2();
        assertThat(tokenPhonetic1).isNotEqualTo(tokenPhonetic2);
    }

    @Test
    void trademarkTokenTest() {
        TokenPhonetic tokenPhonetic = getTokenPhoneticRandomSampleGenerator();
        TrademarkToken trademarkTokenBack = getTrademarkTokenRandomSampleGenerator();

        tokenPhonetic.setTrademarkToken(trademarkTokenBack);
        assertThat(tokenPhonetic.getTrademarkToken()).isEqualTo(trademarkTokenBack);

        tokenPhonetic.trademarkToken(null);
        assertThat(tokenPhonetic.getTrademarkToken()).isNull();
    }
}
