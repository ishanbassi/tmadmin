package com.bassi.tmapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEventsTrackingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEventsTrackingDTO.class);
        UserEventsTrackingDTO userEventsTrackingDTO1 = new UserEventsTrackingDTO();
        userEventsTrackingDTO1.setId(1L);
        UserEventsTrackingDTO userEventsTrackingDTO2 = new UserEventsTrackingDTO();
        assertThat(userEventsTrackingDTO1).isNotEqualTo(userEventsTrackingDTO2);
        userEventsTrackingDTO2.setId(userEventsTrackingDTO1.getId());
        assertThat(userEventsTrackingDTO1).isEqualTo(userEventsTrackingDTO2);
        userEventsTrackingDTO2.setId(2L);
        assertThat(userEventsTrackingDTO1).isNotEqualTo(userEventsTrackingDTO2);
        userEventsTrackingDTO1.setId(null);
        assertThat(userEventsTrackingDTO1).isNotEqualTo(userEventsTrackingDTO2);
    }
}
