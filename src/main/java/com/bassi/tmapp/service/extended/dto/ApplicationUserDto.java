package com.bassi.tmapp.service.extended.dto;

import com.bassi.tmapp.service.dto.AdminUserDTO;
import java.io.Serializable;

/**
 * A DTO for the {@link com.bassi.tmapp.domain.TmAgent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUserDto extends AdminUserDTO implements Serializable {

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
