package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LeadCriteriaTest {

    @Test
    void newLeadCriteriaHasAllFiltersNullTest() {
        var leadCriteria = new LeadCriteria();
        assertThat(leadCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void leadCriteriaFluentMethodsCreatesFiltersTest() {
        var leadCriteria = new LeadCriteria();

        setAllFilters(leadCriteria);

        assertThat(leadCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void leadCriteriaCopyCreatesNullFilterTest() {
        var leadCriteria = new LeadCriteria();
        var copy = leadCriteria.copy();

        assertThat(leadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(leadCriteria)
        );
    }

    @Test
    void leadCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var leadCriteria = new LeadCriteria();
        setAllFilters(leadCriteria);

        var copy = leadCriteria.copy();

        assertThat(leadCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(leadCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var leadCriteria = new LeadCriteria();

        assertThat(leadCriteria).hasToString("LeadCriteria{}");
    }

    private static void setAllFilters(LeadCriteria leadCriteria) {
        leadCriteria.id();
        leadCriteria.fullName();
        leadCriteria.phoneNumber();
        leadCriteria.email();
        leadCriteria.city();
        leadCriteria.brandName();
        leadCriteria.selectedPackage();
        leadCriteria.tmClass();
        leadCriteria.comments();
        leadCriteria.contactMethod();
        leadCriteria.createdDate();
        leadCriteria.modifiedDate();
        leadCriteria.deleted();
        leadCriteria.status();
        leadCriteria.leadSource();
        leadCriteria.assignedToId();
        leadCriteria.distinct();
    }

    private static Condition<LeadCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getBrandName()) &&
                condition.apply(criteria.getSelectedPackage()) &&
                condition.apply(criteria.getTmClass()) &&
                condition.apply(criteria.getComments()) &&
                condition.apply(criteria.getContactMethod()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getLeadSource()) &&
                condition.apply(criteria.getAssignedToId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LeadCriteria> copyFiltersAre(LeadCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getBrandName(), copy.getBrandName()) &&
                condition.apply(criteria.getSelectedPackage(), copy.getSelectedPackage()) &&
                condition.apply(criteria.getTmClass(), copy.getTmClass()) &&
                condition.apply(criteria.getComments(), copy.getComments()) &&
                condition.apply(criteria.getContactMethod(), copy.getContactMethod()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getLeadSource(), copy.getLeadSource()) &&
                condition.apply(criteria.getAssignedToId(), copy.getAssignedToId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
