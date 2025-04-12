package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.TmAgentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
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
}
