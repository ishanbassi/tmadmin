package com.bassi.tmapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrademarkPlan.
 */
@Entity
@Table(name = "trademark_plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkPlan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "fees", precision = 21, scale = 2)
    private BigDecimal fees;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrademarkPlan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public TrademarkPlan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getFees() {
        return this.fees;
    }

    public TrademarkPlan fees(BigDecimal fees) {
        this.setFees(fees);
        return this;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public String getNotes() {
        return this.notes;
    }

    public TrademarkPlan notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public TrademarkPlan createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public TrademarkPlan deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public TrademarkPlan modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkPlan)) {
            return false;
        }
        return getId() != null && getId().equals(((TrademarkPlan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkPlan{" +
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
