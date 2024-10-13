package com.bassi.tmapp.web.rest.extended;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.repository.UserRepository;
import com.bassi.tmapp.security.SecurityUtils;
import com.bassi.tmapp.service.MailService;
import com.bassi.tmapp.service.UserService;
import com.bassi.tmapp.service.dto.AdminUserDTO;
import com.bassi.tmapp.service.dto.PasswordChangeDTO;
import com.bassi.tmapp.service.extended.AccountServiceExtended;
import com.bassi.tmapp.service.extended.UserServiceExtended;
import com.bassi.tmapp.service.extended.dto.AccountDto;
import com.bassi.tmapp.service.extended.dto.ApplicationUserDto;
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
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/extended")
public class AccountResourceExtended {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger log = LoggerFactory.getLogger(AccountResourceExtended.class);

    private final UserRepository userRepository;

    private final UserServiceExtended userServiceExtended;

    private final MailService mailService;
    
    private AccountServiceExtended accountServiceExtended;

	public AccountResourceExtended(UserRepository userRepository, UserServiceExtended userServiceExtended,
			MailService mailService, AccountServiceExtended accountServiceExtended) {
        this.userRepository = userRepository;
        this.userServiceExtended = userServiceExtended;
        this.mailService = mailService;
        this.accountServiceExtended =accountServiceExtended;
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
    public ResponseEntity<TmAgent> registerAccount(@Valid @RequestBody ManagedUserVMExtended  managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        TmAgent agent = userServiceExtended.registerUser(managedUserVM, managedUserVM.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(agent);
    }

   
    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public AccountDto getAccount() {
    	AccountDto account = accountServiceExtended.getAgent();
		AdminUserDTO user=  userServiceExtended
	    .getUserWithAuthorities()
	    .map(AdminUserDTO::new)
	    .orElseThrow(() -> new AccountResourceException("User could not be found"));
		account.setUser(user);
		return account;
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
        userServiceExtended.updateUser(
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
        userServiceExtended.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<User> user = userServiceExtended.requestPasswordReset(mail);
        if (user.isPresent()) {
            mailService.sendPasswordResetMail(user.orElseThrow());
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
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
        Optional<User> user = userServiceExtended.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

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
}
