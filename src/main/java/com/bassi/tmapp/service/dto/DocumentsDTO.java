package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.DocumentStatus;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.Documents} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentsDTO implements Serializable {

    private Long id;

    private DocumentType documentType;

    private String fileContentType;

    private String fileName;

    private String fileUrl;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean deleted;

    private DocumentStatus status;

    private TrademarkDTO trademark;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
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

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public TrademarkDTO getTrademark() {
        return trademark;
    }

    public void setTrademark(TrademarkDTO trademark) {
        this.trademark = trademark;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
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
            ", fileContentType='" + getFileContentType() + "'" +
            ", fileName='" + getFileName() + "'" +
            ", fileUrl='" + getFileUrl() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", status='" + getStatus() + "'" +
            ", trademark=" + getTrademark() +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
