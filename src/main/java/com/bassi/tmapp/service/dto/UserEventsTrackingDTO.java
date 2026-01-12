package com.bassi.tmapp.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.UserEventsTracking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserEventsTrackingDTO implements Serializable {

    private Long id;

    private String eventType;

    private String pageName;

    private String deviceType;

    private ZonedDateTime createdDate;

    private Boolean deleted;

    private ZonedDateTime modifiedDate;

    private UserProfileDTO userProfile;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public UserProfileDTO getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfileDTO userProfile) {
        this.userProfile = userProfile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserEventsTrackingDTO)) {
            return false;
        }

        UserEventsTrackingDTO userEventsTrackingDTO = (UserEventsTrackingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userEventsTrackingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserEventsTrackingDTO{" +
            "id=" + getId() +
            ", eventType='" + getEventType() + "'" +
            ", pageName='" + getPageName() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", deleted='" + getDeleted() + "'" +
            ", modifiedDate='" + getModifiedDate() + "'" +
            ", userProfile=" + getUserProfile() +
            "}";
    }
}
