package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkClassDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkClassDTO.class);
        TrademarkClassDTO trademarkClassDTO1 = new TrademarkClassDTO();
        trademarkClassDTO1.setId(1L);
        TrademarkClassDTO trademarkClassDTO2 = new TrademarkClassDTO();
        assertThat(trademarkClassDTO1).isNotEqualTo(trademarkClassDTO2);
        trademarkClassDTO2.setId(trademarkClassDTO1.getId());
        assertThat(trademarkClassDTO1).isEqualTo(trademarkClassDTO2);
        trademarkClassDTO2.setId(2L);
        assertThat(trademarkClassDTO1).isNotEqualTo(trademarkClassDTO2);
        trademarkClassDTO1.setId(null);
        assertThat(trademarkClassDTO1).isNotEqualTo(trademarkClassDTO2);
    }
}
