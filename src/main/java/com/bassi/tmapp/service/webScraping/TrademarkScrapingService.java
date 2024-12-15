package com.bassi.tmapp.service.webScraping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.undertow.util.FileUtils;

@Component
public class TrademarkScrapingService {
	
	
	private static final String trademarkJournalBaseURL = "https://search.ipindia.gov.in/IPOJournal/Journal/";

	private static final Logger log = LoggerFactory.getLogger(TrademarkScrapingService.class);
//	private final RestTemplate restTemplate;
//	
//	
//	TrademarkJournalDownloaderService(RestTemplate restTemplate) {
//		this.restTemplate = restTemplate;
//	}
	
	@Value("${pdf-file-base-path}")
    private String basePdfDirectory;
	
    @EventListener(ApplicationReadyEvent.class)
	void downloadPdf() {
        try {
			Document doc = Jsoup.connect(trademarkJournalBaseURL + "Trademark")
					.timeout(60000)
					.get();
			Element journalElement = doc.getElementById("Journal");
			
			// We will download only the first journal
			Element firstTr = journalElement.select("tbody tr").first();
			Integer journalNo = Integer.valueOf(firstTr.select("td").get(1).text());

			log.info("Going to download pdfs for the journalNo : {}", journalNo);

			Elements fileNameInput = firstTr.select("input[name=FileName]");
			log.info("Found {} pdf files to download", fileNameInput.size());
			
			Path journalDirectory  = Files.createDirectories(Paths.get(basePdfDirectory + "/" + journalNo ));
			

			
			 for (Element input : fileNameInput) {
				 	RestTemplate restTemplate = new RestTemplate();
		            String pdfFileName = input.attr("value");
		            String completeUrl = trademarkJournalBaseURL + "ViewJournal";
		            
		            // extract last part of the file name
		            String sanitizedPdfFileName;
		            String regex = "[^\\\\]+$"; 

		            Pattern pattern = Pattern.compile(regex);
		            Matcher matcher = pattern.matcher(pdfFileName);
		            if (matcher.find()) {
		            	sanitizedPdfFileName = matcher.group();
		            } else {
		            	sanitizedPdfFileName = "";
		            }
		            Path pdfFilePath =  Paths.get(journalDirectory.toString() , sanitizedPdfFileName);
		            if(pdfFilePath.toFile().exists()) {
		            	log.info("Skipping downloading the pdf file: {} because it already exists", sanitizedPdfFileName);
		            	continue;
		            }
		            
		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		            formData.add("FileName", pdfFileName);

		            
		            log.info("Going to download {} using the url: {}", sanitizedPdfFileName, completeUrl);
		            
		            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData,headers);
		            ResponseEntity<byte[]> result  = restTemplate.postForEntity(completeUrl, entity,byte[].class);
		            byte[] bytes = result.getBody();
		            
		            Files.write(pdfFilePath, bytes);
		        }
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}

		
	}
    @EventListener(ApplicationReadyEvent.class)
    public void scrape() {
    	List<PublishedTm> applNumberArr = new ArrayList<>();
    	PublishedTm test  = new PublishedTm();
    	test.setApplicationNo(Long.valueOf(2340394));
    	applNumberArr.add(test);
        WebDriver driver = setupDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));

        try {
            for (int i = 0; i < applNumberArr.size(); i++) {
            	PublishedTm applNumber = applNumberArr.get(i);
                try {
                    // Open the page
                    driver.get("https://tmrsearch.ipindia.gov.in/eregister/Application_View.aspx");

                    // Select the radio button
                     WebElement radioButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("rdb_0")));
                    radioButton.click();

                    // Navigate
                    driver.navigate().refresh();

                    // Enter application number
                    WebElement applNumberInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("applNumber")));
                    applNumberInput.sendKeys(String.valueOf(applNumber.getApplicationNo()));

                    // Click search button and wait for navigation
                    WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnView")));
                    searchButton.click();

                    // Handle captcha (replace with actual solution logic)
                    String captchaText = solveCaptcha(driver);
                    WebElement captchaInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("captcha1")));
                    captchaInput.sendKeys(captchaText);
                    captchaInput.submit();

                    // Wait for the result
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1")));

                    // Click the first result
                    WebElement firstResult = driver.findElement(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1"));
                    firstResult.click();

                    // Extract trademark and status
                    WebElement trademarkElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='TM Applied For']")));
                    String trademark = trademarkElement.findElement(By.xpath("following-sibling::td")).getText();

                    WebElement statusElement = driver.findElement(By.xpath("//b[contains(text(), 'Status')]"));
                    String tmStatus = statusElement.findElement(By.xpath("following-sibling::b")).getText();

                    // Update the data
                    dataUpdate(applNumber.getApplicationNo(), trademark, null, tmStatus);
                    System.out.println("Processed application: " + applNumber.getApplicationNo());
                } catch (Exception e) {
                    System.err.println("Error processing application: " + applNumber.getApplicationNo());
                    // Retry or skip to the next application
                }
            }
        } finally {
            driver.quit();
        }
    }

    private WebDriver setupDriver() {
        // Set up ChromeDriver
//        System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");
        return new ChromeDriver();
    }

    private String solveCaptcha(WebDriver driver) {
        // Implement captcha-solving logic here (e.g., using OCR)
        return "dummyCaptcha";
    }

    private void dataUpdate(long applicationNo, String trademark, Object o, String tmStatus) {
        // Implement your database update logic here
    }

    public static class Application {
        private long applicationNo;

        public long getApplicationNo() {
            return applicationNo;
        }

        public void setApplicationNo(long applicationNo) {
            this.applicationNo = applicationNo;
        }
    }
	
	
	

}
