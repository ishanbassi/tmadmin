package com.bassi.tmapp.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrademarkTokenFrequency.
 */
@Entity
@Table(name = "trademark_token_frequency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkTokenFrequency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "word")
    private String word;

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

    public TrademarkTokenFrequency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getFrequency() {
        return this.frequency;
    }

    public TrademarkTokenFrequency frequency(Integer frequency) {
        this.setFrequency(frequency);
        return this;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getWord() {
        return this.word;
    }

    public TrademarkTokenFrequency word(String word) {
        this.setWord(word);
        return this;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public TrademarkTokenFrequency createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public TrademarkTokenFrequency deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public TrademarkTokenFrequency modifiedDate(ZonedDateTime modifiedDate) {
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
        if (!(o instanceof TrademarkTokenFrequency)) {
            return false;
        }
        return getId() != null && getId().equals(((TrademarkTokenFrequency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkTokenFrequency{" +
            "id=" + getId() +
            ", frequency=" + getFrequency() +
            ", word='" + getWord() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
