package com.bassi.tmapp.service.dto;

import com.bassi.tmapp.web.rest.AuthenticateController.JWTToken;

public class PaymentConfirmationDto {

    private String status;
    private JWTToken token;
    private UserProfileDTO userProfileDTO;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public JWTToken getToken() {
        return token;
    }

    public void setToken(JWTToken token) {
        this.token = token;
    }

    public UserProfileDTO getUserProfileDTO() {
        return userProfileDTO;
    }

    public void setUserProfileDTO(UserProfileDTO userProfileDTO) {
        this.userProfileDTO = userProfileDTO;
    }
}
