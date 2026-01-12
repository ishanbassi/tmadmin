package com.bassi.tmapp.service.criteria;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.domain.enumeration.TrademarkType;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bassi.tmapp.domain.Trademark} entity. This class is used
 * in {@link com.bassi.tmapp.web.rest.TrademarkResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /trademarks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TrademarkCriteria implements Serializable, Criteria {

    /**
     * Class for filtering HeadOffice
     */
    public static class HeadOfficeFilter extends Filter<HeadOffice> {

        public HeadOfficeFilter() {}

        public HeadOfficeFilter(HeadOfficeFilter filter) {
            super(filter);
        }

        @Override
        public HeadOfficeFilter copy() {
            return new HeadOfficeFilter(this);
        }
    }

    /**
     * Class for filtering TrademarkStatus
     */
    public static class TrademarkStatusFilter extends Filter<TrademarkStatus> {

        public TrademarkStatusFilter() {}

        public TrademarkStatusFilter(TrademarkStatusFilter filter) {
            super(filter);
        }

        @Override
        public TrademarkStatusFilter copy() {
            return new TrademarkStatusFilter(this);
        }
    }

    /**
     * Class for filtering TrademarkType
     */
    public static class TrademarkTypeFilter extends Filter<TrademarkType> {

        public TrademarkTypeFilter() {}

        public TrademarkTypeFilter(TrademarkTypeFilter filter) {
            super(filter);
        }

        @Override
        public TrademarkTypeFilter copy() {
            return new TrademarkTypeFilter(this);
        }
    }

    /**
     * Class for filtering TrademarkSource
     */
    public static class TrademarkSourceFilter extends Filter<TrademarkSource> {

        public TrademarkSourceFilter() {}

        public TrademarkSourceFilter(TrademarkSourceFilter filter) {
            super(filter);
        }

        @Override
        public TrademarkSourceFilter copy() {
            return new TrademarkSourceFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter details;

    private LongFilter applicationNo;

    private LocalDateFilter applicationDate;

    private StringFilter agentName;

    private StringFilter agentAddress;

    private StringFilter proprietorName;

    private StringFilter proprietorAddress;

    private HeadOfficeFilter headOffice;

    private StringFilter imgUrl;

    private IntegerFilter tmClass;

    private IntegerFilter journalNo;

    private BooleanFilter deleted;

    private StringFilter usage;

    private StringFilter associatedTms;

    private TrademarkStatusFilter trademarkStatus;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter modifiedDate;

    private LocalDateFilter renewalDate;

    private TrademarkTypeFilter type;

    private IntegerFilter pageNo;

    private TrademarkSourceFilter source;

    private StringFilter phoneNumber;

    private StringFilter email;

    private StringFilter organizationType;

    private StringFilter normalizedName;

    private LongFilter leadId;

    private LongFilter userId;

    private LongFilter trademarkPlanId;

    private LongFilter tmAgentId;

    private LongFilter trademarkClassesId;

    private LongFilter documentsId;

    private Boolean distinct;

    public TrademarkCriteria() {}

    public TrademarkCriteria(TrademarkCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.details = other.optionalDetails().map(StringFilter::copy).orElse(null);
        this.applicationNo = other.optionalApplicationNo().map(LongFilter::copy).orElse(null);
        this.applicationDate = other.optionalApplicationDate().map(LocalDateFilter::copy).orElse(null);
        this.agentName = other.optionalAgentName().map(StringFilter::copy).orElse(null);
        this.agentAddress = other.optionalAgentAddress().map(StringFilter::copy).orElse(null);
        this.proprietorName = other.optionalProprietorName().map(StringFilter::copy).orElse(null);
        this.proprietorAddress = other.optionalProprietorAddress().map(StringFilter::copy).orElse(null);
        this.headOffice = other.optionalHeadOffice().map(HeadOfficeFilter::copy).orElse(null);
        this.imgUrl = other.optionalImgUrl().map(StringFilter::copy).orElse(null);
        this.tmClass = other.optionalTmClass().map(IntegerFilter::copy).orElse(null);
        this.journalNo = other.optionalJournalNo().map(IntegerFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.usage = other.optionalUsage().map(StringFilter::copy).orElse(null);
        this.associatedTms = other.optionalAssociatedTms().map(StringFilter::copy).orElse(null);
        this.trademarkStatus = other.optionalTrademarkStatus().map(TrademarkStatusFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.modifiedDate = other.optionalModifiedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.renewalDate = other.optionalRenewalDate().map(LocalDateFilter::copy).orElse(null);
        this.type = other.optionalType().map(TrademarkTypeFilter::copy).orElse(null);
        this.pageNo = other.optionalPageNo().map(IntegerFilter::copy).orElse(null);
        this.source = other.optionalSource().map(TrademarkSourceFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.organizationType = other.optionalOrganizationType().map(StringFilter::copy).orElse(null);
        this.normalizedName = other.optionalNormalizedName().map(StringFilter::copy).orElse(null);
        this.leadId = other.optionalLeadId().map(LongFilter::copy).orElse(null);
        this.userId = other.optionalUserId().map(LongFilter::copy).orElse(null);
        this.trademarkPlanId = other.optionalTrademarkPlanId().map(LongFilter::copy).orElse(null);
        this.tmAgentId = other.optionalTmAgentId().map(LongFilter::copy).orElse(null);
        this.trademarkClassesId = other.optionalTrademarkClassesId().map(LongFilter::copy).orElse(null);
        this.documentsId = other.optionalDocumentsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public TrademarkCriteria copy() {
        return new TrademarkCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDetails() {
        return details;
    }

    public Optional<StringFilter> optionalDetails() {
        return Optional.ofNullable(details);
    }

    public StringFilter details() {
        if (details == null) {
            setDetails(new StringFilter());
        }
        return details;
    }

    public void setDetails(StringFilter details) {
        this.details = details;
    }

    public LongFilter getApplicationNo() {
        return applicationNo;
    }

    public Optional<LongFilter> optionalApplicationNo() {
        return Optional.ofNullable(applicationNo);
    }

    public LongFilter applicationNo() {
        if (applicationNo == null) {
            setApplicationNo(new LongFilter());
        }
        return applicationNo;
    }

    public void setApplicationNo(LongFilter applicationNo) {
        this.applicationNo = applicationNo;
    }

    public LocalDateFilter getApplicationDate() {
        return applicationDate;
    }

    public Optional<LocalDateFilter> optionalApplicationDate() {
        return Optional.ofNullable(applicationDate);
    }

    public LocalDateFilter applicationDate() {
        if (applicationDate == null) {
            setApplicationDate(new LocalDateFilter());
        }
        return applicationDate;
    }

    public void setApplicationDate(LocalDateFilter applicationDate) {
        this.applicationDate = applicationDate;
    }

    public StringFilter getAgentName() {
        return agentName;
    }

    public Optional<StringFilter> optionalAgentName() {
        return Optional.ofNullable(agentName);
    }

    public StringFilter agentName() {
        if (agentName == null) {
            setAgentName(new StringFilter());
        }
        return agentName;
    }

    public void setAgentName(StringFilter agentName) {
        this.agentName = agentName;
    }

    public StringFilter getAgentAddress() {
        return agentAddress;
    }

    public Optional<StringFilter> optionalAgentAddress() {
        return Optional.ofNullable(agentAddress);
    }

    public StringFilter agentAddress() {
        if (agentAddress == null) {
            setAgentAddress(new StringFilter());
        }
        return agentAddress;
    }

    public void setAgentAddress(StringFilter agentAddress) {
        this.agentAddress = agentAddress;
    }

    public StringFilter getProprietorName() {
        return proprietorName;
    }

    public Optional<StringFilter> optionalProprietorName() {
        return Optional.ofNullable(proprietorName);
    }

    public StringFilter proprietorName() {
        if (proprietorName == null) {
            setProprietorName(new StringFilter());
        }
        return proprietorName;
    }

    public void setProprietorName(StringFilter proprietorName) {
        this.proprietorName = proprietorName;
    }

    public StringFilter getProprietorAddress() {
        return proprietorAddress;
    }

    public Optional<StringFilter> optionalProprietorAddress() {
        return Optional.ofNullable(proprietorAddress);
    }

    public StringFilter proprietorAddress() {
        if (proprietorAddress == null) {
            setProprietorAddress(new StringFilter());
        }
        return proprietorAddress;
    }

    public void setProprietorAddress(StringFilter proprietorAddress) {
        this.proprietorAddress = proprietorAddress;
    }

    public HeadOfficeFilter getHeadOffice() {
        return headOffice;
    }

    public Optional<HeadOfficeFilter> optionalHeadOffice() {
        return Optional.ofNullable(headOffice);
    }

    public HeadOfficeFilter headOffice() {
        if (headOffice == null) {
            setHeadOffice(new HeadOfficeFilter());
        }
        return headOffice;
    }

    public void setHeadOffice(HeadOfficeFilter headOffice) {
        this.headOffice = headOffice;
    }

    public StringFilter getImgUrl() {
        return imgUrl;
    }

    public Optional<StringFilter> optionalImgUrl() {
        return Optional.ofNullable(imgUrl);
    }

    public StringFilter imgUrl() {
        if (imgUrl == null) {
            setImgUrl(new StringFilter());
        }
        return imgUrl;
    }

    public void setImgUrl(StringFilter imgUrl) {
        this.imgUrl = imgUrl;
    }

    public IntegerFilter getTmClass() {
        return tmClass;
    }

    public Optional<IntegerFilter> optionalTmClass() {
        return Optional.ofNullable(tmClass);
    }

    public IntegerFilter tmClass() {
        if (tmClass == null) {
            setTmClass(new IntegerFilter());
        }
        return tmClass;
    }

    public void setTmClass(IntegerFilter tmClass) {
        this.tmClass = tmClass;
    }

    public IntegerFilter getJournalNo() {
        return journalNo;
    }

    public Optional<IntegerFilter> optionalJournalNo() {
        return Optional.ofNullable(journalNo);
    }

    public IntegerFilter journalNo() {
        if (journalNo == null) {
            setJournalNo(new IntegerFilter());
        }
        return journalNo;
    }

    public void setJournalNo(IntegerFilter journalNo) {
        this.journalNo = journalNo;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public Optional<BooleanFilter> optionalDeleted() {
        return Optional.ofNullable(deleted);
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            setDeleted(new BooleanFilter());
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public StringFilter getUsage() {
        return usage;
    }

    public Optional<StringFilter> optionalUsage() {
        return Optional.ofNullable(usage);
    }

    public StringFilter usage() {
        if (usage == null) {
            setUsage(new StringFilter());
        }
        return usage;
    }

    public void setUsage(StringFilter usage) {
        this.usage = usage;
    }

    public StringFilter getAssociatedTms() {
        return associatedTms;
    }

    public Optional<StringFilter> optionalAssociatedTms() {
        return Optional.ofNullable(associatedTms);
    }

    public StringFilter associatedTms() {
        if (associatedTms == null) {
            setAssociatedTms(new StringFilter());
        }
        return associatedTms;
    }

    public void setAssociatedTms(StringFilter associatedTms) {
        this.associatedTms = associatedTms;
    }

    public TrademarkStatusFilter getTrademarkStatus() {
        return trademarkStatus;
    }

    public Optional<TrademarkStatusFilter> optionalTrademarkStatus() {
        return Optional.ofNullable(trademarkStatus);
    }

    public TrademarkStatusFilter trademarkStatus() {
        if (trademarkStatus == null) {
            setTrademarkStatus(new TrademarkStatusFilter());
        }
        return trademarkStatus;
    }

    public void setTrademarkStatus(TrademarkStatusFilter trademarkStatus) {
        this.trademarkStatus = trademarkStatus;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTimeFilter getModifiedDate() {
        return modifiedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalModifiedDate() {
        return Optional.ofNullable(modifiedDate);
    }

    public ZonedDateTimeFilter modifiedDate() {
        if (modifiedDate == null) {
            setModifiedDate(new ZonedDateTimeFilter());
        }
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTimeFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LocalDateFilter getRenewalDate() {
        return renewalDate;
    }

    public Optional<LocalDateFilter> optionalRenewalDate() {
        return Optional.ofNullable(renewalDate);
    }

    public LocalDateFilter renewalDate() {
        if (renewalDate == null) {
            setRenewalDate(new LocalDateFilter());
        }
        return renewalDate;
    }

    public void setRenewalDate(LocalDateFilter renewalDate) {
        this.renewalDate = renewalDate;
    }

    public TrademarkTypeFilter getType() {
        return type;
    }

    public Optional<TrademarkTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public TrademarkTypeFilter type() {
        if (type == null) {
            setType(new TrademarkTypeFilter());
        }
        return type;
    }

    public void setType(TrademarkTypeFilter type) {
        this.type = type;
    }

    public IntegerFilter getPageNo() {
        return pageNo;
    }

    public Optional<IntegerFilter> optionalPageNo() {
        return Optional.ofNullable(pageNo);
    }

    public IntegerFilter pageNo() {
        if (pageNo == null) {
            setPageNo(new IntegerFilter());
        }
        return pageNo;
    }

    public void setPageNo(IntegerFilter pageNo) {
        this.pageNo = pageNo;
    }

    public TrademarkSourceFilter getSource() {
        return source;
    }

    public Optional<TrademarkSourceFilter> optionalSource() {
        return Optional.ofNullable(source);
    }

    public TrademarkSourceFilter source() {
        if (source == null) {
            setSource(new TrademarkSourceFilter());
        }
        return source;
    }

    public void setSource(TrademarkSourceFilter source) {
        this.source = source;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<StringFilter> optionalPhoneNumber() {
        return Optional.ofNullable(phoneNumber);
    }

    public StringFilter phoneNumber() {
        if (phoneNumber == null) {
            setPhoneNumber(new StringFilter());
        }
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getOrganizationType() {
        return organizationType;
    }

    public Optional<StringFilter> optionalOrganizationType() {
        return Optional.ofNullable(organizationType);
    }

    public StringFilter organizationType() {
        if (organizationType == null) {
            setOrganizationType(new StringFilter());
        }
        return organizationType;
    }

    public void setOrganizationType(StringFilter organizationType) {
        this.organizationType = organizationType;
    }

    public StringFilter getNormalizedName() {
        return normalizedName;
    }

    public Optional<StringFilter> optionalNormalizedName() {
        return Optional.ofNullable(normalizedName);
    }

    public StringFilter normalizedName() {
        if (normalizedName == null) {
            setNormalizedName(new StringFilter());
        }
        return normalizedName;
    }

    public void setNormalizedName(StringFilter normalizedName) {
        this.normalizedName = normalizedName;
    }

    public LongFilter getLeadId() {
        return leadId;
    }

    public Optional<LongFilter> optionalLeadId() {
        return Optional.ofNullable(leadId);
    }

    public LongFilter leadId() {
        if (leadId == null) {
            setLeadId(new LongFilter());
        }
        return leadId;
    }

    public void setLeadId(LongFilter leadId) {
        this.leadId = leadId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public Optional<LongFilter> optionalUserId() {
        return Optional.ofNullable(userId);
    }

    public LongFilter userId() {
        if (userId == null) {
            setUserId(new LongFilter());
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getTrademarkPlanId() {
        return trademarkPlanId;
    }

    public Optional<LongFilter> optionalTrademarkPlanId() {
        return Optional.ofNullable(trademarkPlanId);
    }

    public LongFilter trademarkPlanId() {
        if (trademarkPlanId == null) {
            setTrademarkPlanId(new LongFilter());
        }
        return trademarkPlanId;
    }

    public void setTrademarkPlanId(LongFilter trademarkPlanId) {
        this.trademarkPlanId = trademarkPlanId;
    }

    public LongFilter getTmAgentId() {
        return tmAgentId;
    }

    public Optional<LongFilter> optionalTmAgentId() {
        return Optional.ofNullable(tmAgentId);
    }

    public LongFilter tmAgentId() {
        if (tmAgentId == null) {
            setTmAgentId(new LongFilter());
        }
        return tmAgentId;
    }

    public void setTmAgentId(LongFilter tmAgentId) {
        this.tmAgentId = tmAgentId;
    }

    public LongFilter getTrademarkClassesId() {
        return trademarkClassesId;
    }

    public Optional<LongFilter> optionalTrademarkClassesId() {
        return Optional.ofNullable(trademarkClassesId);
    }

    public LongFilter trademarkClassesId() {
        if (trademarkClassesId == null) {
            setTrademarkClassesId(new LongFilter());
        }
        return trademarkClassesId;
    }

    public void setTrademarkClassesId(LongFilter trademarkClassesId) {
        this.trademarkClassesId = trademarkClassesId;
    }

    public LongFilter getDocumentsId() {
        return documentsId;
    }

    public Optional<LongFilter> optionalDocumentsId() {
        return Optional.ofNullable(documentsId);
    }

    public LongFilter documentsId() {
        if (documentsId == null) {
            setDocumentsId(new LongFilter());
        }
        return documentsId;
    }

    public void setDocumentsId(LongFilter documentsId) {
        this.documentsId = documentsId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TrademarkCriteria that = (TrademarkCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(details, that.details) &&
            Objects.equals(applicationNo, that.applicationNo) &&
            Objects.equals(applicationDate, that.applicationDate) &&
            Objects.equals(agentName, that.agentName) &&
            Objects.equals(agentAddress, that.agentAddress) &&
            Objects.equals(proprietorName, that.proprietorName) &&
            Objects.equals(proprietorAddress, that.proprietorAddress) &&
            Objects.equals(headOffice, that.headOffice) &&
            Objects.equals(imgUrl, that.imgUrl) &&
            Objects.equals(tmClass, that.tmClass) &&
            Objects.equals(journalNo, that.journalNo) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(usage, that.usage) &&
            Objects.equals(associatedTms, that.associatedTms) &&
            Objects.equals(trademarkStatus, that.trademarkStatus) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(renewalDate, that.renewalDate) &&
            Objects.equals(type, that.type) &&
            Objects.equals(pageNo, that.pageNo) &&
            Objects.equals(source, that.source) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(organizationType, that.organizationType) &&
            Objects.equals(normalizedName, that.normalizedName) &&
            Objects.equals(leadId, that.leadId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(trademarkPlanId, that.trademarkPlanId) &&
            Objects.equals(tmAgentId, that.tmAgentId) &&
            Objects.equals(trademarkClassesId, that.trademarkClassesId) &&
            Objects.equals(documentsId, that.documentsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            details,
            applicationNo,
            applicationDate,
            agentName,
            agentAddress,
            proprietorName,
            proprietorAddress,
            headOffice,
            imgUrl,
            tmClass,
            journalNo,
            deleted,
            usage,
            associatedTms,
            trademarkStatus,
            createdDate,
            modifiedDate,
            renewalDate,
            type,
            pageNo,
            source,
            phoneNumber,
            email,
            organizationType,
            normalizedName,
            leadId,
            userId,
            trademarkPlanId,
            tmAgentId,
            trademarkClassesId,
            documentsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrademarkCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDetails().map(f -> "details=" + f + ", ").orElse("") +
            optionalApplicationNo().map(f -> "applicationNo=" + f + ", ").orElse("") +
            optionalApplicationDate().map(f -> "applicationDate=" + f + ", ").orElse("") +
            optionalAgentName().map(f -> "agentName=" + f + ", ").orElse("") +
            optionalAgentAddress().map(f -> "agentAddress=" + f + ", ").orElse("") +
            optionalProprietorName().map(f -> "proprietorName=" + f + ", ").orElse("") +
            optionalProprietorAddress().map(f -> "proprietorAddress=" + f + ", ").orElse("") +
            optionalHeadOffice().map(f -> "headOffice=" + f + ", ").orElse("") +
            optionalImgUrl().map(f -> "imgUrl=" + f + ", ").orElse("") +
            optionalTmClass().map(f -> "tmClass=" + f + ", ").orElse("") +
            optionalJournalNo().map(f -> "journalNo=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalUsage().map(f -> "usage=" + f + ", ").orElse("") +
            optionalAssociatedTms().map(f -> "associatedTms=" + f + ", ").orElse("") +
            optionalTrademarkStatus().map(f -> "trademarkStatus=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalModifiedDate().map(f -> "modifiedDate=" + f + ", ").orElse("") +
            optionalRenewalDate().map(f -> "renewalDate=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalPageNo().map(f -> "pageNo=" + f + ", ").orElse("") +
            optionalSource().map(f -> "source=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalOrganizationType().map(f -> "organizationType=" + f + ", ").orElse("") +
            optionalNormalizedName().map(f -> "normalizedName=" + f + ", ").orElse("") +
            optionalLeadId().map(f -> "leadId=" + f + ", ").orElse("") +
            optionalUserId().map(f -> "userId=" + f + ", ").orElse("") +
            optionalTrademarkPlanId().map(f -> "trademarkPlanId=" + f + ", ").orElse("") +
            optionalTmAgentId().map(f -> "tmAgentId=" + f + ", ").orElse("") +
            optionalTrademarkClassesId().map(f -> "trademarkClassesId=" + f + ", ").orElse("") +
            optionalDocumentsId().map(f -> "documentsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
