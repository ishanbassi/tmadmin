package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TrademarkPlanTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkPlan.class);
        TrademarkPlan trademarkPlan1 = getTrademarkPlanSample1();
        TrademarkPlan trademarkPlan2 = new TrademarkPlan();
        assertThat(trademarkPlan1).isNotEqualTo(trademarkPlan2);

        trademarkPlan2.setId(trademarkPlan1.getId());
        assertThat(trademarkPlan1).isEqualTo(trademarkPlan2);

        trademarkPlan2 = getTrademarkPlanSample2();
        assertThat(trademarkPlan1).isNotEqualTo(trademarkPlan2);
    }
}
