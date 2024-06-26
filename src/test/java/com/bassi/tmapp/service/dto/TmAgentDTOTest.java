package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TmAgentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TmAgentDTO.class);
        TmAgentDTO tmAgentDTO1 = new TmAgentDTO();
        tmAgentDTO1.setId(1L);
        TmAgentDTO tmAgentDTO2 = new TmAgentDTO();
        assertThat(tmAgentDTO1).isNotEqualTo(tmAgentDTO2);
        tmAgentDTO2.setId(tmAgentDTO1.getId());
        assertThat(tmAgentDTO1).isEqualTo(tmAgentDTO2);
        tmAgentDTO2.setId(2L);
        assertThat(tmAgentDTO1).isNotEqualTo(tmAgentDTO2);
        tmAgentDTO1.setId(null);
        assertThat(tmAgentDTO1).isNotEqualTo(tmAgentDTO2);
    }
}
