package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trademark.
 */
@Entity
@Table(name = "trademark")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Trademark implements Serializable {

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

    @Column(name = "trademark_status")
    private String trademarkStatus;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "renewal_date")
    private LocalDate renewalDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TrademarkType type;

    @Column(name = "page_no")
    private Integer pageNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "source")
    private TrademarkSource source;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "organization_type")
    private String organizationType;

    @Column(name = "normalized_name")
    private String normalizedName;

    @Column(name = "filing_mode")
    private String filingMode;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "assignedTo" }, allowSetters = true)
    private Lead lead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "documents" }, allowSetters = true)
    private UserProfile user;

    @ManyToOne(fetch = FetchType.LAZY)
    private TrademarkPlan trademarkPlan;

    @ManyToOne(fetch = FetchType.LAZY)
    private TmAgent tmAgent;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_trademark__trademark_classes",
        joinColumns = @JoinColumn(name = "trademark_id"),
        inverseJoinColumns = @JoinColumn(name = "trademark_classes_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trademarks" }, allowSetters = true)
    private Set<TrademarkClass> trademarkClasses = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "trademark")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trademark", "userProfile" }, allowSetters = true)
    private Set<Documents> documents = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Trademark id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Trademark name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return this.details;
    }

    public Trademark details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getApplicationNo() {
        return this.applicationNo;
    }

    public Trademark applicationNo(Long applicationNo) {
        this.setApplicationNo(applicationNo);
        return this;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public LocalDate getApplicationDate() {
        return this.applicationDate;
    }

    public Trademark applicationDate(LocalDate applicationDate) {
        this.setApplicationDate(applicationDate);
        return this;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAgentName() {
        return this.agentName;
    }

    public Trademark agentName(String agentName) {
        this.setAgentName(agentName);
        return this;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentAddress() {
        return this.agentAddress;
    }

    public Trademark agentAddress(String agentAddress) {
        this.setAgentAddress(agentAddress);
        return this;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getProprietorName() {
        return this.proprietorName;
    }

    public Trademark proprietorName(String proprietorName) {
        this.setProprietorName(proprietorName);
        return this;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorAddress() {
        return this.proprietorAddress;
    }

    public Trademark proprietorAddress(String proprietorAddress) {
        this.setProprietorAddress(proprietorAddress);
        return this;
    }

    public void setProprietorAddress(String proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public HeadOffice getHeadOffice() {
        return this.headOffice;
    }

    public Trademark headOffice(HeadOffice headOffice) {
        this.setHeadOffice(headOffice);
        return this;
    }

    public void setHeadOffice(HeadOffice headOffice) {
        this.headOffice = headOffice;
    }

    public String getImgUrl() {
        return this.imgUrl;
    }

    public Trademark imgUrl(String imgUrl) {
        this.setImgUrl(imgUrl);
        return this;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTmClass() {
        return this.tmClass;
    }

    public Trademark tmClass(Integer tmClass) {
        this.setTmClass(tmClass);
        return this;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public Integer getJournalNo() {
        return this.journalNo;
    }

    public Trademark journalNo(Integer journalNo) {
        this.setJournalNo(journalNo);
        return this;
    }

    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Trademark deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsage() {
        return this.usage;
    }

    public Trademark usage(String usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAssociatedTms() {
        return this.associatedTms;
    }

    public Trademark associatedTms(String associatedTms) {
        this.setAssociatedTms(associatedTms);
        return this;
    }

    public void setAssociatedTms(String associatedTms) {
        this.associatedTms = associatedTms;
    }

    public String getTrademarkStatus() {
        return this.trademarkStatus;
    }

    public Trademark trademarkStatus(String trademarkStatus) {
        this.setTrademarkStatus(trademarkStatus);
        return this;
    }

    public void setTrademarkStatus(String trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Trademark createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Trademark modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LocalDate getRenewalDate() {
        return this.renewalDate;
    }

    public Trademark renewalDate(LocalDate renewalDate) {
        this.setRenewalDate(renewalDate);
        return this;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public TrademarkType getType() {
        return this.type;
    }

    public Trademark type(TrademarkType type) {
        this.setType(type);
        return this;
    }

    public void setType(TrademarkType type) {
        this.type = type;
    }

    public Integer getPageNo() {
        return this.pageNo;
    }

    public Trademark pageNo(Integer pageNo) {
        this.setPageNo(pageNo);
        return this;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public TrademarkSource getSource() {
        return this.source;
    }

    public Trademark source(TrademarkSource source) {
        this.setSource(source);
        return this;
    }

    public void setSource(TrademarkSource source) {
        this.source = source;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Trademark phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Trademark email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationType() {
        return this.organizationType;
    }

    public Trademark organizationType(String organizationType) {
        this.setOrganizationType(organizationType);
        return this;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getNormalizedName() {
        return this.normalizedName;
    }

    public Trademark normalizedName(String normalizedName) {
        this.setNormalizedName(normalizedName);
        return this;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public String getFilingMode() {
        return this.filingMode;
    }

    public Trademark filingMode(String filingMode) {
        this.setFilingMode(filingMode);
        return this;
    }

    public void setFilingMode(String filingMode) {
        this.filingMode = filingMode;
    }

    public String getState() {
        return this.state;
    }

    public Trademark state(String state) {
        this.setState(state);
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return this.country;
    }

    public Trademark country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Lead getLead() {
        return this.lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Trademark lead(Lead lead) {
        this.setLead(lead);
        return this;
    }

    public UserProfile getUser() {
        return this.user;
    }

    public void setUser(UserProfile userProfile) {
        this.user = userProfile;
    }

    public Trademark user(UserProfile userProfile) {
        this.setUser(userProfile);
        return this;
    }

    public TrademarkPlan getTrademarkPlan() {
        return this.trademarkPlan;
    }

    public void setTrademarkPlan(TrademarkPlan trademarkPlan) {
        this.trademarkPlan = trademarkPlan;
    }

    public Trademark trademarkPlan(TrademarkPlan trademarkPlan) {
        this.setTrademarkPlan(trademarkPlan);
        return this;
    }

    public TmAgent getTmAgent() {
        return this.tmAgent;
    }

    public void setTmAgent(TmAgent tmAgent) {
        this.tmAgent = tmAgent;
    }

    public Trademark tmAgent(TmAgent tmAgent) {
        this.setTmAgent(tmAgent);
        return this;
    }

    public Set<TrademarkClass> getTrademarkClasses() {
        return this.trademarkClasses;
    }

    public void setTrademarkClasses(Set<TrademarkClass> trademarkClasses) {
        this.trademarkClasses = trademarkClasses;
    }

    public Trademark trademarkClasses(Set<TrademarkClass> trademarkClasses) {
        this.setTrademarkClasses(trademarkClasses);
        return this;
    }

    public Trademark addTrademarkClasses(TrademarkClass trademarkClass) {
        this.trademarkClasses.add(trademarkClass);
        return this;
    }

    public Trademark removeTrademarkClasses(TrademarkClass trademarkClass) {
        this.trademarkClasses.remove(trademarkClass);
        return this;
    }

    public Set<Documents> getDocuments() {
        return this.documents;
    }

    public void setDocuments(Set<Documents> documents) {
        if (this.documents != null) {
            this.documents.forEach(i -> i.setTrademark(null));
        }
        if (documents != null) {
            documents.forEach(i -> i.setTrademark(this));
        }
        this.documents = documents;
    }

    public Trademark documents(Set<Documents> documents) {
        this.setDocuments(documents);
        return this;
    }

    public Trademark addDocuments(Documents documents) {
        this.documents.add(documents);
        documents.setTrademark(this);
        return this;
    }

    public Trademark removeDocuments(Documents documents) {
        this.documents.remove(documents);
        documents.setTrademark(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trademark)) {
            return false;
        }
        return getId() != null && getId().equals(((Trademark) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trademark{" +
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
            ", renewalDate='" + getRenewalDate() + "'" +
            ", type='" + getType() + "'" +
            ", pageNo=" + getPageNo() +
            ", source='" + getSource() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", organizationType='" + getOrganizationType() + "'" +
            ", normalizedName='" + getNormalizedName() + "'" +
            ", filingMode='" + getFilingMode() + "'" +
            ", state='" + getState() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
