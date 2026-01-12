package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.Trademark} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkDTO implements Serializable {

    private Long id;

    private String name;

    private String details;

    private Long applicationNo;

    private LocalDate applicationDate;

    private String agentName;

    private String agentAddress;

    private String proprietorName;

    private String proprietorAddress;

    private HeadOffice headOffice;

    private String imgUrl;

    private Integer tmClass;

    private Integer journalNo;

    private Boolean deleted;

    private String usage;

    private String associatedTms;

    private TrademarkStatus trademarkStatus;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private LocalDate renewalDate;

    private TrademarkType type;

    private Integer pageNo;

    private TrademarkSource source;

    private String phoneNumber;

    private String email;

    private String organizationType;

    private String normalizedName;

    private LeadDTO lead;

    private UserProfileDTO user;

    private TrademarkPlanDTO trademarkPlan;

    private TmAgentDTO tmAgent;

    private Set<TrademarkClassDTO> trademarkClasses = new HashSet<>();

    private Set<DocumentsDTO> documents = new HashSet<>();

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Long getApplicationNo() {
        return applicationNo;
    }

    public void setApplicationNo(Long applicationNo) {
        this.applicationNo = applicationNo;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }

    public String getProprietorName() {
        return proprietorName;
    }

    public void setProprietorName(String proprietorName) {
        this.proprietorName = proprietorName;
    }

    public String getProprietorAddress() {
        return proprietorAddress;
    }

    public void setProprietorAddress(String proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public HeadOffice getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(HeadOffice headOffice) {
        this.headOffice = headOffice;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTmClass() {
        return tmClass;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public Integer getJournalNo() {
        return journalNo;
    }

    public void setJournalNo(Integer journalNo) {
        this.journalNo = journalNo;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getAssociatedTms() {
        return associatedTms;
    }

    public void setAssociatedTms(String associatedTms) {
        this.associatedTms = associatedTms;
    }

    public TrademarkStatus getTrademarkStatus() {
        return trademarkStatus;
    }

    public void setTrademarkStatus(TrademarkStatus trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
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

    public LocalDate getRenewalDate() {
        return renewalDate;
    }

    public void setRenewalDate(LocalDate renewalDate) {
        this.renewalDate = renewalDate;
    }

    public TrademarkType getType() {
        return type;
    }

    public void setType(TrademarkType type) {
        this.type = type;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public TrademarkSource getSource() {
        return source;
    }

    public void setSource(TrademarkSource source) {
        this.source = source;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getNormalizedName() {
        return normalizedName;
    }

    public void setNormalizedName(String normalizedName) {
        this.normalizedName = normalizedName;
    }

    public LeadDTO getLead() {
        return lead;
    }

    public void setLead(LeadDTO lead) {
        this.lead = lead;
    }

    public UserProfileDTO getUser() {
        return user;
    }

    public void setUser(UserProfileDTO user) {
        this.user = user;
    }

    public TrademarkPlanDTO getTrademarkPlan() {
        return trademarkPlan;
    }

    public void setTrademarkPlan(TrademarkPlanDTO trademarkPlan) {
        this.trademarkPlan = trademarkPlan;
    }

    public TmAgentDTO getTmAgent() {
        return tmAgent;
    }

    public void setTmAgent(TmAgentDTO tmAgent) {
        this.tmAgent = tmAgent;
    }

    public Set<TrademarkClassDTO> getTrademarkClasses() {
        return trademarkClasses;
    }

    public void setTrademarkClasses(Set<TrademarkClassDTO> trademarkClasses) {
        this.trademarkClasses = trademarkClasses;
    }

    public Set<DocumentsDTO> getDocuments() {
        return documents;
    }

    public void setDocuments(Set<DocumentsDTO> documents) {
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrademarkDTO)) {
            return false;
        }

        TrademarkDTO trademarkDTO = (TrademarkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, trademarkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkDTO{" +
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
            ", lead=" + getLead() +
            ", user=" + getUser() +
            ", trademarkPlan=" + getTrademarkPlan() +
            ", tmAgent=" + getTmAgent() +
            ", trademarkClasses=" + getTrademarkClasses() +
            "}";
    }
}
