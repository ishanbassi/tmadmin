package com.bassi.tmapp.service.criteria;

import com.bassi.tmapp.domain.enumeration.ContactMethod;
import com.bassi.tmapp.domain.enumeration.LeadStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bassi.tmapp.domain.Lead} entity. This class is used
 * in {@link com.bassi.tmapp.web.rest.LeadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ContactMethod
     */
    public static class ContactMethodFilter extends Filter<ContactMethod> {

        public ContactMethodFilter() {}

        public ContactMethodFilter(ContactMethodFilter filter) {
            super(filter);
        }

        @Override
        public ContactMethodFilter copy() {
            return new ContactMethodFilter(this);
        }
    }

    /**
     * Class for filtering LeadStatus
     */
    public static class LeadStatusFilter extends Filter<LeadStatus> {

        public LeadStatusFilter() {}

        public LeadStatusFilter(LeadStatusFilter filter) {
            super(filter);
        }

        @Override
        public LeadStatusFilter copy() {
            return new LeadStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter phoneNumber;

    private StringFilter email;

    private StringFilter city;

    private StringFilter brandName;

    private StringFilter selectedPackage;

    private IntegerFilter tmClass;

    private StringFilter comments;

    private ContactMethodFilter contactMethod;

    private ZonedDateTimeFilter createdDate;

    private ZonedDateTimeFilter modifiedDate;

    private BooleanFilter deleted;

    private LeadStatusFilter status;

    private StringFilter leadSource;

    private LongFilter assignedToId;

    private Boolean distinct;

    public LeadCriteria() {}

    public LeadCriteria(LeadCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.fullName = other.optionalFullName().map(StringFilter::copy).orElse(null);
        this.phoneNumber = other.optionalPhoneNumber().map(StringFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.city = other.optionalCity().map(StringFilter::copy).orElse(null);
        this.brandName = other.optionalBrandName().map(StringFilter::copy).orElse(null);
        this.selectedPackage = other.optionalSelectedPackage().map(StringFilter::copy).orElse(null);
        this.tmClass = other.optionalTmClass().map(IntegerFilter::copy).orElse(null);
        this.comments = other.optionalComments().map(StringFilter::copy).orElse(null);
        this.contactMethod = other.optionalContactMethod().map(ContactMethodFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.modifiedDate = other.optionalModifiedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(LeadStatusFilter::copy).orElse(null);
        this.leadSource = other.optionalLeadSource().map(StringFilter::copy).orElse(null);
        this.assignedToId = other.optionalAssignedToId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public LeadCriteria copy() {
        return new LeadCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public Optional<StringFilter> optionalFullName() {
        return Optional.ofNullable(fullName);
    }

    public StringFilter fullName() {
        if (fullName == null) {
            setFullName(new StringFilter());
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
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

    public StringFilter getCity() {
        return city;
    }

    public Optional<StringFilter> optionalCity() {
        return Optional.ofNullable(city);
    }

    public StringFilter city() {
        if (city == null) {
            setCity(new StringFilter());
        }
        return city;
    }

    public void setCity(StringFilter city) {
        this.city = city;
    }

    public StringFilter getBrandName() {
        return brandName;
    }

    public Optional<StringFilter> optionalBrandName() {
        return Optional.ofNullable(brandName);
    }

    public StringFilter brandName() {
        if (brandName == null) {
            setBrandName(new StringFilter());
        }
        return brandName;
    }

    public void setBrandName(StringFilter brandName) {
        this.brandName = brandName;
    }

    public StringFilter getSelectedPackage() {
        return selectedPackage;
    }

    public Optional<StringFilter> optionalSelectedPackage() {
        return Optional.ofNullable(selectedPackage);
    }

    public StringFilter selectedPackage() {
        if (selectedPackage == null) {
            setSelectedPackage(new StringFilter());
        }
        return selectedPackage;
    }

    public void setSelectedPackage(StringFilter selectedPackage) {
        this.selectedPackage = selectedPackage;
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

    public StringFilter getComments() {
        return comments;
    }

    public Optional<StringFilter> optionalComments() {
        return Optional.ofNullable(comments);
    }

    public StringFilter comments() {
        if (comments == null) {
            setComments(new StringFilter());
        }
        return comments;
    }

    public void setComments(StringFilter comments) {
        this.comments = comments;
    }

    public ContactMethodFilter getContactMethod() {
        return contactMethod;
    }

    public Optional<ContactMethodFilter> optionalContactMethod() {
        return Optional.ofNullable(contactMethod);
    }

    public ContactMethodFilter contactMethod() {
        if (contactMethod == null) {
            setContactMethod(new ContactMethodFilter());
        }
        return contactMethod;
    }

    public void setContactMethod(ContactMethodFilter contactMethod) {
        this.contactMethod = contactMethod;
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

    public LeadStatusFilter getStatus() {
        return status;
    }

    public Optional<LeadStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public LeadStatusFilter status() {
        if (status == null) {
            setStatus(new LeadStatusFilter());
        }
        return status;
    }

    public void setStatus(LeadStatusFilter status) {
        this.status = status;
    }

    public StringFilter getLeadSource() {
        return leadSource;
    }

    public Optional<StringFilter> optionalLeadSource() {
        return Optional.ofNullable(leadSource);
    }

    public StringFilter leadSource() {
        if (leadSource == null) {
            setLeadSource(new StringFilter());
        }
        return leadSource;
    }

    public void setLeadSource(StringFilter leadSource) {
        this.leadSource = leadSource;
    }

    public LongFilter getAssignedToId() {
        return assignedToId;
    }

    public Optional<LongFilter> optionalAssignedToId() {
        return Optional.ofNullable(assignedToId);
    }

    public LongFilter assignedToId() {
        if (assignedToId == null) {
            setAssignedToId(new LongFilter());
        }
        return assignedToId;
    }

    public void setAssignedToId(LongFilter assignedToId) {
        this.assignedToId = assignedToId;
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
        final LeadCriteria that = (LeadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(email, that.email) &&
            Objects.equals(city, that.city) &&
            Objects.equals(brandName, that.brandName) &&
            Objects.equals(selectedPackage, that.selectedPackage) &&
            Objects.equals(tmClass, that.tmClass) &&
            Objects.equals(comments, that.comments) &&
            Objects.equals(contactMethod, that.contactMethod) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(status, that.status) &&
            Objects.equals(leadSource, that.leadSource) &&
            Objects.equals(assignedToId, that.assignedToId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            fullName,
            phoneNumber,
            email,
            city,
            brandName,
            selectedPackage,
            tmClass,
            comments,
            contactMethod,
            createdDate,
            modifiedDate,
            deleted,
            status,
            leadSource,
            assignedToId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeadCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalFullName().map(f -> "fullName=" + f + ", ").orElse("") +
            optionalPhoneNumber().map(f -> "phoneNumber=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalCity().map(f -> "city=" + f + ", ").orElse("") +
            optionalBrandName().map(f -> "brandName=" + f + ", ").orElse("") +
            optionalSelectedPackage().map(f -> "selectedPackage=" + f + ", ").orElse("") +
            optionalTmClass().map(f -> "tmClass=" + f + ", ").orElse("") +
            optionalComments().map(f -> "comments=" + f + ", ").orElse("") +
            optionalContactMethod().map(f -> "contactMethod=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalModifiedDate().map(f -> "modifiedDate=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalLeadSource().map(f -> "leadSource=" + f + ", ").orElse("") +
            optionalAssignedToId().map(f -> "assignedToId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
