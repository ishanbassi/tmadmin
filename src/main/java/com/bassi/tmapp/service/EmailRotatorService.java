package com.bassi.tmapp.service;

import com.bassi.tmapp.config.ApplicationProperties;
import com.bassi.tmapp.config.ImapProperties;
import com.bassi.tmapp.config.ImapProperties.ImapAccount;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailRotatorService {

    @Autowired
    private ImapProperties imapProperties;

    // Tracks which email to use next
    private final AtomicInteger currentIndex = new AtomicInteger(0);
    private static final Logger log = LoggerFactory.getLogger(EmailRotatorService.class);

    /**
     * Returns next email in rotation.
     * Cycles back to first after last.
     */
    public ImapAccount getNextAccount() {
        List<ImapAccount> accounts = imapProperties.getAccounts();
        int index = currentIndex.getAndUpdate(i -> (i + 1) % accounts.size());
        ImapAccount account = accounts.get(index);
        log.info("Using account : {} (index {})", account.isEmail() ? account.getUsername() : account.getPhoneNumber(), index);

        return account;
    }

    public int getTotalAccounts() {
        return imapProperties.getAccounts().size();
    }
}
