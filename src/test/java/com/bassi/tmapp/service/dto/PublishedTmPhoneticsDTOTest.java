package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PublishedTmPhoneticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublishedTmPhoneticsDTO.class);
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO1 = new PublishedTmPhoneticsDTO();
        publishedTmPhoneticsDTO1.setId(1L);
        PublishedTmPhoneticsDTO publishedTmPhoneticsDTO2 = new PublishedTmPhoneticsDTO();
        assertThat(publishedTmPhoneticsDTO1).isNotEqualTo(publishedTmPhoneticsDTO2);
        publishedTmPhoneticsDTO2.setId(publishedTmPhoneticsDTO1.getId());
        assertThat(publishedTmPhoneticsDTO1).isEqualTo(publishedTmPhoneticsDTO2);
        publishedTmPhoneticsDTO2.setId(2L);
        assertThat(publishedTmPhoneticsDTO1).isNotEqualTo(publishedTmPhoneticsDTO2);
        publishedTmPhoneticsDTO1.setId(null);
        assertThat(publishedTmPhoneticsDTO1).isNotEqualTo(publishedTmPhoneticsDTO2);
    }
}
