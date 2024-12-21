package com.bassi.tmapp.service.webScraping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.extended.PublishedTmPhoneticsServiceExtended;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@Component
public class TrademarkScrapingService {
	
	
	private static final String TrademarkJournalBaseURL = "https://search.ipindia.gov.in/IPOJournal/Journal/";

	private static final Logger log = LoggerFactory.getLogger(TrademarkScrapingService.class);
	private final PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended;
	private final RestTemplate restTemplate;
	private final PublishedTmRepositoryExtended publishedTmRepositoryExtended;
	
	public TrademarkScrapingService(PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended,
			RestTemplate restTemplate,PublishedTmRepositoryExtended publishedTmRepositoryExtended) {
		this.restTemplate = restTemplate;
		this.publishedTmPhoneticsServiceExtended = publishedTmPhoneticsServiceExtended;
		this.publishedTmRepositoryExtended = publishedTmRepositoryExtended;
	}
	
	@Value("${pdf-file-base-path}")
    private String basePdfDirectory;
	
	public Integer downloadPdf() {
		Integer journalNo = null;
        try {
			Document doc = Jsoup.connect(TrademarkJournalBaseURL + "Trademark")
					.timeout(60000)
					.get();
			Element journalElement = doc.getElementById("Journal");
			
			// We will download only the first journal
			Element firstTr = journalElement.select("tbody tr").first();
			journalNo = Integer.valueOf(firstTr.select("td").get(1).text());

			log.info("Going to download pdfs for the journalNo : {}", journalNo);

			Elements fileNameInput = firstTr.select("input[name=FileName]");
			log.info("Found {} pdf files to download", fileNameInput.size());
			
			Path journalDirectory  = Files.createDirectories(Paths.get(basePdfDirectory + "/" + journalNo ));
			

			
			 for (Element input : fileNameInput) {
		            String pdfFileName = input.attr("value");
		            String completeUrl = TrademarkJournalBaseURL + "ViewJournal";
		            
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
        return journalNo;

		
	}
    public void scrape(List<PublishedTm> publishedTmArr) {
        WebDriver driver = setupDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(60));
        
        
        try {
            for (PublishedTm publishedTm:publishedTmArr) {
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
                    solveCaptchaAndHandleAlert(wait,driver);
  

                    // Wait for the result
                    wait.until(ExpectedConditions.presenceOfElementLocated(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1")));

                    // Click the first result
                    WebElement firstResult = driver.findElement(By.id("SearchWMDatagrid_ctl03_lnkbtnappNumber1"));
                    firstResult.click();

                    // Extract trademark and status
                    WebElement trademarkElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='TM Applied For']")));
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
    
	private void updatePublishedTrademarkAndSavePhonetics(PublishedTm publishedTm, String trademark,
			String statusText) {
		publishedTm.setName(trademark);
		publishedTm.setTrademarkStatus(statusText);
		publishedTmRepositoryExtended.updateNameAndTrademarkStatusByIdOrApplicationNo(trademark, statusText,
				publishedTm.getId(), publishedTm.getApplicationNo());
		
		publishedTmPhoneticsServiceExtended.savePhoneticsFromPublishedTm(publishedTm);
	}

    private void solveCaptchaAndHandleAlert(WebDriverWait wait,WebDriver driver) {
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
                captchaImage);

        CaptchaResponseBody captchaText = solveCaptcha(base64Image);
        WebElement captchaInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("captcha1")));
        log.info("Captcha Response: {}", captchaText);
        if(captchaText != null) {
        	captchaInput.clear();
            captchaInput.sendKeys(captchaText.getResult());

        }

        // Click search button and wait for navigation
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btnView")));
        searchButton.click();	
        
        if(isAlertPresent(driver)){
            solveCaptchaAndHandleAlert(wait,driver);

        }
	}
	private WebDriver setupDriver() {
        return new ChromeDriver();
    }

    private CaptchaResponseBody solveCaptcha(String base64ImageData) {
        CaptchaRequestBody captchaRequestBody = new CaptchaRequestBody();
        captchaRequestBody.setApikey("Gm0iz1chYVbJ1En2QzG8");
        captchaRequestBody.setUserid("ishanbassi23@gmail.com");
        captchaRequestBody.setData(base64ImageData);
        HttpEntity<CaptchaRequestBody> entity = new HttpEntity<>(captchaRequestBody);
        ResponseEntity<CaptchaResponseBody> result  = restTemplate.postForEntity("https://api.apitruecaptcha.org/one/gettext", entity,CaptchaResponseBody.class);
        return result.getBody();
        
    }
    public static class CaptchaRequestBody{
    	private String userid;
    	private 	String apikey;
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
	
	
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CaptchaResponseBody{
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
			return "CaptchaResponseBody [method=" + method + ", success=" + success + ", requestId=" + requestId
					+ ", timeTaken=" + timeTaken + ", clusterName=" + clusterName + ", mycase=" + mycase
					+ ", clusterAccuracy=" + clusterAccuracy + ", confidence=" + confidence + ", valid=" + valid
					+ ", mode=" + mode + ", lambda=" + lambda + ", result=" + result + "]";
		}
		
		
		
		
	    
	    
	    
    }
    public boolean isAlertPresent(WebDriver driver) 
    { 
        try 
        { 
            driver.switchTo().alert().accept();
            return true; 
        }   // try 
        catch (NoAlertPresentException Ex) 
        { 
            return false; 
        }  
    }   
	

}
