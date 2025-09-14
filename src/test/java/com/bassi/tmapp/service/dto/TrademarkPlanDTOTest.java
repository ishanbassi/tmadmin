package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkPlanDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkPlanDTO.class);
        TrademarkPlanDTO trademarkPlanDTO1 = new TrademarkPlanDTO();
        trademarkPlanDTO1.setId(1L);
        TrademarkPlanDTO trademarkPlanDTO2 = new TrademarkPlanDTO();
        assertThat(trademarkPlanDTO1).isNotEqualTo(trademarkPlanDTO2);
        trademarkPlanDTO2.setId(trademarkPlanDTO1.getId());
        assertThat(trademarkPlanDTO1).isEqualTo(trademarkPlanDTO2);
        trademarkPlanDTO2.setId(2L);
        assertThat(trademarkPlanDTO1).isNotEqualTo(trademarkPlanDTO2);
        trademarkPlanDTO1.setId(null);
        assertThat(trademarkPlanDTO1).isNotEqualTo(trademarkPlanDTO2);
    }
}
