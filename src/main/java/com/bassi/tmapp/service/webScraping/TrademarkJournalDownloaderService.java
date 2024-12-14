package com.bassi.tmapp.service.webScraping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;

import io.micrometer.core.ipc.http.HttpSender.Response;
import io.undertow.util.FileUtils;

@Component
public class TrademarkJournalDownloaderService {
	
	
	private static final String trademarkJournalBaseURL = "https://search.ipindia.gov.in/IPOJournal/Journal/";

	private static final Logger log = LoggerFactory.getLogger(TrademarkJournalDownloaderService.class);
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
        	System.out.println(System.getProperty("javax.net.ssl.trustStore"));
			Document doc = Jsoup.connect(trademarkJournalBaseURL + "Trademark").get();
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
		            HttpHeaders headers = new HttpHeaders();
		            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		            formData.add("FileName", pdfFileName);

		            
		            log.info("Going to download {} using the url: {}", sanitizedPdfFileName, completeUrl);

		            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData,headers);
		            ResponseEntity<byte[]> result  = restTemplate.postForEntity(completeUrl, entity,byte[].class);
		            byte[] bytes = result.getBody();
		            
		            Files.write(new File(journalDirectory + "/" + sanitizedPdfFileName).toPath(), bytes);
		            

		            OutputStream out = new FileOutputStream(journalDirectory.toString());
		            out.write(bytes);
		            out.close();
		            
		        }
			
		} catch (IOException e) {
		
			e.printStackTrace();
		}

		
	}
	
	
	

}
