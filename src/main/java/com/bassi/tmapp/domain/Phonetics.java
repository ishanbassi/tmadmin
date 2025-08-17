package com.bassi.tmapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Phonetics.
 */
@Entity
@Table(name = "phonetics")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Phonetics implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sanitized_tm")
    private String sanitizedTm;

    @Column(name = "phonetic_pk")
    private String phoneticPk;

    @Column(name = "phonetic_sk")
    private String phoneticSk;

    @Column(name = "complete")
    private Boolean complete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "lead", "user", "trademarkClasses" }, allowSetters = true)
    private Trademark trademark;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Phonetics id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSanitizedTm() {
        return this.sanitizedTm;
    }

    public Phonetics sanitizedTm(String sanitizedTm) {
        this.setSanitizedTm(sanitizedTm);
        return this;
    }

    public void setSanitizedTm(String sanitizedTm) {
        this.sanitizedTm = sanitizedTm;
    }

    public String getPhoneticPk() {
        return this.phoneticPk;
    }

    public Phonetics phoneticPk(String phoneticPk) {
        this.setPhoneticPk(phoneticPk);
        return this;
    }

    public void setPhoneticPk(String phoneticPk) {
        this.phoneticPk = phoneticPk;
    }

    public String getPhoneticSk() {
        return this.phoneticSk;
    }

    public Phonetics phoneticSk(String phoneticSk) {
        this.setPhoneticSk(phoneticSk);
        return this;
    }

    public void setPhoneticSk(String phoneticSk) {
        this.phoneticSk = phoneticSk;
    }

    public Boolean getComplete() {
        return this.complete;
    }

    public Phonetics complete(Boolean complete) {
        this.setComplete(complete);
        return this;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }

    public Trademark getTrademark() {
        return this.trademark;
    }

    public void setTrademark(Trademark trademark) {
        this.trademark = trademark;
    }

    public Phonetics trademark(Trademark trademark) {
        this.setTrademark(trademark);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Phonetics)) {
            return false;
        }
        return getId() != null && getId().equals(((Phonetics) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Phonetics{" +
            "id=" + getId() +
            ", sanitizedTm='" + getSanitizedTm() + "'" +
            ", phoneticPk='" + getPhoneticPk() + "'" +
            ", phoneticSk='" + getPhoneticSk() + "'" +
            ", complete='" + getComplete() + "'" +
            "}";
    }
}
