package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TmAgent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TmAgentDTO implements Serializable {

    private Long id;

    private String agentCode;

    private String address;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean deleted;

    private String companyName;

    private String fullName;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TmAgentDTO)) {
            return false;
        }

        TmAgentDTO tmAgentDTO = (TmAgentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tmAgentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return (
            "TmAgentDTO [id=" +
            id +
            ", agentCode=" +
            agentCode +
            ", address=" +
            address +
            ", createdDate=" +
            createdDate +
            ", modifiedDate=" +
            modifiedDate +
            ", deleted=" +
            deleted +
            ", companyName=" +
            companyName +
            ", fullName=" +
            fullName +
            "]"
        );
    }
}
