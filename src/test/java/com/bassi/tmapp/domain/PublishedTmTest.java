package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.PublishedTmTestSamples.*;
import static com.bassi.tmapp.domain.TmAgentTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PublishedTmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublishedTm.class);
        PublishedTm publishedTm1 = getPublishedTmSample1();
        PublishedTm publishedTm2 = new PublishedTm();
        assertThat(publishedTm1).isNotEqualTo(publishedTm2);

        publishedTm2.setId(publishedTm1.getId());
        assertThat(publishedTm1).isEqualTo(publishedTm2);

        publishedTm2 = getPublishedTmSample2();
        assertThat(publishedTm1).isNotEqualTo(publishedTm2);
    }

    @Test
    void tmAgentTest() {
        PublishedTm publishedTm = getPublishedTmRandomSampleGenerator();
        TmAgent tmAgentBack = getTmAgentRandomSampleGenerator();

        publishedTm.setTmAgent(tmAgentBack);
        assertThat(publishedTm.getTmAgent()).isEqualTo(tmAgentBack);

        publishedTm.tmAgent(null);
        assertThat(publishedTm.getTmAgent()).isNull();
    }
}
