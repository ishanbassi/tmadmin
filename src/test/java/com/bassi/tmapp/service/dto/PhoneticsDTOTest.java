package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhoneticsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhoneticsDTO.class);
        PhoneticsDTO phoneticsDTO1 = new PhoneticsDTO();
        phoneticsDTO1.setId(1L);
        PhoneticsDTO phoneticsDTO2 = new PhoneticsDTO();
        assertThat(phoneticsDTO1).isNotEqualTo(phoneticsDTO2);
        phoneticsDTO2.setId(phoneticsDTO1.getId());
        assertThat(phoneticsDTO1).isEqualTo(phoneticsDTO2);
        phoneticsDTO2.setId(2L);
        assertThat(phoneticsDTO1).isNotEqualTo(phoneticsDTO2);
        phoneticsDTO1.setId(null);
        assertThat(phoneticsDTO1).isNotEqualTo(phoneticsDTO2);
    }
}
