package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TrademarkPlan} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkPlanDTO implements Serializable {

    private Long id;

    private String name;

    private BigDecimal fees;

    private String notes;

    private ZonedDateTime createdDate;

    private Boolean deleted;

    private ZonedDateTime modifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkPlanDTO)) {
            return false;
        }

        TrademarkPlanDTO trademarkPlanDTO = (TrademarkPlanDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trademarkPlanDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkPlanDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fees=" + getFees() +
            ", notes='" + getNotes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
