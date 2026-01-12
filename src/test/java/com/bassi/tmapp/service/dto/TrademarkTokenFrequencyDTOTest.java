package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrademarkTokenFrequencyDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrademarkTokenFrequencyDTO.class);
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO1 = new TrademarkTokenFrequencyDTO();
        trademarkTokenFrequencyDTO1.setId(1L);
        TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO2 = new TrademarkTokenFrequencyDTO();
        assertThat(trademarkTokenFrequencyDTO1).isNotEqualTo(trademarkTokenFrequencyDTO2);
        trademarkTokenFrequencyDTO2.setId(trademarkTokenFrequencyDTO1.getId());
        assertThat(trademarkTokenFrequencyDTO1).isEqualTo(trademarkTokenFrequencyDTO2);
        trademarkTokenFrequencyDTO2.setId(2L);
        assertThat(trademarkTokenFrequencyDTO1).isNotEqualTo(trademarkTokenFrequencyDTO2);
        trademarkTokenFrequencyDTO1.setId(null);
        assertThat(trademarkTokenFrequencyDTO1).isNotEqualTo(trademarkTokenFrequencyDTO2);
    }
}
