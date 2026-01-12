package com.bassi.tmapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserEventsTracking.
 */
@Entity
@Table(name = "user_events_tracking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserEventsTracking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "event_type")
    private String eventType;

    @Column(name = "page_name")
    private String pageName;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "created_date")
    private ZonedDateTime createdDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "modified_date")
    private ZonedDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "documents" }, allowSetters = true)
    private UserProfile userProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserEventsTracking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return this.eventType;
    }

    public UserEventsTracking eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPageName() {
        return this.pageName;
    }

    public UserEventsTracking pageName(String pageName) {
        this.setPageName(pageName);
        return this;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public UserEventsTracking deviceType(String deviceType) {
        this.setDeviceType(deviceType);
        return this;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public ZonedDateTime getCreatedDate() {
        return this.createdDate;
    }

    public UserEventsTracking createdDate(ZonedDateTime createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public UserEventsTracking deleted(Boolean deleted) {
        this.setDeleted(deleted);
        return this;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getModifiedDate() {
        return this.modifiedDate;
    }

    public UserEventsTracking modifiedDate(ZonedDateTime modifiedDate) {
        this.setModifiedDate(modifiedDate);
        return this;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserProfile getUserProfile() {
        return this.userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public UserEventsTracking userProfile(UserProfile userProfile) {
        this.setUserProfile(userProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEventsTracking)) {
            return false;
        }
        return getId() != null && getId().equals(((UserEventsTracking) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserEventsTracking{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", pageName='" + getPageName() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            "}";
    }
}
