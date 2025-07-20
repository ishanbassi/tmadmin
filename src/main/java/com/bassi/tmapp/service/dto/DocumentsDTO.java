package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.Documents} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentsDTO implements Serializable {

    private Long id;

    private String documentType;

    private String fileUrl;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean deleted;

    private TrademarkDTO trademark;

    private UserProfileDTO user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public TrademarkDTO getTrademark() {
        return trademark;
    }

    public void setTrademark(TrademarkDTO trademark) {
        this.trademark = trademark;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentsDTO)) {
            return false;
        }

        DocumentsDTO documentsDTO = (DocumentsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentsDTO{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", trademark=" + getTrademark() +
            ", user=" + getUser() +
            "}";
    }
}
