package com.bassi.tmapp.service;

import com.bassi.tmapp.service.webScraping.TrademarkScrapingService;
import jakarta.mail.BodyPart;
import jakarta.mail.Flags;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.search.AndTerm;
import jakarta.mail.search.ComparisonTerm;
import jakarta.mail.search.FlagTerm;
import jakarta.mail.search.FromStringTerm;
import jakarta.mail.search.ReceivedDateTerm;
import jakarta.mail.search.SearchTerm;
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
@Slf4j
public class EmailOtpService {

    private static final Logger log = LoggerFactory.getLogger(EmailOtpService.class);

    @Value("${imap.host}")
    private String host;

    @Value("${imap.port}")
    private int port;

    @Value("${imap.username}")
    private String username;

    @Value("${imap.password}")
    private String password;

    @Value("${imap.folder}")
    private String folder;

    @Value("${otp.wait.timeout.seconds}")
    private int timeoutSeconds;

    @Value("${otp.wait.poll.interval.seconds}")
    private int pollIntervalSeconds;

    @Value("${otp.sender.email}")
    private String otpSenderEmail;

    private static final Pattern OTP_PATTERN = Pattern.compile("\\b(\\d{4,6})\\b");

    /**
     * Waits for OTP email and returns the OTP string.
     * Polls inbox every N seconds until timeout.
     */
    public String waitForOtp() throws Exception {
        log.info("Waiting for OTP email from: {}", otpSenderEmail);

        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;

        // Record time before OTP was requested — ignore older emails
        Date requestedAt = new Date(startTime - 5000); // 5s buffer

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            String otp = fetchOtpFromInbox(requestedAt);
            if (otp != null) {
                log.info("OTP received successfully");
                return otp;
            }
            log.debug("OTP not found yet, retrying in {}s...", pollIntervalSeconds);
            Thread.sleep(pollIntervalSeconds * 1000L);
        }

        throw new RuntimeException("OTP not received within " + timeoutSeconds + " seconds");
    }

    private String fetchOtpFromInbox(Date after) throws Exception {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imaps.host", host);
        props.put("mail.imaps.port", String.valueOf(port));
        props.put("mail.imaps.ssl.enable", "true");

        Session session = Session.getInstance(props);
        try (Store store = session.getStore("imaps")) {
            store.connect(host, username, password);

            try (Folder inbox = store.getFolder(this.folder)) {
                inbox.open(Folder.READ_ONLY);

                // Search for unread emails from IP India after the request time
                SearchTerm senderTerm = new FromStringTerm(otpSenderEmail);
                SearchTerm dateTerm = new ReceivedDateTerm(ComparisonTerm.GT, after);
                SearchTerm unseenTerm = new FlagTerm(new Flags(Flags.Flag.SEEN), false);
                SearchTerm combined = new AndTerm(new SearchTerm[] { senderTerm, dateTerm, unseenTerm });

                Message[] messages = inbox.search(combined);
                log.debug("Found {} matching emails", messages.length);

                for (Message message : messages) {
                    String body = getEmailBody(message);
                    String otp = extractOtp(body);
                    if (otp != null) {
                        return otp;
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
