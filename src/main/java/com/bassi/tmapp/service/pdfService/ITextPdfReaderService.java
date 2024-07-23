package com.bassi.tmapp.service.pdfService;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bassi.tmapp.service.PdfReaderService;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.geom.Vector;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.EventType;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.data.IEventData;
import com.itextpdf.kernel.pdf.canvas.parser.data.TextRenderInfo;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IEventListener;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;

@Service
@Transactional
public class ITextPdfReaderService {
	
private static final Logger log = LoggerFactory.getLogger(ITextPdfReaderService.class);
	
	private static final float MIN_TM_HEIGHT = 23;
	private static final float MAX_TM_HEIGHT = 24.5f;
	private static final float MIN_APPLICATION_NO_HEIGHT = 11;
	private static final float MAX_APPLICATION_NO_HEIGHT = 13;
	private static final Pattern tmClassPattern = Pattern.compile("Trade Marks Journal No:\\s*\\d{4}\\s*,\\s+.+Class\\s+(\\d{1,2})");
	private static final Pattern applicationInfoPattern  = Pattern.compile("(\\d{5,7})\s+(0?[1-9]|[12][0-9]|3[01][\\/\\-]0?[1-9]|1[012][\\/\\-]\\d{4})");
	
	private List<PublishedTmDTO> publishedTrademarks = new ArrayList<>();
	private PublishedTmDTO currentPublishedTmDto;
	

	@EventListener(ApplicationReadyEvent.class)
	public void readPdf() throws IOException {
		String src = "pdfs/1-16.pdf";

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        CustomTextExtractionStrategy strategy = new CustomTextExtractionStrategy();
        PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);
        
        for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
        	currentPublishedTmDto =  new PublishedTmDTO();
            processor.processPageContent(pdfDoc.getPage(i));
            
            List<LineInfo> lines = strategy.getLines();
            extractAndSavePublishedTrademark(lines);
            	
                
        }
        
		pdfDoc.close();

	}
	
	public PublishedTmDTO extractAndSavePublishedTrademark(List<LineInfo> lines) {
		
		extractTrademarkClass(lines);
		extractApplicationNumberAndDate(lines);
		extractAgentNameAndAddress(lines);
		extractTrademark(lines);
		
		extractProprietorNameAndAddress(lines);
		extractDetails(lines);
		extractUsage(lines);
		extractAssociatedTm(lines);
		extractHeadOffice(lines);
		
		
		return null;
		
	}

	private void extractHeadOffice(List<LineInfo> lines) {
		if(currentPublishedTmDto.getTextIndexMap(PublishedTmDTO.TRADEMARK_USAGE));
		
	}

	private void extractAssociatedTm(List<LineInfo> lines) {
		
	}

	private void extractUsage(List<LineInfo> lines) {
		if(currentPublishedTmDto.getUsage() == null) {
			for(LineInfo line:lines) {
				WordInfo words = line.getAllWordsFromSameLineWithInfo();
				if(words.getText().equals("Proposed to be") ||  words.getText().contains("Used Since")) {
					currentPublishedTmDto.setUsage(words.getText());
					currentPublishedTmDto.setTextIndexes(PublishedTmDTO.TRADEMARK_USAGE, lines.indexOf(line));;
					break;
				}
			}
		}
	}

	private void extractDetails(List<LineInfo> lines) {
		
	}

	private void extractAgentNameAndAddress(List<LineInfo> lines) {
		Optional<LineInfo> agentAddressLine =  lines.stream().filter(line -> {
			WordInfo wordInfo = line.getAllWordsFromSameLineWithInfo();
			return wordInfo.getText().contains("Address for service in India"); 
		}).findFirst();
		if(agentAddressLine.isPresent()) {
			int agentAddressLineIdx  =lines.indexOf(agentAddressLine.get());
			if(agentAddressLineIdx != -1) {
				LineInfo agentNameLine = lines.get(agentAddressLineIdx + 1);
				WordInfo agentName =  agentNameLine.getAllWordsFromSameLineWithInfo();
				currentPublishedTmDto.setAgentName(agentName.getText());
			}
			
			log.info("Going to extract agent address");
			Optional<LineInfo> trademarkUsageLine =  lines.stream().filter(line -> {
				WordInfo wordInfo = line.getAllWordsFromSameLineWithInfo();
				return wordInfo.getText().contains("Proposed to be Used") || wordInfo.getText().contains("Used Since")  ; 
			}).findFirst();
			if(trademarkUsageLine.isPresent()) {
				int trademarkUsageIdx = lines.indexOf(trademarkUsageLine.get());
				
			List<String> agentAddressLines = 	lines.subList(agentAddressLineIdx, trademarkUsageIdx).stream()
				.map(line -> line.getAllWordsFromSameLineWithInfo().getText())
				.toList();
				
				String agentAddress = String.join("", agentAddressLines);
				currentPublishedTmDto.setAgentAddress(agentAddress);
			}
			
			log.info("Going to save the index so it can be used for proprietor name and address");
			currentPublishedTmDto.setTextIndexes(PublishedTmDTO.AGENT_NAME_ADDRESS, agentAddressLineIdx);
			
		}
		
		
		
	}

	private void extractProprietorNameAndAddress(List<LineInfo> lines) {
		Map<String,Integer> textIndexMap = currentPublishedTmDto.getTextIndexMap();
		if (textIndexMap.containsKey(PublishedTmDTO.APPLICATION_NUMBER_ADDRESS)
				&& textIndexMap.containsKey(PublishedTmDTO.AGENT_NAME_ADDRESS)) {
			int applicationNumberAddressIdx = textIndexMap.get(PublishedTmDTO.APPLICATION_NUMBER_ADDRESS);
			String proprietorName = lines.get(applicationNumberAddressIdx + 1)
					.getAllWordsFromSameLineWithInfo()
					.getText();
			currentPublishedTmDto.setProprietorName(proprietorName);
			
			// increasing the fromIndex by 2 because it's inclusive and next index is for proprietor name 
			List<String> proprietorAddressTextList =  lines
			.subList(applicationNumberAddressIdx+2, textIndexMap.get(PublishedTmDTO.AGENT_NAME_ADDRESS))
			.stream()
			.map(line -> line.getAllWordsFromSameLineWithInfo().getText())
			.toList();
			
			String proprietorAddressText = String.join("", proprietorAddressTextList);
			currentPublishedTmDto.setProprietorAddress(proprietorAddressText);
			
			
			
		}
		
	}

	private void extractApplicationNumberAndDate(List<LineInfo> lines) {
		if(currentPublishedTmDto.getApplicationNo() == null && currentPublishedTmDto.getApplicationDate() == null) {
			for(LineInfo line:lines) {
				WordInfo words = line.getAllWordsFromSameLineWithInfo();
				Matcher matcher = applicationInfoPattern.matcher(words.getText());
				if(matcher.find()) {
					Long applicationNo = Long.valueOf(matcher.group(1));
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mm-yyyy");
					LocalDate applicationDate =  LocalDate.parse(matcher.group(2), formatter);
					currentPublishedTmDto.setApplicationNo(applicationNo);
					currentPublishedTmDto.setApplicationDate(applicationDate);
					
					log.info("Going to save the index so it can be used for proprietor name and address");
					currentPublishedTmDto.setTextIndexes(PublishedTmDTO.APPLICATION_NUMBER_ADDRESS, lines.indexOf(line));
					break;
				}
			}

			
		}
	}

	private void extractTrademark(List<LineInfo> lines) {
		for(LineInfo line:lines) {
			WordInfo words = line.getAllWordsFromSameLineWithInfo();
			if(words.getFontSize() >= MIN_TM_HEIGHT && words.getFontSize() <= MAX_TM_HEIGHT) {
				String trademark = currentPublishedTmDto.getName(); 
				if( trademark != null) {
					currentPublishedTmDto.setName(trademark.concat(words.getText()));
				}
				currentPublishedTmDto.setName(words.getText());
			}
		}

		
	}

	private void extractTrademarkClass(List<LineInfo> lines) {
		if(currentPublishedTmDto.getTmClass() == null) {
			for(LineInfo line:lines) {
				WordInfo words = line.getAllWordsFromSameLineWithInfo();
				Matcher matcher =  tmClassPattern.matcher(words.getText());
				if(matcher.find()) {
					int tmClass = Integer.parseInt(matcher.group(1));
					currentPublishedTmDto.setTmClass(tmClass);
					break;
				}
			}
		}
	}
}



