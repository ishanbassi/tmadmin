package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TmAgentTestSamples.*;
import static com.bassi.tmapp.domain.TrademarkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TmAgentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TmAgent.class);
        TmAgent tmAgent1 = getTmAgentSample1();
        TmAgent tmAgent2 = new TmAgent();
        assertThat(tmAgent1).isNotEqualTo(tmAgent2);

        tmAgent2.setId(tmAgent1.getId());
        assertThat(tmAgent1).isEqualTo(tmAgent2);

        tmAgent2 = getTmAgentSample2();
        assertThat(tmAgent1).isNotEqualTo(tmAgent2);
    }

    @Test
    void trademarksTest() throws Exception {
        TmAgent tmAgent = getTmAgentRandomSampleGenerator();
        Trademark trademarkBack = getTrademarkRandomSampleGenerator();

        tmAgent.addTrademarks(trademarkBack);
        assertThat(tmAgent.getTrademarks()).containsOnly(trademarkBack);
        assertThat(trademarkBack.getTmAgent()).isEqualTo(tmAgent);

        tmAgent.removeTrademarks(trademarkBack);
        assertThat(tmAgent.getTrademarks()).doesNotContain(trademarkBack);
        assertThat(trademarkBack.getTmAgent()).isNull();

        tmAgent.trademarks(new HashSet<>(Set.of(trademarkBack)));
        assertThat(tmAgent.getTrademarks()).containsOnly(trademarkBack);
        assertThat(trademarkBack.getTmAgent()).isEqualTo(tmAgent);

        tmAgent.setTrademarks(new HashSet<>());
        assertThat(tmAgent.getTrademarks()).doesNotContain(trademarkBack);
        assertThat(trademarkBack.getTmAgent()).isNull();
    }
}
