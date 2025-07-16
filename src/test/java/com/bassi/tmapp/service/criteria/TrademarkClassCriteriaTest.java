package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrademarkClassCriteriaTest {

    @Test
    void newTrademarkClassCriteriaHasAllFiltersNullTest() {
        var trademarkClassCriteria = new TrademarkClassCriteria();
        assertThat(trademarkClassCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trademarkClassCriteriaFluentMethodsCreatesFiltersTest() {
        var trademarkClassCriteria = new TrademarkClassCriteria();

        setAllFilters(trademarkClassCriteria);

        assertThat(trademarkClassCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trademarkClassCriteriaCopyCreatesNullFilterTest() {
        var trademarkClassCriteria = new TrademarkClassCriteria();
        var copy = trademarkClassCriteria.copy();

        assertThat(trademarkClassCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trademarkClassCriteria)
        );
    }

    @Test
    void trademarkClassCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trademarkClassCriteria = new TrademarkClassCriteria();
        setAllFilters(trademarkClassCriteria);

        var copy = trademarkClassCriteria.copy();

        assertThat(trademarkClassCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trademarkClassCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trademarkClassCriteria = new TrademarkClassCriteria();

        assertThat(trademarkClassCriteria).hasToString("TrademarkClassCriteria{}");
    }

    private static void setAllFilters(TrademarkClassCriteria trademarkClassCriteria) {
        trademarkClassCriteria.id();
        trademarkClassCriteria.code();
        trademarkClassCriteria.tmClass();
        trademarkClassCriteria.keyword();
        trademarkClassCriteria.title();
        trademarkClassCriteria.description();
        trademarkClassCriteria.createdDate();
        trademarkClassCriteria.modifiedDate();
        trademarkClassCriteria.deleted();
        trademarkClassCriteria.distinct();
    }

    private static Condition<TrademarkClassCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getTmClass()) &&
                condition.apply(criteria.getKeyword()) &&
                condition.apply(criteria.getTitle()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrademarkClassCriteria> copyFiltersAre(
        TrademarkClassCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getTmClass(), copy.getTmClass()) &&
                condition.apply(criteria.getKeyword(), copy.getKeyword()) &&
                condition.apply(criteria.getTitle(), copy.getTitle()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
