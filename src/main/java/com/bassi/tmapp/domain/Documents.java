package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Documents.
 */
@Entity
@Table(name = "documents")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Documents implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType documentType;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lead", "user" }, allowSetters = true)
    private Trademark trademark;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Documents id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return this.documentType;
    }

    public Documents documentType(DocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public Documents fileContentType(String fileContentType) {
        this.setFileContentType(fileContentType);
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Documents fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return this.fileUrl;
    }

    public Documents fileUrl(String fileUrl) {
        this.setFileUrl(fileUrl);
        return this;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Documents createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Documents modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Documents deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Trademark getTrademark() {
        return this.trademark;
    }

    public void setTrademark(Trademark trademark) {
        this.trademark = trademark;
    }

    public Documents trademark(Trademark trademark) {
        this.setTrademark(trademark);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documents)) {
            return false;
        }
        return getId() != null && getId().equals(((Documents) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documents{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }
}
