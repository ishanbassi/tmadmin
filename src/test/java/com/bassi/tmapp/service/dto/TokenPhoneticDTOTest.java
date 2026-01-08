package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TokenPhoneticDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TokenPhoneticDTO.class);
        TokenPhoneticDTO tokenPhoneticDTO1 = new TokenPhoneticDTO();
        tokenPhoneticDTO1.setId(1L);
        TokenPhoneticDTO tokenPhoneticDTO2 = new TokenPhoneticDTO();
        assertThat(tokenPhoneticDTO1).isNotEqualTo(tokenPhoneticDTO2);
        tokenPhoneticDTO2.setId(tokenPhoneticDTO1.getId());
        assertThat(tokenPhoneticDTO1).isEqualTo(tokenPhoneticDTO2);
        tokenPhoneticDTO2.setId(2L);
        assertThat(tokenPhoneticDTO1).isNotEqualTo(tokenPhoneticDTO2);
        tokenPhoneticDTO1.setId(null);
        assertThat(tokenPhoneticDTO1).isNotEqualTo(tokenPhoneticDTO2);
    }
}
