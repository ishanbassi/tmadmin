package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.repository.UserProfileRepository;
import com.bassi.tmapp.security.SecurityUtils;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final UserProfileService userProfileService;

    CurrentUserService(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    public UserProfile getCurrentUserProfile() {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new InternalServerAlertException("User could not be found"));
        return userProfileService
            .findByUserEmail(userLogin)
            .orElseThrow(() -> new InternalServerAlertException("User profile does not exists for the user login"));
    }
}
