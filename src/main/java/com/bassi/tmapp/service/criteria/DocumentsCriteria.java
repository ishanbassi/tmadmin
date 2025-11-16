package com.bassi.tmapp.service.criteria;

import com.bassi.tmapp.domain.enumeration.DocumentStatus;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bassi.tmapp.domain.Documents} entity. This class is used
 * in {@link com.bassi.tmapp.web.rest.DocumentsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentsCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentType
     */
    public static class DocumentTypeFilter extends Filter<DocumentType> {

        public DocumentTypeFilter() {}

        public DocumentTypeFilter(DocumentTypeFilter filter) {
            super(filter);
        }

        @Override
        public DocumentTypeFilter copy() {
            return new DocumentTypeFilter(this);
        }
    }

    /**
     * Class for filtering DocumentStatus
     */
    public static class DocumentStatusFilter extends Filter<DocumentStatus> {

        public DocumentStatusFilter() {}

        public DocumentStatusFilter(DocumentStatusFilter filter) {
            super(filter);
        }

        @Override
        public DocumentStatusFilter copy() {
            return new DocumentStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DocumentTypeFilter documentType;

    private StringFilter fileContentType;

    private StringFilter fileName;

    private StringFilter fileUrl;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter modifiedDate;

    private BooleanFilter deleted;

    private DocumentStatusFilter status;

    private LongFilter trademarkId;

    private LongFilter userProfileId;

    private Boolean distinct;

    public DocumentsCriteria() {}

    public DocumentsCriteria(DocumentsCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentType = other.optionalDocumentType().map(DocumentTypeFilter::copy).orElse(null);
        this.fileContentType = other.optionalFileContentType().map(StringFilter::copy).orElse(null);
        this.fileName = other.optionalFileName().map(StringFilter::copy).orElse(null);
        this.fileUrl = other.optionalFileUrl().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.modifiedDate = other.optionalModifiedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DocumentStatusFilter::copy).orElse(null);
        this.trademarkId = other.optionalTrademarkId().map(LongFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DocumentsCriteria copy() {
        return new DocumentsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DocumentTypeFilter getDocumentType() {
        return documentType;
    }

    public Optional<DocumentTypeFilter> optionalDocumentType() {
        return Optional.ofNullable(documentType);
    }

    public DocumentTypeFilter documentType() {
        if (documentType == null) {
            setDocumentType(new DocumentTypeFilter());
        }
        return documentType;
    }

    public void setDocumentType(DocumentTypeFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getFileContentType() {
        return fileContentType;
    }

    public Optional<StringFilter> optionalFileContentType() {
        return Optional.ofNullable(fileContentType);
    }

    public StringFilter fileContentType() {
        if (fileContentType == null) {
            setFileContentType(new StringFilter());
        }
        return fileContentType;
    }

    public void setFileContentType(StringFilter fileContentType) {
        this.fileContentType = fileContentType;
    }

    public StringFilter getFileName() {
        return fileName;
    }

    public Optional<StringFilter> optionalFileName() {
        return Optional.ofNullable(fileName);
    }

    public StringFilter fileName() {
        if (fileName == null) {
            setFileName(new StringFilter());
        }
        return fileName;
    }

    public void setFileName(StringFilter fileName) {
        this.fileName = fileName;
    }

    public StringFilter getFileUrl() {
        return fileUrl;
    }

    public Optional<StringFilter> optionalFileUrl() {
        return Optional.ofNullable(fileUrl);
    }

    public StringFilter fileUrl() {
        if (fileUrl == null) {
            setFileUrl(new StringFilter());
        }
        return fileUrl;
    }

    public void setFileUrl(StringFilter fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTimeFilter getModifiedDate() {
        return modifiedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalModifiedDate() {
        return Optional.ofNullable(modifiedDate);
    }

    public ZonedDateTimeFilter modifiedDate() {
        if (modifiedDate == null) {
            setModifiedDate(new ZonedDateTimeFilter());
        }
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTimeFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public Optional<BooleanFilter> optionalDeleted() {
        return Optional.ofNullable(deleted);
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            setDeleted(new BooleanFilter());
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public DocumentStatusFilter getStatus() {
        return status;
    }

    public Optional<DocumentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DocumentStatusFilter status() {
        if (status == null) {
            setStatus(new DocumentStatusFilter());
        }
        return status;
    }

    public void setStatus(DocumentStatusFilter status) {
        this.status = status;
    }

    public LongFilter getTrademarkId() {
        return trademarkId;
    }

    public Optional<LongFilter> optionalTrademarkId() {
        return Optional.ofNullable(trademarkId);
    }

    public LongFilter trademarkId() {
        if (trademarkId == null) {
            setTrademarkId(new LongFilter());
        }
        return trademarkId;
    }

    public void setTrademarkId(LongFilter trademarkId) {
        this.trademarkId = trademarkId;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DocumentsCriteria that = (DocumentsCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(fileContentType, that.fileContentType) &&
            Objects.equals(fileName, that.fileName) &&
            Objects.equals(fileUrl, that.fileUrl) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(status, that.status) &&
            Objects.equals(trademarkId, that.trademarkId) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentType,
            fileContentType,
            fileName,
            fileUrl,
            createdDate,
            modifiedDate,
            deleted,
            status,
            trademarkId,
            userProfileId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentsCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentType().map(f -> "documentType=" + f + ", ").orElse("") +
            optionalFileContentType().map(f -> "fileContentType=" + f + ", ").orElse("") +
            optionalFileName().map(f -> "fileName=" + f + ", ").orElse("") +
            optionalFileUrl().map(f -> "fileUrl=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalModifiedDate().map(f -> "modifiedDate=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalTrademarkId().map(f -> "trademarkId=" + f + ", ").orElse("") +
            optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
