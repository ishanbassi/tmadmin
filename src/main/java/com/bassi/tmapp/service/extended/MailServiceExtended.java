package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.User;
import com.bassi.tmapp.service.MailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Primary;
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
@Primary
public class MailServiceExtended extends MailService {

    private static final Logger LOG = LoggerFactory.getLogger(MailServiceExtended.class);
    private final ApplicationProperties applicationProperties;

    public MailServiceExtended(
        JHipsterProperties jHipsterProperties,
        JavaMailSender javaMailSender,
        MessageSource messageSource,
        SpringTemplateEngine templateEngine,
        ApplicationProperties applicationProperties
    ) {
        super(jHipsterProperties, javaMailSender, messageSource, templateEngine);
        this.applicationProperties = applicationProperties;
    }

    @Async
    public void sendNewLeadMailToAdmin(Lead lead) {
        LOG.debug("Sending new lead mail to '{}'", applicationProperties.getAdminNotificationsEmailAddress());
        sendNewLeadMailToAdmin(lead, "mail/newLeadEmail", "email.lead.title");
    }
}
