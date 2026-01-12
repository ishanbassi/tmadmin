package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.UserEventsTrackingTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserEventsTrackingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserEventsTracking.class);
        UserEventsTracking userEventsTracking1 = getUserEventsTrackingSample1();
        UserEventsTracking userEventsTracking2 = new UserEventsTracking();
        assertThat(userEventsTracking1).isNotEqualTo(userEventsTracking2);

        userEventsTracking2.setId(userEventsTracking1.getId());
        assertThat(userEventsTracking1).isEqualTo(userEventsTracking2);

        userEventsTracking2 = getUserEventsTrackingSample2();
        assertThat(userEventsTracking1).isNotEqualTo(userEventsTracking2);
    }

    @Test
    void userProfileTest() {
        UserEventsTracking userEventsTracking = getUserEventsTrackingRandomSampleGenerator();
        UserProfile userProfileBack = getUserProfileRandomSampleGenerator();

        userEventsTracking.setUserProfile(userProfileBack);
        assertThat(userEventsTracking.getUserProfile()).isEqualTo(userProfileBack);

        userEventsTracking.userProfile(null);
        assertThat(userEventsTracking.getUserProfile()).isNull();
    }
}
