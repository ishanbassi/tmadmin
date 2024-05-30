package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PublishedTmDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PublishedTmDTO.class);
        PublishedTmDTO publishedTmDTO1 = new PublishedTmDTO();
        publishedTmDTO1.setId(1L);
        PublishedTmDTO publishedTmDTO2 = new PublishedTmDTO();
        assertThat(publishedTmDTO1).isNotEqualTo(publishedTmDTO2);
        publishedTmDTO2.setId(publishedTmDTO1.getId());
        assertThat(publishedTmDTO1).isEqualTo(publishedTmDTO2);
        publishedTmDTO2.setId(2L);
        assertThat(publishedTmDTO1).isNotEqualTo(publishedTmDTO2);
        publishedTmDTO1.setId(null);
        assertThat(publishedTmDTO1).isNotEqualTo(publishedTmDTO2);
    }
}
