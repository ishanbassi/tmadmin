package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.PublishedTmPhoneticsTestSamples.*;
import static com.bassi.tmapp.domain.PublishedTmTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PublishedTmPhoneticsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublishedTmPhonetics.class);
        PublishedTmPhonetics publishedTmPhonetics1 = getPublishedTmPhoneticsSample1();
        PublishedTmPhonetics publishedTmPhonetics2 = new PublishedTmPhonetics();
        assertThat(publishedTmPhonetics1).isNotEqualTo(publishedTmPhonetics2);

        publishedTmPhonetics2.setId(publishedTmPhonetics1.getId());
        assertThat(publishedTmPhonetics1).isEqualTo(publishedTmPhonetics2);

        publishedTmPhonetics2 = getPublishedTmPhoneticsSample2();
        assertThat(publishedTmPhonetics1).isNotEqualTo(publishedTmPhonetics2);
    }

    @Test
    void publishedTmTest() {
        PublishedTmPhonetics publishedTmPhonetics = getPublishedTmPhoneticsRandomSampleGenerator();
        PublishedTm publishedTmBack = getPublishedTmRandomSampleGenerator();

        publishedTmPhonetics.setPublishedTm(publishedTmBack);
        assertThat(publishedTmPhonetics.getPublishedTm()).isEqualTo(publishedTmBack);

        publishedTmPhonetics.publishedTm(null);
        assertThat(publishedTmPhonetics.getPublishedTm()).isNull();
    }
}
