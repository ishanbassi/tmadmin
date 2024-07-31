package com.bassi.tmapp.service.pdfService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.PdfReaderService;
import com.bassi.tmapp.service.PhoneticsService;
import com.bassi.tmapp.service.PublishedTmPhoneticsService;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
	
	private static final Pattern tmClassPattern = Pattern.compile("Trade Marks Journal No:\\s*(\\d{4})\\s*,\\s+.+Class\\s+(\\d{1,2})", Pattern.CASE_INSENSITIVE);
	private static final Pattern applicationInfoPattern  = Pattern.compile("(\\d{5,7})\s+(\\d\\d\\/\\d\\d\\/\\d\\d\\d\\d)", Pattern.CASE_INSENSITIVE);
	
	private PublishedTmDTO currentPublishedTmDto;
	private  PhoneticsService phoneticsService;
	private PublishedTmRepository publishedTmRepository;
	private PublishedTmPhoneticsService publishedTmPhoneticsService;
	
	@Value("${file-upload-base-path}")
    private String baseUploadDirectory;
	
	@Value("${pdf-file-base-path}")
    private String basePdfDirectory;
	
	@Value("${json-file-base-path}")
    private String baseJsonDirectory;
	
	private PublishedTmMapper publishedTmMapper;

	public ITextPdfReaderService(PhoneticsService phoneticsService,PublishedTmRepository publishedTmRepository,PublishedTmMapper publishedTmMapper ) {
		this.phoneticsService = phoneticsService;
		this.publishedTmRepository = publishedTmRepository;
		this.publishedTmMapper = publishedTmMapper;
	}
	

	
	public List<PublishedTmDTO>  readPdf(String pdfFilePath){
		log.info("Going to read pdf file: {}" , pdfFilePath);
		List<PublishedTmDTO> publishedTrademarks = new ArrayList<>();
		PdfDocument pdfDoc;
		try {
			pdfDoc = new PdfDocument(new PdfReader(pdfFilePath));	
		}
		catch(IOException e) {
			throw new InternalServerAlertException("Unable to read pdf file, " + pdfFilePath +  " Reason: " + e.getLocalizedMessage());
		}
        for (int i = 11; i <= 11; i++) {
        	log.info("Going to process page number {}", i);
        	currentPublishedTmDto =  new PublishedTmDTO();
        	
        	CustomTextExtractionStrategy strategy = new CustomTextExtractionStrategy();
            PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);	
            processor.processPageContent(pdfDoc.getPage(i));
            
            //extract text 
            List<LineInfo> lines = strategy.getLines();
            extractAndSavePublishedTrademark(lines);
            
            // extract images
            PdfImage pdfImage = strategy.getImage();
            if(pdfImage != null) {
            	String path = saveToFileSystem(pdfImage);
            	currentPublishedTmDto.setImgUrl(path);
            	
            }
            publishedTrademarks.add(currentPublishedTmDto);
        }
        
		pdfDoc.close();
		publishedTrademarks  =  checkAnyMissingInformation(publishedTrademarks);
		return publishedTrademarks;
	}
	
	public void extractAndSavePublishedTrademark(List<LineInfo> lines) {
		
		extractJournalNoAndTrademarkClass(lines);
		extractApplicationNumberAndDate(lines);
		extractAgentNameAndAddress(lines);
		extractUsage(lines);
		extractTrademark(lines);
		extractProprietorNameAndAddress(lines);
		extractHeadOffice(lines);
		extractDetails(lines);
		extractAssociatedTm(lines);
		
		
		if(currentPublishedTmDto.getName() != null) {
			generatePhonetics(currentPublishedTmDto.getName());
		}
	}

	private void extractHeadOffice(List<LineInfo> lines) {
		if(currentPublishedTmDto.getTextIndexMap().containsKey(PublishedTmDTO.TRADEMARK_USAGE)) {
			for(LineInfo line:lines) {
				String words = line.getAllWordsFromSameLineWithInfo();
				if(words != null) {
					try {
						HeadOffice headOffice = HeadOffice.valueOf(words.trim());
						currentPublishedTmDto.setHeadOffice(headOffice);
						currentPublishedTmDto.setTextIndexes(PublishedTmDTO.HEAD_OFFICE, lines.indexOf(line));
					}
					catch(IllegalArgumentException e) {
					}
				}
		}

	}}

	private void extractAssociatedTm(List<LineInfo> lines) {
		Optional<LineInfo> associatedTmLine =  lines.stream().filter(line -> {
			String wordInfo = line.getAllWordsFromSameLineWithInfo();
			return  wordInfo  != null && wordInfo.contains("To be associated with"); 
		}).findFirst();
		
		if(associatedTmLine.isPresent()) {
			int associatedTmIdx = lines.indexOf(associatedTmLine.get());
			if(associatedTmIdx != -1) {
				LineInfo associatedTmsLineInfo = lines.get(associatedTmIdx+1);
				String associatedTmsWordInfo = associatedTmsLineInfo.getAllWordsFromSameLineWithInfo();
				currentPublishedTmDto.setAssociatedTms(associatedTmsWordInfo);

				
			}
		}

	}

	private void extractUsage(List<LineInfo> lines) {
		if(currentPublishedTmDto.getUsage() == null) {
			for(LineInfo line:lines) {
				String words = line.getAllWordsFromSameLineWithInfo();
				if(words != null && (words.contains("Proposed to be Used") ||  words.contains("Used Since"))) {
					currentPublishedTmDto.setUsage(words);
					currentPublishedTmDto.setTextIndexes(PublishedTmDTO.TRADEMARK_USAGE, lines.indexOf(line));
					break;
				}
			}
		}
	}

	private void extractDetails(List<LineInfo> lines) {
		Map<String,Integer> textIdxMap = currentPublishedTmDto.getTextIndexMap();
		if(textIdxMap.containsKey(PublishedTmDTO.HEAD_OFFICE)) {
			int headOfficeIdx = textIdxMap.get(PublishedTmDTO.HEAD_OFFICE);
			Optional<String> details = lines.subList(headOfficeIdx+1, lines.size())
			.stream()
			.map(LineInfo::getAllWordsFromSameLineWithInfo)
			.reduce((intialString , currentString) -> intialString + " " + currentString);
			
			if(details.isPresent()) {
				currentPublishedTmDto.setDetails(details.get());
			}
		}
	}

	private void extractAgentNameAndAddress(List<LineInfo> lines) {
		Optional<LineInfo> agentAddressLine =  lines.stream().filter(line -> {
			String wordInfo = line.getAllWordsFromSameLineWithInfo();
			return wordInfo != null && wordInfo.contains("Address for service in India"); 
		}).findFirst();
		if(agentAddressLine.isPresent()) {
			int agentAddressLineIdx  =lines.indexOf(agentAddressLine.get());
			if(agentAddressLineIdx != -1) {
				LineInfo agentNameLine = lines.get(agentAddressLineIdx + 1);
				String agentName =  agentNameLine.getAllWordsFromSameLineWithInfo();
				currentPublishedTmDto.setAgentName(agentName);
			}
			
			log.info("Going to extract agent address");
			Optional<LineInfo> trademarkUsageLine =  lines.stream().filter(line -> {
				String wordInfo = line.getAllWordsFromSameLineWithInfo();
				return wordInfo != null &&( wordInfo.contains("Proposed to be Used") || wordInfo.contains("Used Since"))  ; 
			}).findFirst();
			if(trademarkUsageLine.isPresent()) {
				int trademarkUsageIdx = lines.indexOf(trademarkUsageLine.get());
				
			Optional<String> agentAddress = 	lines.subList(agentAddressLineIdx+2, trademarkUsageIdx).stream()
				.map(LineInfo::getAllWordsFromSameLineWithInfo)
				.reduce((intialString , currentString) -> intialString + " " + currentString);
				if(agentAddress.isPresent()) {
					currentPublishedTmDto.setAgentAddress(agentAddress.get());
				}
			}
			
			log.info("Going to save the index so it can be used for proprietor name and address");
			currentPublishedTmDto.setTextIndexes(PublishedTmDTO.AGENT_NAME_ADDRESS, agentAddressLineIdx);
			
		}
		
		
		
	}

	private void extractProprietorNameAndAddress(List<LineInfo> lines) {
		Map<String,Integer> textIndexMap = currentPublishedTmDto.getTextIndexMap();
		if (textIndexMap.containsKey(PublishedTmDTO.APPLICATION_NUMBER_DATE)) {
			int applicationNumberDateIdx = textIndexMap.get(PublishedTmDTO.APPLICATION_NUMBER_DATE);
			String proprietorName = lines.get(applicationNumberDateIdx + 1)
					.getAllWordsFromSameLineWithInfo();
			currentPublishedTmDto.setProprietorName(proprietorName);
			

			int proprietorAddressMaxIdx = textIndexMap.containsKey(PublishedTmDTO.AGENT_NAME_ADDRESS)
					? textIndexMap.get(PublishedTmDTO.AGENT_NAME_ADDRESS)
					: textIndexMap.get(PublishedTmDTO.TRADEMARK_USAGE);
			// increasing the fromIndex by 2 because it's inclusive and next index is for proprietor name
			Optional<String> proprietorAddressText =  lines
			.subList(applicationNumberDateIdx+2, proprietorAddressMaxIdx)
			.stream()
			.map(LineInfo::getAllWordsFromSameLineWithInfo)
			.reduce((intialString , currentString) -> intialString + " " + currentString);

			if(proprietorAddressText.isPresent()) {
				currentPublishedTmDto.setProprietorAddress(proprietorAddressText.get());
			}
			
		}
		
	}

	private void extractApplicationNumberAndDate(List<LineInfo> lines) {
		if(currentPublishedTmDto.getApplicationNo() == null && currentPublishedTmDto.getApplicationDate() == null) {
			for(LineInfo line:lines) {
				String words = line.getAllWordsFromSameLineWithInfo();
				if(words != null) {
					Matcher matcher = applicationInfoPattern.matcher(words);
					boolean matched =matcher.find();
					if(matched) {
						Long applicationNo = Long.valueOf(matcher.group(1));
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
						LocalDate applicationDate =  LocalDate.parse(matcher.group(2), formatter);
						currentPublishedTmDto.setApplicationNo(applicationNo);
						currentPublishedTmDto.setApplicationDate(applicationDate);
						
						log.info("Going to save the index so it can be used for proprietor name and address");
						currentPublishedTmDto.setTextIndexes(PublishedTmDTO.APPLICATION_NUMBER_DATE, lines.indexOf(line));
						break;
					}
				}
				
			}

		}
	}

	private void extractTrademark(List<LineInfo> lines) {
		Map<String, Integer> textIndexMap = currentPublishedTmDto.getTextIndexMap();
		if (textIndexMap.containsKey(PublishedTmDTO.TM_CLASS)
				&& textIndexMap.containsKey(PublishedTmDTO.APPLICATION_NUMBER_DATE)) {

			List<LineInfo> subLine = lines.subList(textIndexMap.get(PublishedTmDTO.TM_CLASS) + 1,
					textIndexMap.get(PublishedTmDTO.APPLICATION_NUMBER_DATE));
			for (LineInfo line : subLine) {
				String words = line.getAllWordsFromSameLineWithInfo();
				if(words != null) {
					String trademark = currentPublishedTmDto.getName();
					if (trademark != null) {
						currentPublishedTmDto.setName(trademark.concat(words));
					} else {
						currentPublishedTmDto.setName(words);
					}
				}
			}
		}
	}

	private void extractJournalNoAndTrademarkClass(List<LineInfo> lines) {
		if(currentPublishedTmDto.getJournalNo() == null  &&  currentPublishedTmDto.getTmClass() == null) {
			for(LineInfo line:lines) {
				String words = line.getAllWordsFromSameLineWithInfo();
				if(words != null) {
					Matcher matcher =  tmClassPattern.matcher(words);
					boolean matched =matcher.find(); 
					if(matched) {
						int journalNo = Integer.parseInt(matcher.group(1));
						int tmClass = Integer.parseInt(matcher.group(2));
						currentPublishedTmDto.setJournalNo(journalNo);
						currentPublishedTmDto.setTmClass(tmClass);
						currentPublishedTmDto.setTextIndexes(PublishedTmDTO.TM_CLASS, lines.indexOf(line));
						break;
					}
				}

			}
		}
	}
	
	
	private String saveToFileSystem(PdfImage pdfImage) { 
		log.info("Going to save image : in the file system"  );
		byte[] content = pdfImage.getImageContent(); 
		String extensionType = pdfImage.getImageType();
		String applicationNumber = currentPublishedTmDto.getApplicationNo().toString();
		String resourcesDir = Paths.get(baseUploadDirectory).toAbsolutePath().toString();		
		String filePath  = new Date().getTime() + "-"+ applicationNumber +  "." + extensionType;
		Path newFile = Paths.get(resourcesDir , filePath);
		log.info("Going to write image in the file system at: {}" ,newFile);
		try {
			Files.createDirectories(newFile.getParent());
			Files.write(newFile, content);
		}
		catch(IOException e) {
			log.error("unable to save image, Reason: {}", e.getLocalizedMessage());
		}
		return filePath;
	}

	public List<String> generatePhonetics(String trademark) {
		List<String> subWords = new ArrayList<>(Arrays.asList(trademark.split(" ")));
		subWords.add(trademark);
		return subWords.stream().map(word -> phoneticsService.generatePhonetics(word)).toList();
	}
	
	
	public void readPdfFilesFromFileSystem(String journalNo) {
		File baseDirectory = new File(Paths.get(basePdfDirectory).toAbsolutePath().toString() + "/" + journalNo);
		Stream.of(baseDirectory.listFiles()).map(File::getAbsolutePath)
				.forEach(path -> {
					try {
						List<PublishedTmDTO> publishedTrademarksDto = readPdf(path);
						List<PublishedTm> publishedTrademarks = publishedTmMapper.toEntity(publishedTrademarksDto);
						savePublishedTmAndGeneratePhoneticsDto(publishedTrademarks);
						
					}
					catch(Exception e) { 
						throw new InternalServerAlertException("Unable to Read pdf files from the journal " + journalNo + " Reason: " + e.getLocalizedMessage());
					}
				});
		
		
		
	}
	
	private void savePublishedTmAndGeneratePhoneticsDto(List<PublishedTm> publishedTrademarks) {
		publishedTrademarks =  publishedTmRepository.saveAll(publishedTrademarks);
		List<PublishedTmPhonetics> publishedPhonetics = publishedTmPhoneticsService.saveAll(publishedTrademarks); 
		
		
	}



	private List<PublishedTmDTO> checkAnyMissingInformation(List<PublishedTmDTO> publishedTrademarks) {
		List<PublishedTmDTO> errors = new ArrayList<>();
		List<PublishedTmDTO> validTms = new ArrayList<>();
		for(PublishedTmDTO tm: publishedTrademarks) {
			if(tm != null && tm.getName() != null && tm.getTmClass() != null && tm.getApplicationNo() != null
					&& tm.getApplicationDate() != null && tm.getHeadOffice() != null && tm.getJournalNo() != null) {
				validTms.add(tm);
			}else {
				errors.add(tm);
			}
		}
		log.info("Going to save missing information to the json file");
		writeErrorsToJson(errors);
		return validTms;
	}



	private void writeErrorsToJson(List<PublishedTmDTO> errors) {
		ObjectMapper mapper = new ObjectMapper();
		String jsonDir = Paths.get(baseJsonDirectory).toAbsolutePath().toString();
		String fileName  = new Date().getTime() + "-errors.json";
		File file = new File(Paths.get(jsonDir , fileName).toString());
		 try {  
		        mapper.writeValue(file, errors );

		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }
		
	}

}



