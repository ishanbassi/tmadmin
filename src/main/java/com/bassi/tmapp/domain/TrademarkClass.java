package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.LeadStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrademarkClass.
 */
@Entity
@Table(name = "trademark_class")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private Integer code;

    @Column(name = "tm_class")
    private Integer tmClass;

    @Column(name = "keyword")
    private String keyword;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "trademarkClasses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "lead", "user", "trademarkPlan", "trademarkClasses" }, allowSetters = true)
    private Set<Trademark> trademarks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TrademarkClass id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCode() {
        return this.code;
    }

    public TrademarkClass code(Integer code) {
        this.setCode(code);
        return this;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getTmClass() {
        return this.tmClass;
    }

    public TrademarkClass tmClass(Integer tmClass) {
        this.setTmClass(tmClass);
        return this;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public TrademarkClass keyword(String keyword) {
        this.setKeyword(keyword);
        return this;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return this.title;
    }

    public TrademarkClass title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public TrademarkClass description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public TrademarkClass createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public TrademarkClass modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public TrademarkClass deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Trademark> getTrademarks() {
        return this.trademarks;
    }

    public void setTrademarks(Set<Trademark> trademarks) {
        if (this.trademarks != null) {
            this.trademarks.forEach(i -> i.removeTrademarkClasses(this));
        }
        if (trademarks != null) {
            trademarks.forEach(i -> i.addTrademarkClasses(this));
        }
        this.trademarks = trademarks;
    }

    public TrademarkClass trademarks(Set<Trademark> trademarks) {
        this.setTrademarks(trademarks);
        return this;
    }

    public TrademarkClass addTrademarks(Trademark trademark) {
        this.trademarks.add(trademark);
        trademark.getTrademarkClasses().add(this);
        return this;
    }

    public TrademarkClass removeTrademarks(Trademark trademark) {
        this.trademarks.remove(trademark);
        trademark.getTrademarkClasses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkClass)) {
            return false;
        }
        return getId() != null && getId().equals(((TrademarkClass) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkClass{" +
            "id=" + getId() +
            ", code=" + getCode() +
            ", tmClass=" + getTmClass() +
            ", keyword='" + getKeyword() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            "}";
    }

    @PrePersist
    private void beforeSave() {
        this.createdDate = ZonedDateTime.now();
        this.modifiedDate = ZonedDateTime.now();
        this.deleted = false;
    }

    @PreUpdate
    private void beforeUpdate() {
        this.modifiedDate = ZonedDateTime.now();
    }
}
