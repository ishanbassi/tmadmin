package com.bassi.tmapp.domain;

import com.bassi.tmapp.domain.enumeration.ContactMethod;
import com.bassi.tmapp.domain.enumeration.LeadStatus;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Lead.
 */
@Entity
@Table(name = "lead")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lead implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "city")
    private String city;

    @Column(name = "brand_name")
    private String brandName;

    @Column(name = "selected_package")
    private String selectedPackage;

    @Column(name = "tm_class")
    private Integer tmClass;

    @Column(name = "comments")
    private String comments;

    @Enumerated(EnumType.STRING)
    @Column(name = "contact_method")
    private ContactMethod contactMethod;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeadStatus status;

    @Column(name = "lead_source")
    private String leadSource;

    @ManyToOne(fetch = FetchType.EAGER)
    private Employee assignedTo;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lead id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Lead fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Lead phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public Lead email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return this.city;
    }

    public Lead city(String city) {
        this.setCity(city);
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBrandName() {
        return this.brandName;
    }

    public Lead brandName(String brandName) {
        this.setBrandName(brandName);
        return this;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSelectedPackage() {
        return this.selectedPackage;
    }

    public Lead selectedPackage(String selectedPackage) {
        this.setSelectedPackage(selectedPackage);
        return this;
    }

    public void setSelectedPackage(String selectedPackage) {
        this.selectedPackage = selectedPackage;
    }

    public Integer getTmClass() {
        return this.tmClass;
    }

    public Lead tmClass(Integer tmClass) {
        this.setTmClass(tmClass);
        return this;
    }

    public void setTmClass(Integer tmClass) {
        this.tmClass = tmClass;
    }

    public String getComments() {
        return this.comments;
    }

    public Lead comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ContactMethod getContactMethod() {
        return this.contactMethod;
    }

    public Lead contactMethod(ContactMethod contactMethod) {
        this.setContactMethod(contactMethod);
        return this;
    }

    public void setContactMethod(ContactMethod contactMethod) {
        this.contactMethod = contactMethod;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public Lead createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public Lead modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Lead deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public LeadStatus getStatus() {
        return this.status;
    }

    public Lead status(LeadStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(LeadStatus status) {
        this.status = status;
    }

    public String getLeadSource() {
        return this.leadSource;
    }

    public Lead leadSource(String leadSource) {
        this.setLeadSource(leadSource);
        return this;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public Employee getAssignedTo() {
        return this.assignedTo;
    }

    public void setAssignedTo(Employee employee) {
        this.assignedTo = employee;
    }

    public Lead assignedTo(Employee employee) {
        this.setAssignedTo(employee);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lead)) {
            return false;
        }
        return getId() != null && getId().equals(((Lead) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lead{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", email='" + getEmail() + "'" +
            ", city='" + getCity() + "'" +
            ", brandName='" + getBrandName() + "'" +
            ", selectedPackage='" + getSelectedPackage() + "'" +
            ", tmClass=" + getTmClass() +
            ", comments='" + getComments() + "'" +
            ", contactMethod='" + getContactMethod() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", status='" + getStatus() + "'" +
            ", leadSource='" + getLeadSource() + "'" +
            "}";
    }

    @PrePersist
    private void beforeSave() {
        this.createdDate = ZonedDateTime.now();
        this.modifiedDate = ZonedDateTime.now();
        this.deleted = false;
        this.status = LeadStatus.NEW;
    }

    @PreUpdate
    private void beforeUpdate() {
        this.modifiedDate = ZonedDateTime.now();
    }
}
