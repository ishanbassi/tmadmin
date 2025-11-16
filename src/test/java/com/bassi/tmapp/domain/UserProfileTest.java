package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.DocumentsTestSamples.*;
import static com.bassi.tmapp.domain.UserProfileTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserProfileTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserProfile.class);
        UserProfile userProfile1 = getUserProfileSample1();
        UserProfile userProfile2 = new UserProfile();
        assertThat(userProfile1).isNotEqualTo(userProfile2);

        userProfile2.setId(userProfile1.getId());
        assertThat(userProfile1).isEqualTo(userProfile2);

        userProfile2 = getUserProfileSample2();
        assertThat(userProfile1).isNotEqualTo(userProfile2);
    }

    @Test
    void documentsTest() {
        UserProfile userProfile = getUserProfileRandomSampleGenerator();
        Documents documentsBack = getDocumentsRandomSampleGenerator();

        userProfile.addDocuments(documentsBack);
        assertThat(userProfile.getDocuments()).containsOnly(documentsBack);
        assertThat(documentsBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.removeDocuments(documentsBack);
        assertThat(userProfile.getDocuments()).doesNotContain(documentsBack);
        assertThat(documentsBack.getUserProfile()).isNull();

        userProfile.documents(new HashSet<>(Set.of(documentsBack)));
        assertThat(userProfile.getDocuments()).containsOnly(documentsBack);
        assertThat(documentsBack.getUserProfile()).isEqualTo(userProfile);

        userProfile.setDocuments(new HashSet<>());
        assertThat(userProfile.getDocuments()).doesNotContain(documentsBack);
        assertThat(documentsBack.getUserProfile()).isNull();
    }
}
