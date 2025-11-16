package com.bassi.tmapp.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DocumentsCriteriaTest {

    @Test
    void newDocumentsCriteriaHasAllFiltersNullTest() {
        var documentsCriteria = new DocumentsCriteria();
        assertThat(documentsCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void documentsCriteriaFluentMethodsCreatesFiltersTest() {
        var documentsCriteria = new DocumentsCriteria();

        setAllFilters(documentsCriteria);

        assertThat(documentsCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void documentsCriteriaCopyCreatesNullFilterTest() {
        var documentsCriteria = new DocumentsCriteria();
        var copy = documentsCriteria.copy();

        assertThat(documentsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(documentsCriteria)
        );
    }

    @Test
    void documentsCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var documentsCriteria = new DocumentsCriteria();
        setAllFilters(documentsCriteria);

        var copy = documentsCriteria.copy();

        assertThat(documentsCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(documentsCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var documentsCriteria = new DocumentsCriteria();

        assertThat(documentsCriteria).hasToString("DocumentsCriteria{}");
    }

    private static void setAllFilters(DocumentsCriteria documentsCriteria) {
        documentsCriteria.id();
        documentsCriteria.documentType();
        documentsCriteria.fileContentType();
        documentsCriteria.fileName();
        documentsCriteria.fileUrl();
        documentsCriteria.createdDate();
        documentsCriteria.modifiedDate();
        documentsCriteria.deleted();
        documentsCriteria.status();
        documentsCriteria.trademarkId();
        documentsCriteria.userProfileId();
        documentsCriteria.distinct();
    }

    private static Condition<DocumentsCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentType()) &&
                condition.apply(criteria.getFileContentType()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.getFileUrl()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate()) &&
                condition.apply(criteria.getDeleted()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTrademarkId()) &&
                condition.apply(criteria.getUserProfileId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DocumentsCriteria> copyFiltersAre(DocumentsCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentType(), copy.getDocumentType()) &&
                condition.apply(criteria.getFileContentType(), copy.getFileContentType()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.getFileUrl(), copy.getFileUrl()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getModifiedDate(), copy.getModifiedDate()) &&
                condition.apply(criteria.getDeleted(), copy.getDeleted()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTrademarkId(), copy.getTrademarkId()) &&
                condition.apply(criteria.getUserProfileId(), copy.getUserProfileId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
