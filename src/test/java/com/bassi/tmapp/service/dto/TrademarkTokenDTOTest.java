package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkTokenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkTokenDTO.class);
        TrademarkTokenDTO trademarkTokenDTO1 = new TrademarkTokenDTO();
        trademarkTokenDTO1.setId(1L);
        TrademarkTokenDTO trademarkTokenDTO2 = new TrademarkTokenDTO();
        assertThat(trademarkTokenDTO1).isNotEqualTo(trademarkTokenDTO2);
        trademarkTokenDTO2.setId(trademarkTokenDTO1.getId());
        assertThat(trademarkTokenDTO1).isEqualTo(trademarkTokenDTO2);
        trademarkTokenDTO2.setId(2L);
        assertThat(trademarkTokenDTO1).isNotEqualTo(trademarkTokenDTO2);
        trademarkTokenDTO1.setId(null);
        assertThat(trademarkTokenDTO1).isNotEqualTo(trademarkTokenDTO2);
    }
}
