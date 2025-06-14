package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CompanyCriteriaTest {

    @Test
    void newCompanyCriteriaHasAllFiltersNullTest() {
        var companyCriteria = new CompanyCriteria();
        assertThat(companyCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void companyCriteriaFluentMethodsCreatesFiltersTest() {
        var companyCriteria = new CompanyCriteria();

        setAllFilters(companyCriteria);

        assertThat(companyCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void companyCriteriaCopyCreatesNullFilterTest() {
        var companyCriteria = new CompanyCriteria();
        var copy = companyCriteria.copy();

        assertThat(companyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(companyCriteria)
        );
    }

    @Test
    void companyCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var companyCriteria = new CompanyCriteria();
        setAllFilters(companyCriteria);

        var copy = companyCriteria.copy();

        assertThat(companyCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(companyCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var companyCriteria = new CompanyCriteria();

        assertThat(companyCriteria).hasToString("CompanyCriteria{}");
    }

    private static void setAllFilters(CompanyCriteria companyCriteria) {
        companyCriteria.id();
        companyCriteria.type();
        companyCriteria.name();
        companyCriteria.cin();
        companyCriteria.gstin();
        companyCriteria.natureOfBusiness();
        companyCriteria.address();
        companyCriteria.state();
        companyCriteria.pincode();
        companyCriteria.city();
        companyCriteria.createdDate();
        companyCriteria.modifiedDate();
        companyCriteria.deleted();
        companyCriteria.userId();
        companyCriteria.distinct();
    }

    private static Condition<CompanyCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCin()) &&
                condition.apply(criteria.getGstin()) &&
                condition.apply(criteria.getNatureOfBusiness()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getState()) &&
                condition.apply(criteria.getPincode()) &&
                condition.apply(criteria.getCity()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CompanyCriteria> copyFiltersAre(CompanyCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCin(), copy.getCin()) &&
                condition.apply(criteria.getGstin(), copy.getGstin()) &&
                condition.apply(criteria.getNatureOfBusiness(), copy.getNatureOfBusiness()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getState(), copy.getState()) &&
                condition.apply(criteria.getPincode(), copy.getPincode()) &&
                condition.apply(criteria.getCity(), copy.getCity()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
