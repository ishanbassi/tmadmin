package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PublishedTmCriteriaTest {

    @Test
    void newPublishedTmCriteriaHasAllFiltersNullTest() {
        var publishedTmCriteria = new PublishedTmCriteria();
        assertThat(publishedTmCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void publishedTmCriteriaFluentMethodsCreatesFiltersTest() {
        var publishedTmCriteria = new PublishedTmCriteria();

        setAllFilters(publishedTmCriteria);

        assertThat(publishedTmCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void publishedTmCriteriaCopyCreatesNullFilterTest() {
        var publishedTmCriteria = new PublishedTmCriteria();
        var copy = publishedTmCriteria.copy();

        assertThat(publishedTmCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(publishedTmCriteria)
        );
    }

    @Test
    void publishedTmCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var publishedTmCriteria = new PublishedTmCriteria();
        setAllFilters(publishedTmCriteria);

        var copy = publishedTmCriteria.copy();

        assertThat(publishedTmCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(publishedTmCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var publishedTmCriteria = new PublishedTmCriteria();

        assertThat(publishedTmCriteria).hasToString("PublishedTmCriteria{}");
    }

    private static void setAllFilters(PublishedTmCriteria publishedTmCriteria) {
        publishedTmCriteria.id();
        publishedTmCriteria.name();
        publishedTmCriteria.details();
        publishedTmCriteria.applicationNo();
        publishedTmCriteria.applicationDate();
        publishedTmCriteria.agentName();
        publishedTmCriteria.agentAddress();
        publishedTmCriteria.proprietorName();
        publishedTmCriteria.proprietorAddress();
        publishedTmCriteria.headOffice();
        publishedTmCriteria.imgUrl();
        publishedTmCriteria.tmClass();
        publishedTmCriteria.journalNo();
        publishedTmCriteria.deleted();
        publishedTmCriteria.usage();
        publishedTmCriteria.associatedTms();
        publishedTmCriteria.trademarkStatus();
        publishedTmCriteria.createdDate();
        publishedTmCriteria.modifiedDate();
        publishedTmCriteria.renewalDate();
        publishedTmCriteria.type();
        publishedTmCriteria.tmAgentId();
        publishedTmCriteria.distinct();
    }

    private static Condition<PublishedTmCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getTmAgentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PublishedTmCriteria> copyFiltersAre(PublishedTmCriteria copy, BiFunction<Object, Object, Boolean> condition) {
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
                condition.apply(criteria.getTmAgentId(), copy.getTmAgentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
