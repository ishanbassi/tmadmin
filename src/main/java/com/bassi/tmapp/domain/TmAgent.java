package com.bassi.tmapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TmAgent.
 */
@Entity
@Table(name = "tm_agent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TmAgent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "agent_code")
    private String agentCode;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "company_name")
    private String companyName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tmAgent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tmAgent" }, allowSetters = true)
    private Set<Trademark> trademarks = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TmAgent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAgentCode() {
        return this.agentCode;
    }

    public TmAgent agentCode(String agentCode) {
        this.setAgentCode(agentCode);
        return this;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public TmAgent firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public TmAgent lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return this.address;
    }

    public TmAgent address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public TmAgent createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public TmAgent modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public TmAgent deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public TmAgent companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Set<Trademark> getTrademarks() {
        return this.trademarks;
    }

    public void setTrademarks(Set<Trademark> trademarks) {
        if (this.trademarks != null) {
            this.trademarks.forEach(i -> i.setTmAgent(null));
        }
        if (trademarks != null) {
            trademarks.forEach(i -> i.setTmAgent(this));
        }
        this.trademarks = trademarks;
    }

    public TmAgent trademarks(Set<Trademark> trademarks) {
        this.setTrademarks(trademarks);
        return this;
    }

    public TmAgent addTrademarks(Trademark trademark) {
        this.trademarks.add(trademark);
        trademark.setTmAgent(this);
        return this;
    }

    public TmAgent removeTrademarks(Trademark trademark) {
        this.trademarks.remove(trademark);
        trademark.setTmAgent(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TmAgent)) {
            return false;
        }
        return getId() != null && getId().equals(((TmAgent) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TmAgent{" +
            "id=" + getId() +
            ", agentCode='" + getAgentCode() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", address='" + getAddress() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            "}";
    }
    
    @PrePersist
    private void beforeSave() {
        this.createdDate = ZonedDateTime.now();
        this.modifiedDate = ZonedDateTime.now();
    }

    @PreUpdate
    private void beforeUpdate() {
        this.modifiedDate = ZonedDateTime.now();
    }
}
