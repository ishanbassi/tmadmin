package com.bassi.tmapp.service.criteria;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bassi.tmapp.domain.PublishedTm} entity. This class is used
 * in {@link com.bassi.tmapp.web.rest.PublishedTmResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /published-tms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PublishedTmCriteria implements Serializable, Criteria {

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

    private Boolean distinct;

    public PublishedTmCriteria() {}

    public PublishedTmCriteria(PublishedTmCriteria other) {
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
        this.distinct = other.distinct;
    }

    @Override
    public PublishedTmCriteria copy() {
        return new PublishedTmCriteria(this);
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
        final PublishedTmCriteria that = (PublishedTmCriteria) o;
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
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PublishedTmCriteria{" +
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
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
