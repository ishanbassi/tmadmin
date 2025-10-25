package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.repository.UserRepository;
import com.bassi.tmapp.security.SecurityUtils;
import com.bassi.tmapp.service.CurrentUserService;
import com.bassi.tmapp.service.MailService;
import com.bassi.tmapp.service.UserProfileService;
import com.bassi.tmapp.service.UserService;
import com.bassi.tmapp.service.dto.AdminUserDTO;
import com.bassi.tmapp.service.dto.CaptchaResponse;
import com.bassi.tmapp.service.dto.PasswordChangeDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import com.bassi.tmapp.service.mapper.UserProfileMapper;
import com.bassi.tmapp.web.rest.errors.*;
import com.bassi.tmapp.web.rest.vm.KeyAndPasswordVM;
import com.bassi.tmapp.web.rest.vm.ManagedUserVM;
import com.bassi.tmapp.web.rest.vm.extended.ManagedUserVMExtended;
import jakarta.validation.Valid;
import java.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final UserProfileService userProfileService;

    private final ApplicationProperties applicationProperties;

    private final RestTemplate restTemplate;

    private final UserProfileMapper userProfileMapper;

    private final CurrentUserService currentUserService;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        UserProfileService userProfileService,
        ApplicationProperties applicationProperties,
        RestTemplate restTemplate,
        UserProfileMapper userProfileMapper,
        CurrentUserService currentUserService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.userProfileService = userProfileService;
        this.applicationProperties = applicationProperties;
        this.restTemplate = restTemplate;
        this.userProfileMapper = userProfileMapper;
        this.currentUserService = currentUserService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AdminUserDTO getAccount() {
        return userService
            .getUserWithAuthorities()
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        String userLogin = SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.orElseThrow().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        userService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getLangKey(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userService.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            LOG.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }

    @PostMapping("/portal/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserProfileDTO> registerAccount(@Valid @RequestBody ManagedUserVMExtended managedUserVMExtended) {
        if (isGoogleCaptchaInvalid(managedUserVMExtended.getCaptchaResponse())) {
            throw new InternalServerAlertException(
                "reCAPTCHA verification failed. Our server could not validate your response. Please try again, and ensure you complete the reCAPTCHA challenge accurately."
            );
        }

        if (isPasswordLengthInvalid(managedUserVMExtended.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerPortalUser(managedUserVMExtended, managedUserVMExtended.getPassword());
        return ResponseEntity.ok(userProfileService.createUserProfile(user, managedUserVMExtended));
    }

    private boolean isGoogleCaptchaInvalid(String captcha) {
        String params = "?secret=" + applicationProperties.getGoogleCaptcha().getSecretKey() + "&response=" + captcha;
        String completeUrl = applicationProperties.getGoogleCaptcha().getServerVerifyUrl() + params;
        CaptchaResponse resp = restTemplate.postForObject(completeUrl, null, CaptchaResponse.class);
        return resp != null && !resp.isSuccess();
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserProfileDTO> getCurrentUser() {
        UserProfile userProfile = currentUserService.getCurrentUserProfile();
        UserProfileDTO userProfileDTO = userProfileMapper.toDto(userProfile);
        return ResponseEntity.ok(userProfileDTO);
    }
}
