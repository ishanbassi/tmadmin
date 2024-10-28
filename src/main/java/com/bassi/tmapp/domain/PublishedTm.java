package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PublishedTm.
 */
@Entity
@Table(name = "published_tm")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublishedTm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "details")
    private String details;

    @Column(name = "application_no")
    private Long applicationNo;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    @Column(name = "agent_name")
    private String agentName;

    @Column(name = "agent_address")
    private String agentAddress;

    @Column(name = "proprietor_name")
    private String proprietorName;

    @Column(name = "proprietor_address")
    private String proprietorAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "head_office")
    private HeadOffice headOffice;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "tm_class")
    private Integer tmClass;

    @Column(name = "journal_no")
    private Integer journalNo;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "usage")
    private String usage;

    @Column(name = "associated_tms")
    private String associatedTms;

    @Enumerated(EnumType.STRING)
    @Column(name = "trademark_status")
    private TrademarkStatus trademarkStatus;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PublishedTm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PublishedTm name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return this.details;
    }

    public PublishedTm details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getApplicationNo() {
        return this.applicationNo;
    }

    public PublishedTm applicationNo(Long applicationNo) {
        this.setApplicationNo(applicationNo);
        return this;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public PublishedTm applicationDate(LocalDate applicationDate) {
        this.setApplicationDate(applicationDate);
        return this;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public PublishedTm agentName(String agentName) {
        this.setAgentName(agentName);
        return this;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentAddress() {
        return this.agentAddress;
    }

    public PublishedTm agentAddress(String agentAddress) {
        this.setAgentAddress(agentAddress);
        return this;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getProprietorName() {
        return this.proprietorName;
    }

    public PublishedTm proprietorName(String proprietorName) {
        this.setProprietorName(proprietorName);
        return this;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorAddress() {
        return this.proprietorAddress;
    }

    public PublishedTm proprietorAddress(String proprietorAddress) {
        this.setProprietorAddress(proprietorAddress);
        return this;
    }

    public void setProprietorAddress(String proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public HeadOffice getHeadOffice() {
        return this.headOffice;
    }

    public PublishedTm headOffice(HeadOffice headOffice) {
        this.setHeadOffice(headOffice);
        return this;
    }

    public void setHeadOffice(HeadOffice headOffice) {
        this.headOffice = headOffice;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public PublishedTm imgUrl(String imgUrl) {
        this.setImgUrl(imgUrl);
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTmClass() {
        return this.tmClass;
    }

    public PublishedTm tmClass(Integer tmClass) {
        this.setTmClass(tmClass);
        return this;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public Integer getJournalNo() {
        return this.journalNo;
    }

    public PublishedTm journalNo(Integer journalNo) {
        this.setJournalNo(journalNo);
        return this;
    }

    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public PublishedTm deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsage() {
        return this.usage;
    }

    public PublishedTm usage(String usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAssociatedTms() {
        return this.associatedTms;
    }

    public PublishedTm associatedTms(String associatedTms) {
        this.setAssociatedTms(associatedTms);
        return this;
    }

    public void setAssociatedTms(String associatedTms) {
        this.associatedTms = associatedTms;
    }

    public TrademarkStatus getTrademarkStatus() {
        return this.trademarkStatus;
    }

    public PublishedTm trademarkStatus(TrademarkStatus trademarkStatus) {
        this.setTrademarkStatus(trademarkStatus);
        return this;
    }

    public void setTrademarkStatus(TrademarkStatus trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public PublishedTm createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public PublishedTm modifiedDate(ZonedDateTime modifiedDate) {
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
        if (!(o instanceof PublishedTm)) {
            return false;
        }
        return getId() != null && getId().equals(((PublishedTm) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublishedTm{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", details='" + getDetails() + "'" +
            ", applicationNo=" + getApplicationNo() +
            ", applicationDate='" + getApplicationDate() + "'" +
            ", agentName='" + getAgentName() + "'" +
            ", agentAddress='" + getAgentAddress() + "'" +
            ", proprietorName='" + getProprietorName() + "'" +
            ", proprietorAddress='" + getProprietorAddress() + "'" +
            ", headOffice='" + getHeadOffice() + "'" +
            ", imgUrl='" + getImgUrl() + "'" +
            ", tmClass=" + getTmClass() +
            ", journalNo=" + getJournalNo() +
            ", deleted='" + getDeleted() + "'" +
            ", usage='" + getUsage() + "'" +
            ", associatedTms='" + getAssociatedTms() + "'" +
            ", trademarkStatus='" + getTrademarkStatus() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
