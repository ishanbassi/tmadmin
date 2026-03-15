package com.bassi.tmapp.service;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.config.Constants;
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.ContactDto;
import com.bassi.tmapp.service.dto.UserDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import tech.jhipster.config.JHipsterProperties;

/**
 * Service for sending emails asynchronously.
 * <p>
 * We use the {@link Async} annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String BASE_URL = "baseUrl";

    private static final String LEAD = "lead";

    private static final String USER_PROFILE = "userProfile";

    private static final String PAYMENT = "payment";

    private static final String CONTACT = "contact";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    private final ApplicationProperties applicationProperties;

    public MailService(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine,
        ApplicationProperties applicationProperties
    ) {
        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
        this.applicationProperties = applicationProperties;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        sendEmailSync(to, subject, content, isMultipart, isHtml);
    }

    private void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        LOG.debug(
            "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart,
            isHtml,
            to,
            subject,
            content
        );

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            LOG.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            LOG.warn("Email could not be sent to user '{}'", to, e);
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        sendEmailFromTemplateSync(user, templateName, titleKey);
    }

    private void sendEmailFromTemplateSync(User user, String templateName, String titleKey) {
        if (user.getEmail() == null) {
            LOG.debug("Email doesn't exist for user '{}'", user.getLogin());
            return;
        }
        String langKey = Constants.DEFAULT_LANGUAGE;
        Locale locale = Locale.forLanguageTag(langKey);
        Context context = new Context(locale);
        LocalDate date = LocalDate.now();
        context.setVariable(USER, user);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable("date", date);
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);

        sendEmailSync(user.getEmail(), subject, content, false, true);
    }

    public void sendNewLeadMailToAdmin(Lead lead, String templateName, String titleKey, List<String> leadAdminEmails) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(LEAD, lead);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(templateName, context);
        context.setVariable("name", lead.getFullName());
        context.setVariable("email", lead.getEmail());
        context.setVariable("phone", lead.getPhoneNumber());
        context.setVariable("city", lead.getCity());
        context.setVariable("id", lead.getId());
        context.setVariable("purpose", lead.getComments());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        for (String adminEmail : leadAdminEmails) {
            sendEmailSync(adminEmail, subject, content, false, true);
        }
    }

    public void sendNewAccountCreationMailToAdmin(
        UserProfileDTO userProfile,
        String templateName,
        String titleKey,
        List<String> adminEmails
    ) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(USER_PROFILE, userProfile);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(templateName, context);
        context.setVariable("name", userProfile.getFullName());
        context.setVariable("email", userProfile.getEmail());
        context.setVariable("phone", userProfile.getPhoneNumber());
        context.setVariable("id", userProfile.getId());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        for (String adminEmail : adminEmails) {
            sendEmailSync(adminEmail, subject, content, false, true);
        }
    }

    public void sendPaymentConfirmationEmailToAdmin(Payment payment, String templateName, String titleKey, List<String> adminEmails) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(PAYMENT, payment);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        context.setVariable(templateName, context);
        context.setVariable("userId", payment.getUserProfile().getId());
        context.setVariable("status", payment.getStatus());
        context.setVariable("gatewayOrderId", payment.getGatewayOrderId());
        context.setVariable("gatewayPaymentId", payment.getGatewayPaymentId());
        context.setVariable("id", payment.getId());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        for (String adminEmail : adminEmails) {
            sendEmailSync(adminEmail, subject, content, false, true);
        }
    }

    public void sendOfferMailToPharamas(Lead lead, String templateName, String titleKey, String leadEmail) {
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(LEAD, lead);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmailSync(leadEmail, subject, content, false, true);
    }

    @Async
    public void sendActivationEmail(User user) {
        LOG.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        LOG.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        LOG.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendPortalMemberCreationEmail(User user) {
        LOG.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/portalCreationEmail", "email.portal.activation.title");
    }

    @Async
    public void sendPortalPasswordResetMail(User user) {
        LOG.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplateSync(user, "mail/portalPasswordResetEmail", "email.portal.reset.title");
    }

    @Async
    public void sendPortalMemberCreationEmailToAdmin(UserProfileDTO user) {
        LOG.info("Sending creation email to '{}'", user.getEmail());
        sendNewAccountCreationMailToAdmin(
            user,
            "mail/portalCreationEmailToAdmin",
            "email.portal.account.title",
            applicationProperties.getAdminNotificationsEmailAddress()
        );
    }

    @Async
    public void sendPaymentSuccessfulEmailToAdmin(Payment payment) {
        LOG.info("Sending Payment Successful email to admin");
        sendPaymentConfirmationEmailToAdmin(
            payment,
            "mail/paymentSuccessfulEmailToAdmin",
            "email.payment.success.title",
            applicationProperties.getAdminNotificationsEmailAddress()
        );
    }

    @Async
    public void sendContactUsMail(@Valid ContactDto contactDto, String toEmailAddress) {
        LOG.info("Creating contact us request'{}'", contactDto.toString());
        sendContactEmailFromTemplate(contactDto, toEmailAddress, "mail/userContactRequestEmail", "email.contact.title");
    }

    public void sendContactEmailFromTemplate(ContactDto contact, String toEmailAddress, String templateName, String titleKey) {
        if (toEmailAddress == null) {
            LOG.debug("Email doesn't exist for contact us '{}'", toEmailAddress);
            return;
        }
        Locale locale = Locale.forLanguageTag("en");
        Context context = new Context(locale);
        context.setVariable(CONTACT, contact);
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmailSync(toEmailAddress, subject, content, false, true);
    }
}
