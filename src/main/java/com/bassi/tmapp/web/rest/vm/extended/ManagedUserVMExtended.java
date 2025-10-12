package com.bassi.tmapp.web.rest.vm.extended;

import com.bassi.tmapp.service.extended.dto.ApplicationUserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ManagedUserVMExtended extends ApplicationUserDto {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    @NotBlank
    private String captchaResponse;

    public ManagedUserVMExtended() {
        // Empty constructor needed for Jackson.
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptchaResponse() {
        return captchaResponse;
    }

    public void setCaptchaResponse(String captchaResponse) {
        this.captchaResponse = captchaResponse;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
