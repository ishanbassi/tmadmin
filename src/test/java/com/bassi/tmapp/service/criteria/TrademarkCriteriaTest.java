package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TrademarkCriteriaTest {

    @Test
    void newTrademarkCriteriaHasAllFiltersNullTest() {
        var trademarkCriteria = new TrademarkCriteria();
        assertThat(trademarkCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void trademarkCriteriaFluentMethodsCreatesFiltersTest() {
        var trademarkCriteria = new TrademarkCriteria();

        setAllFilters(trademarkCriteria);

        assertThat(trademarkCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void trademarkCriteriaCopyCreatesNullFilterTest() {
        var trademarkCriteria = new TrademarkCriteria();
        var copy = trademarkCriteria.copy();

        assertThat(trademarkCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(trademarkCriteria)
        );
    }

    @Test
    void trademarkCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var trademarkCriteria = new TrademarkCriteria();
        setAllFilters(trademarkCriteria);

        var copy = trademarkCriteria.copy();

        assertThat(trademarkCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(trademarkCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var trademarkCriteria = new TrademarkCriteria();

        assertThat(trademarkCriteria).hasToString("TrademarkCriteria{}");
    }

    private static void setAllFilters(TrademarkCriteria trademarkCriteria) {
        trademarkCriteria.id();
        trademarkCriteria.name();
        trademarkCriteria.details();
        trademarkCriteria.applicationNo();
        trademarkCriteria.applicationDate();
        trademarkCriteria.agentName();
        trademarkCriteria.agentAddress();
        trademarkCriteria.proprietorName();
        trademarkCriteria.proprietorAddress();
        trademarkCriteria.headOffice();
        trademarkCriteria.imgUrl();
        trademarkCriteria.tmClass();
        trademarkCriteria.journalNo();
        trademarkCriteria.deleted();
        trademarkCriteria.usage();
        trademarkCriteria.associatedTms();
        trademarkCriteria.trademarkStatus();
        trademarkCriteria.createdDate();
        trademarkCriteria.modifiedDate();
        trademarkCriteria.renewalDate();
        trademarkCriteria.type();
        trademarkCriteria.pageNo();
        trademarkCriteria.source();
        trademarkCriteria.leadId();
        trademarkCriteria.userId();
        trademarkCriteria.distinct();
    }

    private static Condition<TrademarkCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDetails()) &&
                condition.apply(criteria.getApplicationNo()) &&
                condition.apply(criteria.getApplicationDate()) &&
                condition.apply(criteria.getAgentName()) &&
                condition.apply(criteria.getAgentAddress()) &&
                condition.apply(criteria.getProprietorName()) &&
                condition.apply(criteria.getProprietorAddress()) &&
                condition.apply(criteria.getHeadOffice()) &&
                condition.apply(criteria.getImgUrl()) &&
                condition.apply(criteria.getTmClass()) &&
                condition.apply(criteria.getJournalNo()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getUsage()) &&
                condition.apply(criteria.getAssociatedTms()) &&
                condition.apply(criteria.getTrademarkStatus()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getRenewalDate()) &&
                condition.apply(criteria.getType()) &&
                condition.apply(criteria.getPageNo()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getLeadId()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TrademarkCriteria> copyFiltersAre(TrademarkCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDetails(), copy.getDetails()) &&
                condition.apply(criteria.getApplicationNo(), copy.getApplicationNo()) &&
                condition.apply(criteria.getApplicationDate(), copy.getApplicationDate()) &&
                condition.apply(criteria.getAgentName(), copy.getAgentName()) &&
                condition.apply(criteria.getAgentAddress(), copy.getAgentAddress()) &&
                condition.apply(criteria.getProprietorName(), copy.getProprietorName()) &&
                condition.apply(criteria.getProprietorAddress(), copy.getProprietorAddress()) &&
                condition.apply(criteria.getHeadOffice(), copy.getHeadOffice()) &&
                condition.apply(criteria.getImgUrl(), copy.getImgUrl()) &&
                condition.apply(criteria.getTmClass(), copy.getTmClass()) &&
                condition.apply(criteria.getJournalNo(), copy.getJournalNo()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getUsage(), copy.getUsage()) &&
                condition.apply(criteria.getAssociatedTms(), copy.getAssociatedTms()) &&
                condition.apply(criteria.getTrademarkStatus(), copy.getTrademarkStatus()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getRenewalDate(), copy.getRenewalDate()) &&
                condition.apply(criteria.getType(), copy.getType()) &&
                condition.apply(criteria.getPageNo(), copy.getPageNo()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getLeadId(), copy.getLeadId()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
