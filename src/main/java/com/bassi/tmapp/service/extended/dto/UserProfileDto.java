package com.bassi.tmapp.service.extended.dto;

import com.bassi.tmapp.domain.User;
import java.time.ZonedDateTime;

public class UserProfileDto {

    private Long id;

    private ZonedDateTime createdDate;

    private ZonedDateTime modifiedDate;

    private Boolean deleted;

    private User user;

    @Override
    public String toString() {
        return (
            "UserProfileDto [id=" +
            id +
            ", createdDate=" +
            createdDate +
            ", modifiedDate=" +
            modifiedDate +
            ", deleted=" +
            deleted +
            ", user=" +
            user +
            "]"
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
