package com.bassi.tmapp.service;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.config.ImapProperties.ImapAccount;
import com.bassi.tmapp.service.webScraping.TrademarkScrapingService;
import com.bassi.tmapp.web.rest.errors.OtpNotReceivedException;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailOtpService {

    private static final Logger log = LoggerFactory.getLogger(EmailOtpService.class);

    @Value("${otp.wait.timeout.seconds}")
    private int timeoutSeconds;

    @Value("${otp.wait.poll.interval.seconds}")
    private int pollIntervalSeconds;

    @Value("${otp.sender.email}")
    private String otpSenderEmail;

    private static final Pattern OTP_PATTERN = Pattern.compile("\\b(\\d{4,6})\\b");

    EmailOtpService() {}

    /**
     * Waits for OTP email and returns the OTP string.
     * Polls inbox every N seconds until timeout.
     */
    public String waitForOtp(ImapAccount account) throws Exception {
        log.info("Waiting for OTP email on: {}", account.getUsername());

        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;

        // Record time before OTP was requested — ignore older emails
        Date requestedAt = new Date(startTime - 200000); // 2m buffer

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            String otp = fetchOtpFromInbox(account, requestedAt);
            if (otp != null) {
                log.info("OTP received successfully");
                return otp;
            }
            log.debug("OTP not found yet, retrying in {}s...", pollIntervalSeconds);
            Thread.sleep(pollIntervalSeconds * 1000L);
        }

        throw new OtpNotReceivedException("OTP not received within " + timeoutSeconds + " seconds");
    }

    private String fetchOtpFromInbox(ImapAccount account, Date after) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", account.getHost());
        props.put("mail.imaps.port", String.valueOf(account.getPort()));
        props.put("mail.imaps.ssl.enable", "true");

        Session session = Session.getInstance(props);
        try (Store store = session.getStore("imaps")) {
            store.connect(account.getHost(), account.getUsername(), account.getPassword());

            try (Folder inbox = store.getFolder(account.getFolder())) {
                inbox.open(Folder.READ_ONLY);

                // Search for unread emails from IP India after the request time
                SearchTerm senderTerm = new FromStringTerm(otpSenderEmail);
                Message[] messages = inbox.search(senderTerm);

                log.debug("Found {} matching emails", messages.length);

                // Sort newest first
                Arrays.sort(messages, (a, b) -> {
                    try {
                        return b.getReceivedDate().compareTo(a.getReceivedDate());
                    } catch (MessagingException e) {
                        return 0;
                    }
                });

                // Filter by time client-side — much more reliable
                for (Message message : messages) {
                    Date received = message.getReceivedDate();
                    log.debug("Email received at: {}", received);

                    if (received != null && received.after(after)) {
                        String body = getEmailBody(message);
                        log.debug("Email body: {}", body); // temporary — remove after debugging
                        String otp = extractOtp(body);
                        if (otp != null) return otp;
                    }
                }
            }
        }
        return null;
    }

    private String extractOtp(String emailBody) {
        if (emailBody == null) return null;
        Matcher matcher = OTP_PATTERN.matcher(emailBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String getEmailBody(Message message) throws Exception {
        Object content = message.getContent();

        if (content instanceof String) {
            return (String) content;
        }

        if (content instanceof Multipart multipart) {
            return extractFromMultipart(multipart);
        }

        return null;
    }

    private String extractFromMultipart(Multipart multipart) throws Exception {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            String contentType = part.getContentType().toLowerCase();

            if (contentType.contains("text/plain")) {
                text.append(part.getContent());
            } else if (contentType.contains("text/html")) {
                // Strip HTML tags as fallback
                String html = (String) part.getContent();
                text.append(html.replaceAll("<[^>]+>", " "));
            } else if (part.getContent() instanceof Multipart nested) {
                text.append(extractFromMultipart(nested));
            }
        }
        return text.toString();
    }
}
