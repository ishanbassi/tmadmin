package com.bassi.tmapp.service.webScraping;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.TrademarkScheduler;
import com.bassi.tmapp.service.TrademarkService;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.extended.PublishedTmPhoneticsServiceExtended;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class TrademarkScrapingService {

    private static final String TrademarkJournalBaseURL = "https://search.ipindia.gov.in/IPOJournal/Journal/";
    private static final String TrademarkStatusURL = "https://tmrsearch.ipindia.gov.in/estatus";

    private static final Logger log = LoggerFactory.getLogger(TrademarkScrapingService.class);
    private final PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended;
    private final RestTemplate restTemplate;
    private final PublishedTmRepositoryExtended publishedTmRepositoryExtended;
    private final TrademarkService trademarkService;
    private final TrademarkMapper trademarkMapper;
    private final TrademarkRepository trademarkRepository;
    private OtpWaitingService otpWaitingService;

    @Value("${selenium.headless}")
    private Boolean isHeadless;

    @Value("${selenium.asset}")
    private String baseUploadDirectory;

    public TrademarkScrapingService(
        PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended,
        RestTemplate restTemplate,
        PublishedTmRepositoryExtended publishedTmRepositoryExtended,
        TrademarkService trademarkService,
        TrademarkMapper trademarkMapper,
        TrademarkRepository trademarkRepository,
        OtpWaitingService otpWaitingService
    ) {
        this.restTemplate = restTemplate;
        this.publishedTmPhoneticsServiceExtended = publishedTmPhoneticsServiceExtended;
        this.publishedTmRepositoryExtended = publishedTmRepositoryExtended;
        this.trademarkService = trademarkService;
        this.trademarkMapper = trademarkMapper;
        this.trademarkRepository = trademarkRepository;
        this.otpWaitingService = otpWaitingService;
    }

    @Value("${pdf-file-base-path}")
    private String basePdfDirectory;

    public Integer downloadLatestPdf() throws IOException {
        Document doc = Jsoup.connect(TrademarkJournalBaseURL + "Trademark").timeout(60000).get();
        Element journalElement = doc.getElementById("Journal");

        // We will download only the first journal
        Element firstTr = journalElement.select("tbody tr").first();
        Integer journalNo = Integer.valueOf(firstTr.select("td").get(1).text());

        log.info("Going to download pdfs for the journalNo : {}", journalNo);

        downloadPdfsForJournal(firstTr, journalNo);
        return journalNo;
    }

    @Async
    public void downloadLatestPdfAsync() throws IOException {
        downloadLatestPdf();
    }

    @Async
    public void scrapeAndSetNameAndStatus(List<PublishedTm> publishedTmArr) {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        WebDriver driver = new ChromeDriver(chromeOptions);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            for (PublishedTm publishedTm : publishedTmArr) {
                try {
                    // Open the page
                    driver.get("https://tmrsearch.ipindia.gov.in/eregister/Application_View.aspx");

                    // Select the radio button
                    WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("rdb_0")));
                    radioButton.click();

                    // Enter application number
                    WebElement applNumberInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("applNumber")));
                    applNumberInput.sendKeys(String.valueOf(publishedTm.getApplicationNo()));

                    // solve captcha and handle alert
                    solveCaptchaAndHandleAlert(wait, driver);

                    // Wait for the result
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1")));

                    // Click the first result
                    WebElement firstResult = driver.findElement(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1"));
                    firstResult.click();

                    // Extract trademark and status
                    WebElement trademarkElement = wait.until(
                        ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='TM Applied For']"))
                    );
                    String trademark = trademarkElement.findElement(By.xpath("following-sibling::td")).getText();

                    WebElement statusElement = driver.findElement(By.xpath("//td[font/b[contains(text(), 'Status')]]"));
                    String fullText = statusElement.getText();

                    String statusText = fullText.split(":")[1].trim();
                    updatePublishedTrademarkAndSavePhonetics(publishedTm, trademark, statusText);
                } catch (Exception e) {
                    log.info("Error processing application: {}", publishedTm.getApplicationNo());
                    // Retry or skip to the next application
                }
            }
        } finally {
            driver.quit();
        }
    }

    private void updatePublishedTrademarkAndSavePhonetics(PublishedTm publishedTm, String trademark, String statusText) {
        publishedTm.setName(trademark);
        publishedTm.setTrademarkStatus(statusText);
        publishedTmRepositoryExtended.updateNameAndTrademarkStatusByIdOrApplicationNo(
            trademark,
            statusText,
            publishedTm.getId(),
            publishedTm.getApplicationNo()
        );

        publishedTmPhoneticsServiceExtended.savePhoneticsFromPublishedTm(publishedTm);
    }

    private void solveCaptchaAndHandleAlert(WebDriverWait wait, WebDriver driver) {
        WebElement captchaImage = driver.findElement(By.id("ImageCaptcha"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String base64Image = (String) js.executeScript(
            "let img = arguments[0]; " +
            "let canvas = document.createElement('canvas'); " +
            "canvas.width = img.naturalWidth; " +
            "canvas.height = img.naturalHeight; " +
            "let ctx = canvas.getContext('2d'); " +
            "ctx.drawImage(img, 0, 0); " +
            "return canvas.toDataURL('image/png').substring(22);", // Remove "data:image/png;base64,"
            captchaImage
        );

        CaptchaResponseBody captchaText = solveCaptcha(base64Image);
        WebElement captchaInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("captcha1")));
        log.info("Captcha Response: {}", captchaText);
        if (captchaText != null) {
            captchaInput.clear();
            captchaInput.sendKeys(captchaText.getResult());
        }

        // Click search button and wait for navigation
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnView")));
        searchButton.click();

        if (isAlertPresent(driver)) {
            solveCaptchaAndHandleAlert(wait, driver);
        }
    }

    private CaptchaResponseBody solveCaptcha(String base64ImageData) {
        CaptchaRequestBody captchaRequestBody = new CaptchaRequestBody();
        captchaRequestBody.setApikey("Gm0iz1chYVbJ1En2QzG8");
        captchaRequestBody.setUserid("ishanbassi23@gmail.com");
        captchaRequestBody.setData(base64ImageData);
        HttpEntity<CaptchaRequestBody> entity = new HttpEntity<>(captchaRequestBody);
        ResponseEntity<CaptchaResponseBody> result = restTemplate.postForEntity(
            "https://api.apitruecaptcha.org/one/gettext",
            entity,
            CaptchaResponseBody.class
        );
        return result.getBody();
    }

    public static class CaptchaRequestBody {

        private String userid;
        private String apikey;
        private String data;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getApikey() {
            return apikey;
        }

        public void setApikey(String apikey) {
            this.apikey = apikey;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public List<Integer> downloadAllPdfs(int start, int end) {
        List<Integer> journalNumbers = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(TrademarkJournalBaseURL + "Trademark").timeout(60000).get();
            Element journalElement = doc.getElementById("Journal");

            Elements allTrs = journalElement.select("tbody tr");
            log.info("Found {} journals to process", allTrs.size());

            for (Element tr : allTrs) {
                Integer journalNo;
                try {
                    journalNo = Integer.valueOf(tr.select("td").get(1).text());
                } catch (NumberFormatException exception) {
                    log.error("Skipping the journal because journal no cannot be extracted: {}", tr.select("td").get(1).text());
                    continue;
                }
                journalNo = Integer.valueOf(tr.select("td").get(1).text());

                if (!(journalNo >= start && journalNo <= end)) {
                    log.info("Skipping the journal no : {} because it outside of the range ", journalNo);
                    continue;
                }
                journalNumbers.add(journalNo);
                log.info("Processing journal number: {}", journalNo);
                downloadPdfsForJournal(tr, journalNo);
            }
        } catch (IOException e) {
            log.error("Error downloading PDFs", e);
            e.printStackTrace();
        }
        return journalNumbers;
    }

    private void downloadPdfsForJournal(Element journalRow, Integer journalNo) throws IOException {
        Path journalDirectory = Files.createDirectories(Paths.get(basePdfDirectory + "/" + journalNo));
        Elements fileNameInput = journalRow.select("input[name=FileName]");

        for (Element input : fileNameInput) {
            String pdfFileName = input.attr("value");
            String completeUrl = TrademarkJournalBaseURL + "ViewJournal";

            // extract last part of the file name
            String sanitizedPdfFileName = sanitizeFileName(pdfFileName);
            Path pdfFilePath = Paths.get(journalDirectory.toString(), sanitizedPdfFileName);

            if (pdfFilePath.toFile().exists()) {
                log.info("Skipping downloading the pdf file: {} because it already exists", sanitizedPdfFileName);
                continue;
            }
            try {
                downloadPdfFile(completeUrl, pdfFileName, pdfFilePath, sanitizedPdfFileName);
            } catch (IOException e) {
                log.error("Error downloading PDF file: {}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String sanitizeFileName(String pdfFileName) {
        String regex = "[^\\\\/]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(pdfFileName);
        return matcher.find() ? matcher.group() : "";
    }

    private void downloadPdfFile(String completeUrl, String pdfFileName, Path pdfFilePath, String sanitizedPdfFileName) throws IOException {
        // ✅ Check if file already exists
        if (Files.exists(pdfFilePath)) {
            log.info("File {} already exists at {}. Skipping download.", sanitizedPdfFileName, pdfFilePath);
            return;
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("FileName", pdfFileName);

        log.info("Going to download {} using the url: {}", sanitizedPdfFileName, completeUrl);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);
        ResponseEntity<byte[]> result = restTemplate.postForEntity(completeUrl, entity, byte[].class);
        byte[] bytes = result.getBody();

        Files.write(pdfFilePath, bytes);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CaptchaResponseBody {

        private String method;
        private boolean success;
        private String requestId;

        @JsonProperty("time_taken")
        private double timeTaken;

        @JsonProperty("cluster_name")
        private String clusterName;

        @JsonProperty("case")
        private String mycase;

        @JsonProperty("cluster_accuracy")
        private double clusterAccuracy;

        private double confidence;
        private boolean valid;
        private String mode;
        private String lambda;
        private String result;

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public double getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(double timeTaken) {
            this.timeTaken = timeTaken;
        }

        public String getMycase() {
            return mycase;
        }

        public void setMycase(String mycase) {
            this.mycase = mycase;
        }

        public double getConfidence() {
            return confidence;
        }

        public void setConfidence(double confidence) {
            this.confidence = confidence;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getLambda() {
            return lambda;
        }

        public void setLambda(String lambda) {
            this.lambda = lambda;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getClusterName() {
            return clusterName;
        }

        public void setClusterName(String clusterName) {
            this.clusterName = clusterName;
        }

        public double getClusterAccuracy() {
            return clusterAccuracy;
        }

        public void setClusterAccuracy(double clusterAccuracy) {
            this.clusterAccuracy = clusterAccuracy;
        }

        @Override
        public String toString() {
            return (
                "CaptchaResponseBody [method=" +
                method +
                ", success=" +
                success +
                ", requestId=" +
                requestId +
                ", timeTaken=" +
                timeTaken +
                ", clusterName=" +
                clusterName +
                ", mycase=" +
                mycase +
                ", clusterAccuracy=" +
                clusterAccuracy +
                ", confidence=" +
                confidence +
                ", valid=" +
                valid +
                ", mode=" +
                mode +
                ", lambda=" +
                lambda +
                ", result=" +
                result +
                "]"
            );
        }
    }

    public boolean isAlertPresent(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
            return true;
        } catch (NoAlertPresentException Ex) { // try
            return false;
        }
    }

    @Async
    public void executeTrademarkAutomationForUpdates(String phoneNumber) {
        Integer journalNo = trademarkRepository.findLatestJournalNoWithMissingData();
        fillAndSubmitOtp(journalNo, phoneNumber);
    }

    @Async
    public void fillAndSubmitOtp(Integer journalNo, String phoneNumber) {
        WebDriver driver = createDriverWithProxy("27.34.242.98", 80);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        try {
            fillAndSubmitOtp(driver, wait, phoneNumber, journalNo);
        } finally {
            //        	takeScreenshot(driver, "error");
            driver.quit();
            TrademarkScheduler.setRunning(false);
        }
    }

    private void solveExpression(WebDriver driver, WebDriverWait wait, WebElement buttonElement, Boolean isFirstStageCaptcha) {
        // Step 2: Read the CAPTCHA expression text
        boolean solved = false;
        int maxAttempts = 5; // safety guard
        int attempt = 0;
        while (!solved && attempt < maxAttempts) {
            attempt++;
            WebElement labelElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("captchatext")));
            String labelExpressionText = labelElement.getText();
            log.info("input value found: " + labelExpressionText);

            WebElement captchaElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("CaptchModel_CaptchaNumbers")));
            String inputExpressionText = captchaElement.getAttribute("value");
            log.info("input value found: " + inputExpressionText);

            String expression = labelExpressionText.concat(" ").concat(inputExpressionText);

            HumanDelay.reading(); // simulate reading the question

            // Step 3: Solve it

            String answer = ExpressionSolver.solve(expression);

            // Step 4: Enter the answer
            WebElement answerInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("CaptchModel_CaptchaAnswer")));
            HumanTyping.clearAndType(answerInput, answer);
            HumanDelay.betweenActions();

            log.info("Computed answer: " + answer);
            HumanClick.moveAndClick(driver, buttonElement);
            if (Boolean.TRUE.equals(isFirstStageCaptcha)) {
                WebElement resultElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("swal2-html-container")));
                String popupText = resultElement.getText().trim();
                if (popupText.contains("OTP has been sent successfully")) {
                    solved = true;
                    log.info("✅ CAPTCHA solved");
                } else if (popupText.contains("Invalid CAPTCHA")) {
                    log.info("❌ CAPTCHA wrong — retrying...");
                    // loop continues
                } else {
                    throw new RuntimeException("Unexpected popup: " + popupText);
                }

                WebElement popupButtonElement = wait.until(ExpectedConditions.elementToBeClickable(By.className("swal2-confirm")));
                HumanClick.moveAndClick(driver, popupButtonElement);
            } else {
                try {
                    wait.until(ExpectedConditions.urlContains("estatus/RegisteredTM"));
                    log.info("Navigation successful");
                    solved = true;
                } catch (TimeoutException e) {
                    log.info("Navigation failed re solving the expression");
                    WebElement refreshCaptcha = wait.until(
                        ExpectedConditions.elementToBeClickable(By.cssSelector("a[onclick='loadCaptcha()']"))
                    );
                    HumanClick.moveAndClick(driver, refreshCaptcha);
                }
            }
        }
    }

    public void fillAndSubmitOtp(WebDriver driver, WebDriverWait wait, String phoneNumber, Integer journalNo) {
        driver.get(TrademarkStatusURL);

        // --- STEP 1: Arrive on page, look around (reading delay) ---
        HumanDelay.reading();

        // Step 2: Enter phone number
        WebElement phoneInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("mobileidentifier")));
        HumanTyping.clearAndType(phoneInput, phoneNumber);
        HumanDelay.betweenActions();

        // Step 2, 3,4
        WebElement sendBtn = driver.findElement(By.id("sendOtpBtn")); // adjust selector
        solveExpression(driver, wait, sendBtn, true);

        // Step 5: Click Send OTP

        HumanDelay.reading();
        if (Boolean.FALSE.equals(isHeadless)) {
            waitForManualOtpEntry(driver, wait);
        } else {
            resolveOtp(driver, wait, phoneNumber);
        }

        //    Step 6 : Click the button on the top left for trademark application number status
        WebElement tmApplicationBtn = wait.until(
            ExpectedConditions.elementToBeClickable(By.xpath("//button[normalize-space()='Trade Mark Application/Registered Mark']"))
        );
        HumanClick.moveAndClick(driver, tmApplicationBtn);

        // Step 7 : Select the radio button
        WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("NationalIRDINumber")));
        HumanClick.moveAndClick(driver, radioButton);

        List<Trademark> tms = trademarkRepository.findTrademarksWhereNameIsNull(journalNo);
        for (Trademark tmToUpdate : tms) {
            // Step 8 : Enter application number
            WebElement applNumberInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("ApplicationNumber")));
            HumanTyping.clearAndType(applNumberInput, String.valueOf(tmToUpdate.getApplicationNo()));
            HumanDelay.betweenActions();

            // Step 9: solve the expression & fill the input
            WebElement viewButtonElement = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnView")));
            solveExpression(driver, wait, viewButtonElement, false);

            // Extract trademark and status

            WebElement statusValue = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                    By.xpath("(//table)[1]//span[normalize-space()='Status:']/following-sibling::span[1]")
                )
            );
            String status = statusValue.getText();

            log.info(status);

            WebElement trademarkElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("(//table)[2]//tr[td]/td[5]")));
            String trademark = trademarkElement.getText();

            log.info(trademark);
            trademarkService.updateNameAndTrademarkStatusByIdOrApplicationNo(trademark, status, tmToUpdate);

            // Click the back button

            WebElement backButtonElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[normalize-space()='Back']")));
            HumanClick.moveAndClick(driver, backButtonElement);
            // reset the session

        }
    }

    public void waitForManualOtpEntry(WebDriver driver, WebDriverWait wait) {
        // Find the OTP input field
        WebDriverWait waitForOtp = new WebDriverWait(driver, Duration.ofSeconds(120));
        WebElement otpInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp")));

        log.info("==============================================");
        log.info("  OTP sent to your phone!");
        log.info("  Please type the OTP in the browser window.");
        log.info("  Waiting for you to enter it...");
        log.info("==============================================");

        // Wait until the OTP field has a value of expected length (e.g. 6 digits)

        waitForOtp.until(driver1 -> {
            String value = otpInput.getAttribute("value");
            return value != null && value.length() == 6; // adjust OTP length
        });

        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#otpSection button")));
        HumanClick.moveAndClick(driver, button);
    }

    private void resolveOtp(WebDriver driver, WebDriverWait wait, String phoneNumber) {
        WebElement otpInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("otp")));
        // Server: block until OTP is POSTed to /api/otp/submit
        log.info("Server mode: waiting for OTP via REST API for {}", phoneNumber);
        try {
            String otp = otpWaitingService.waitForOtp(phoneNumber, 120); // 2 min timeout

            HumanTyping.clearAndType(otpInput, otp);

            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#otpSection button")));
            HumanClick.moveAndClick(driver, button);
        } catch (Exception e) {
            throw new RuntimeException("Failed to receive OTP: " + e.getMessage());
        }
    }

    public void resetSession(WebDriver driver) {
        // Clear all cookies
        driver.manage().deleteAllCookies();
        System.out.println("Cookies cleared.");

        // Clear cache and local storage via JavaScript
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.localStorage.clear();");
        js.executeScript("window.sessionStorage.clear();");

        HumanDelay.between(1000, 2000);
    }

    public WebDriver createDriverWithProxy(String proxyHost, int proxyPort) {
        List<String> userAgents = List.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 Chrome/121.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 Chrome/120.0.0.0 Safari/537.36"
        );
        String agent = userAgents.get(new Random().nextInt(userAgents.size()));

        //	    Proxy proxy = new Proxy();
        //	    proxy.setHttpProxy(proxyHost + ":" + proxyPort);
        //	    proxy.setSslProxy(proxyHost + ":" + proxyPort);
        //	    options.setProxy(proxy);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--user-agent=" + agent);
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", List.of("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);
        options.addArguments("--window-size=1920,1080");
        if (Boolean.TRUE.equals(isHeadless)) {
            options.addArguments("--headless=new"); // use "new" headless (Chrome 112+), more stable than old "--headless"
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        }

        return new ChromeDriver(options);
    }

    public void takeScreenshot(WebDriver driver, String name) {
        Path path = Paths.get(baseUploadDirectory, "screenshots");
        try {
            Files.createDirectories(path);
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), path.resolve(name + ".png"));
        } catch (IOException e) {
            throw new InternalServerAlertException("Unable to Save document file Reason: " + e.getLocalizedMessage());
        }
    }
}
