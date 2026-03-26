package com.bassi.tmapp.service;

import com.bassi.tmapp.service.extended.PublishedTmServiceExtended;
import com.bassi.tmapp.service.webScraping.TrademarkScrapingService;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tech.jhipster.config.JHipsterConstants;

@Service
@Profile({ JHipsterConstants.SPRING_PROFILE_PRODUCTION })
public class TrademarkScheduler {

    private static final Logger log = LoggerFactory.getLogger(TrademarkScheduler.class);
    private static final AtomicBoolean isAutomationRunning = new AtomicBoolean(false);
    private static final AtomicBoolean isPdfExtractionsAutomationRunning = new AtomicBoolean(false);
    private final AtomicBoolean useFirstNumber = new AtomicBoolean(true);

    @Autowired
    private TrademarkScrapingService trademarkScrapingService;

    @Autowired
    private PublishedTmServiceExtended publishedTmServiceExtended;

    @Scheduled(cron = "0 0 9-23 * * *", zone = "Asia/Kolkata")
    public void scheduledRun() {
        if (isAutomationRunning.get()) {
            log.warn("Previous session still running, skipping this trigger.");
            return;
        }

        isAutomationRunning.set(true);
        String phoneNumber = useFirstNumber.getAndSet(!useFirstNumber.get()) ? "9878987497" : "6239771006";

        log.info("Automation Scheduler triggered at: {}", LocalDateTime.now());
        trademarkScrapingService.executeTrademarkAutomationForUpdates(phoneNumber);
    }

    @Scheduled(cron = "0 0 12 * * *", zone = "Asia/Kolkata")
    public void downloadLatestJournal() throws IOException {
        log.info("Journal pdf downloader Automation Scheduler triggered at: {}", LocalDateTime.now());
        publishedTmServiceExtended.downloadLatestPdfAndreadPdfFileV2();
    }

    @Scheduled(cron = "0 */5 * * * *", zone = "Asia/Kolkata")
    public void processTmExtraction() {
        if (isPdfExtractionsAutomationRunning.get()) {
            log.warn("Previous pdf extractions session still running, skipping this trigger.");
            return;
        }

        isPdfExtractionsAutomationRunning.set(true);
        log.info("Journal pdf tm extraction Automation Scheduler triggered at: {}", LocalDateTime.now());
        publishedTmServiceExtended.readMissingPdfFile();
    }

    public static boolean isRunning() {
        return isAutomationRunning.get();
    }

    public static void setRunning(boolean value) {
        isAutomationRunning.set(value);
    }

    public static boolean getIspdfextractionsautomationrunning() {
        return isPdfExtractionsAutomationRunning.get();
    }

    public static void setIspdfextractionsautomationrunning(boolean value) {
        isPdfExtractionsAutomationRunning.set(value);
    }
}
