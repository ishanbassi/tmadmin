package com.bassi.tmapp.domain;

import static com.bassi.tmapp.domain.EmployeeTestSamples.*;
import static com.bassi.tmapp.domain.LeadTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.bassi.tmapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LeadTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Lead.class);
        Lead lead1 = getLeadSample1();
        Lead lead2 = new Lead();
        assertThat(lead1).isNotEqualTo(lead2);

        lead2.setId(lead1.getId());
        assertThat(lead1).isEqualTo(lead2);

        lead2 = getLeadSample2();
        assertThat(lead1).isNotEqualTo(lead2);
    }

    @Test
    void assignedToTest() {
        Lead lead = getLeadRandomSampleGenerator();
        Employee employeeBack = getEmployeeRandomSampleGenerator();

        lead.setAssignedTo(employeeBack);
        assertThat(lead.getAssignedTo()).isEqualTo(employeeBack);

        lead.assignedTo(null);
        assertThat(lead.getAssignedTo()).isNull();
    }
}
