package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TmAgentCriteriaTest {

    @Test
    void newTmAgentCriteriaHasAllFiltersNullTest() {
        var tmAgentCriteria = new TmAgentCriteria();
        assertThat(tmAgentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void tmAgentCriteriaFluentMethodsCreatesFiltersTest() {
        var tmAgentCriteria = new TmAgentCriteria();

        setAllFilters(tmAgentCriteria);

        assertThat(tmAgentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void tmAgentCriteriaCopyCreatesNullFilterTest() {
        var tmAgentCriteria = new TmAgentCriteria();
        var copy = tmAgentCriteria.copy();

        assertThat(tmAgentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(tmAgentCriteria)
        );
    }

    @Test
    void tmAgentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tmAgentCriteria = new TmAgentCriteria();
        setAllFilters(tmAgentCriteria);

        var copy = tmAgentCriteria.copy();

        assertThat(tmAgentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(tmAgentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tmAgentCriteria = new TmAgentCriteria();

        assertThat(tmAgentCriteria).hasToString("TmAgentCriteria{}");
    }

    private static void setAllFilters(TmAgentCriteria tmAgentCriteria) {
        tmAgentCriteria.id();
        tmAgentCriteria.fullName();
        tmAgentCriteria.address();
        tmAgentCriteria.createdDate();
        tmAgentCriteria.modifiedDate();
        tmAgentCriteria.deleted();
        tmAgentCriteria.companyName();
        tmAgentCriteria.agentCode();
        tmAgentCriteria.email();
        tmAgentCriteria.distinct();
    }

    private static Condition<TmAgentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFullName()) &&
                condition.apply(criteria.getAddress()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getCompanyName()) &&
                condition.apply(criteria.getAgentCode()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TmAgentCriteria> copyFiltersAre(TmAgentCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFullName(), copy.getFullName()) &&
                condition.apply(criteria.getAddress(), copy.getAddress()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getCompanyName(), copy.getCompanyName()) &&
                condition.apply(criteria.getAgentCode(), copy.getAgentCode()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
