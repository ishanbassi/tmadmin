package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkDTO.class);
        TrademarkDTO trademarkDTO1 = new TrademarkDTO();
        trademarkDTO1.setId(1L);
        TrademarkDTO trademarkDTO2 = new TrademarkDTO();
        assertThat(trademarkDTO1).isNotEqualTo(trademarkDTO2);
        trademarkDTO2.setId(trademarkDTO1.getId());
        assertThat(trademarkDTO1).isEqualTo(trademarkDTO2);
        trademarkDTO2.setId(2L);
        assertThat(trademarkDTO1).isNotEqualTo(trademarkDTO2);
        trademarkDTO1.setId(null);
        assertThat(trademarkDTO1).isNotEqualTo(trademarkDTO2);
    }
}
