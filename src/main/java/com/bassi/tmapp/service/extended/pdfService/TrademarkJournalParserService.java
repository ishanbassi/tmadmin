package com.bassi.tmapp.service.extended.pdfService;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.LeadService;
import com.bassi.tmapp.service.TrademarkClassService;
import com.bassi.tmapp.service.TrademarkService;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.extended.LeadServiceExtended;
import com.bassi.tmapp.service.extended.PhoneticsServiceExtended;
import com.bassi.tmapp.service.extended.PublishedTmPhoneticsServiceExtended;
import com.bassi.tmapp.service.extended.TmAgentServiceExtended;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.mapper.JournalTrademarkMapper;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.bassi.tmapp.service.mapper.TrademarkClassMapper;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrademarkJournalParserService {

    private static final Logger log = LoggerFactory.getLogger(TrademarkJournalParserService.class);

    private static final Pattern tmClassPattern = Pattern.compile(
        "Trade Marks Journal No:\\s*(\\d{4})\\s*,\\s+.+Class\\s+(\\d{1,2})",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern applicationInfoPattern = Pattern.compile(
        "(\\d{5,7})[\\s\\u00A0]+(\\d{2}/\\d{2}/\\d{4})",
        Pattern.CASE_INSENSITIVE
    );
    private static final Pattern multiTmClassPattern = Pattern.compile("Cl.(\\d{1,2});", Pattern.CASE_INSENSITIVE);

    private PublishedTmDTO currentPublishedTmDto;
    private PhoneticsServiceExtended phoneticsServiceExtended;
    private PublishedTmRepositoryExtended publishedTmRepositoryExtended;
    private PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended;
    private WordSanitizationService wordSanitizationService;
    private TmAgentServiceExtended agentServiceExtended;
    private TrademarkClassService trademarkClassService;
    private final TrademarkClassMapper trademarkClassMapper;
    private CompanyDataProcessor companyDataProcessor;
    private LeadService leadService;
    private TrademarkService trademarkService;

    @Autowired
    @Lazy
    private TrademarkJournalParserService self;

    @Value("${file-upload-base-path}")
    private String baseUploadDirectory;

    @Value("${pdf-file-base-path}")
    private String basePdfDirectory;

    @Value("${errors-file-base-path}")
    private String baseErrorsDirectory;

    private PublishedTmMapper publishedTmMapper;
    private JournalTrademarkMapper journalTrademarkMapper;

    public TrademarkJournalParserService(
        PhoneticsServiceExtended phoneticsServiceExtended,
        PublishedTmRepositoryExtended publishedTmRepositoryExtended,
        PublishedTmMapper publishedTmMapper,
        PublishedTmPhoneticsServiceExtended publishedTmPhoneticsServiceExtended,
        WordSanitizationService wordSanitizationService,
        TmAgentServiceExtended agentServiceExtended,
        TrademarkClassService trademarkClassService,
        TrademarkClassMapper trademarkClassMapper,
        CompanyDataProcessor companyDataProcessor,
        LeadService leadService,
        JournalTrademarkMapper journalTrademarkMapper,
        TrademarkService trademarkService
    ) {
        this.phoneticsServiceExtended = phoneticsServiceExtended;
        this.publishedTmRepositoryExtended = publishedTmRepositoryExtended;
        this.publishedTmMapper = publishedTmMapper;
        this.publishedTmPhoneticsServiceExtended = publishedTmPhoneticsServiceExtended;
        this.wordSanitizationService = wordSanitizationService;
        this.agentServiceExtended = agentServiceExtended;
        this.trademarkClassService = trademarkClassService;
        this.trademarkClassMapper = trademarkClassMapper;
        this.companyDataProcessor = companyDataProcessor;
        this.leadService = leadService;
        this.journalTrademarkMapper = journalTrademarkMapper;
        this.trademarkService = trademarkService;
    }

    @Async
    public void readPdfFilesFromFileSystem(String journalNo) {
        File baseDirectory = new File(Paths.get(basePdfDirectory).toAbsolutePath().toString() + "/" + journalNo);

        processDirectory(baseDirectory);
    }

    public void readPdfAndProcessTmClasses(String pdfDirectoryPath) {
        File baseDirectory = new File(Paths.get(basePdfDirectory).toAbsolutePath().toString() + "/" + pdfDirectoryPath);
        File[] files = fetchFilesFromDirectory(baseDirectory);
        for (File file : files) {
            Pattern pattern = Pattern.compile("-(\\d{1,2})\\.pdf$");
            Matcher matcher = pattern.matcher(file.getAbsolutePath());
            if (matcher.find()) {
                int tmClass = Integer.parseInt(matcher.group(1));
                processTmClassPdf(file.getAbsolutePath(), tmClass);
                continue;
            }

            processTmClassPdf(file.getAbsolutePath(), null);
        }
    }

    private String getValueOrNull(List<String> list, int i) {
        try {
            return list.get(i);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    private void processTmClassPdf(String pdfFilePath, Integer tmClass) {
        PdfDocument pdfDoc;
        List<TrademarkClassDTO> trademarkClassDTOs = new ArrayList<>();
        try {
            pdfDoc = new PdfDocument(new PdfReader(pdfFilePath));
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                CustomTextExtractionStrategy strategy = new CustomTextExtractionStrategy();
                PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);
                processor.processPageContent(pdfDoc.getPage(i));

                List<LineInfo> lines = strategy.getLines();
                for (LineInfo line : lines) {
                    String words = line.getAllWordsFromSameLineWithInfo();
                    words = words.replaceAll("[\\u00A0\\u2007\\u202F]", " "); // replaces non-breaking spaces
                    words = words.trim(); // removes leading/trailing space

                    Pattern pattern = Pattern.compile("^\\s*(\\d{6})\\s+(.+)$");
                    Matcher matcher = pattern.matcher(words);

                    if (matcher.find()) {
                        String basicNo = matcher.group(1);
                        String indication = matcher.group(2);
                        TrademarkClassDTO trademarkClass = new TrademarkClassDTO();
                        trademarkClass.setCode(Integer.valueOf(basicNo));
                        trademarkClass.setKeyword(indication);
                        trademarkClass.setTmClass(tmClass);
                        trademarkClassDTOs.add(trademarkClass);
                    }
                }
            }
            pdfDoc.close();
        } catch (IOException e) {
            throw new InternalServerAlertException("Unable to read pdf file, " + pdfFilePath + " Reason: " + e.getLocalizedMessage());
        }
        trademarkClassService.saveAll(trademarkClassDTOs);
    }

    private File[] fetchFilesFromDirectory(File directory) {
        if (directory == null || !directory.exists()) return new File[0];
        return directory.listFiles();
    }

    private void processDirectory(File directory) {
        if (directory == null || !directory.exists()) return;

        File[] files = directory.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory()) {
                processDirectory(file); // Recursively process subdirectories
            } else if (file.getName().toLowerCase().endsWith(".pdf")) {
                self.readPdfAndSaveDetails(file.getAbsolutePath()); // Process the PDF
                //                self.updateTrademarkStatusFromJournal(file.getAbsolutePath());
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void readPdfAndSaveDetails(String path) {
        List<PublishedTmDTO> publishedTrademarksDto = readPdf(path);
        List<Trademark> trademarks = journalTrademarkMapper.toEntity(publishedTrademarksDto);
        for (Trademark tm : trademarks) {
            try {
                trademarkService.saveTrademarksAndGenerateTokens(tm, TrademarkSource.JOURNAL_PUBLICATION);
            } catch (Exception e) {
                log.error("Failed to save trademark, Reason: {}", e.getLocalizedMessage());
            }
        }
    }

    public List<PublishedTmDTO> readPdf(String pdfFilePath) {
        log.info("Going to read pdf file: {}", pdfFilePath);
        List<PublishedTmDTO> publishedTrademarks = new ArrayList<>();

        try (PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdfFilePath))) {
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                try {
                    // check if international tm begins
                    PdfPage page = pdfDoc.getPage(i);
                    String pageContent = PdfTextExtractor.getTextFromPage(page);
                    if (pageContent.contains("International Registration designating India")) {
                        return publishedTrademarks;
                    }
                    currentPublishedTmDto = new PublishedTmDTO();
                    currentPublishedTmDto.setSource(TrademarkSource.JOURNAL_PUBLICATION);
                    currentPublishedTmDto.setPageNo(i);

                    CustomTextExtractionStrategy strategy = new CustomTextExtractionStrategy();
                    PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);
                    processor.processPageContent(page);

                    //extract text
                    List<LineInfo> lines = strategy.getLines();
                    extractPublishedTrademark(lines);

                    // extract images
                    PdfImage pdfImage = strategy.getImage();
                    if (pdfImage != null) {
                        String path = saveToFileSystem(pdfImage);
                        currentPublishedTmDto.setImgUrl(path);
                    }

                    if (isInfoMissing(currentPublishedTmDto)) {
                        if (currentPublishedTmDto != null && currentPublishedTmDto.getImgUrl() != null) {
                            deleteTmImg(currentPublishedTmDto.getImgUrl());
                        }
                        if (currentPublishedTmDto != null) {
                            currentPublishedTmDto.setFilePath(pdfFilePath);
                        }
                        continue;
                    }

                    // if both image and trademark	 is present, remove the trademark
                    if (currentPublishedTmDto.getImgUrl() != null && (currentPublishedTmDto.getName() != null)) {
                        currentPublishedTmDto.setName(null);
                    }

                    // check if trademark is multi class
                    if (
                        currentPublishedTmDto.getTmClass() != null &&
                        currentPublishedTmDto.getTmClass() == 99 &&
                        currentPublishedTmDto.getDetails() != null
                    ) {
                        List<PublishedTmDTO> class99Trademarks = processTmClass99(currentPublishedTmDto)
                            .stream()
                            .filter(x -> !isInfoMissing(x))
                            .toList();
                        publishedTrademarks.addAll(class99Trademarks);
                    }
                    publishedTrademarks.add(currentPublishedTmDto);
                } catch (Exception e) {
                    log.error("Unable to page No : {} , Trademark Details: {}, Reason: {}", i, currentPublishedTmDto, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("Unable to read pdf file, " + pdfFilePath + " Reason: " + e.getLocalizedMessage());
        }

        return publishedTrademarks;
    }

    public void extractPublishedTrademark(List<LineInfo> lines) {
        extractJournalNoAndTrademarkClass(lines);
        extractApplicationNumberAndDate(lines);
        extractAgentNameAndAddress(lines);
        extractUsage(lines);
        extractTrademark(lines);
        extractProprietorNameAndAddress(lines);
        extractHeadOffice(lines);
        extractDetails(lines);
        extractAssociatedTm(lines);

        if (currentPublishedTmDto.getName() != null) {
            generatePhonetics(currentPublishedTmDto.getName());
        }
    }

    private void extractHeadOffice(List<LineInfo> lines) {
        if (currentPublishedTmDto.getTextIndexMap().containsKey(PublishedTmDTO.TRADEMARK_USAGE)) {
            for (LineInfo line : lines) {
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words != null) {
                    try {
                        HeadOffice headOffice = HeadOffice.valueOf(words.trim());
                        currentPublishedTmDto.setHeadOffice(headOffice);
                        currentPublishedTmDto.setTextIndexes(PublishedTmDTO.HEAD_OFFICE, lines.indexOf(line));
                    } catch (IllegalArgumentException e) {}
                }
            }
        }
    }

    private void extractAssociatedTm(List<LineInfo> lines) {
        Optional<LineInfo> associatedTmLine = lines
            .stream()
            .filter(line -> {
                String wordInfo = line.getAllWordsFromSameLineWithInfo();
                return wordInfo != null && wordInfo.contains("To be associated with");
            })
            .findFirst();

        if (associatedTmLine.isPresent()) {
            int associatedTmIdx = lines.indexOf(associatedTmLine.get());

            // second condition ensures that we don't surpass the length of the total lines of the pdf
            // because the position of the associated tms is not consistent across pages
            if (associatedTmIdx != -1 && lines.size() - associatedTmIdx >= 2) {
                LineInfo associatedTmsLineInfo = lines.get(associatedTmIdx + 1);
                String associatedTmsWordInfo = associatedTmsLineInfo.getAllWordsFromSameLineWithInfo();
                currentPublishedTmDto.setAssociatedTms(associatedTmsWordInfo);
            }
        }
    }

    private void extractUsage(List<LineInfo> lines) {
        if (currentPublishedTmDto.getUsage() == null) {
            for (LineInfo line : lines) {
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words != null && (words.contains("Proposed to be Used") || words.contains("Used Since"))) {
                    currentPublishedTmDto.setUsage(words);
                    currentPublishedTmDto.setTextIndexes(PublishedTmDTO.TRADEMARK_USAGE, lines.indexOf(line));
                    break;
                }
            }
        }
    }

    private void extractDetails(List<LineInfo> lines) {
        Map<String, Integer> textIdxMap = currentPublishedTmDto.getTextIndexMap();
        if (textIdxMap.containsKey(PublishedTmDTO.HEAD_OFFICE)) {
            int headOfficeIdx = textIdxMap.get(PublishedTmDTO.HEAD_OFFICE);
            Optional<String> details = lines
                .subList(headOfficeIdx + 1, lines.size())
                .stream()
                .map(LineInfo::getAllWordsFromSameLineWithInfo)
                .reduce((intialString, currentString) -> intialString + " " + currentString);

            if (details.isPresent()) {
                currentPublishedTmDto.setDetails(details.get());
            }
        }
    }

    private void extractAgentNameAndAddress(List<LineInfo> lines) {
        Optional<LineInfo> agentAddressLine = lines
            .stream()
            .filter(line -> {
                String wordInfo = line.getAllWordsFromSameLineWithInfo();
                return wordInfo != null && wordInfo.contains("Address for service in India");
            })
            .findFirst();
        if (agentAddressLine.isPresent()) {
            int agentAddressLineIdx = lines.indexOf(agentAddressLine.get());
            if (agentAddressLineIdx != -1) {
                LineInfo agentNameLine = lines.get(agentAddressLineIdx + 1);
                String agentName = agentNameLine.getAllWordsFromSameLineWithInfo();
                currentPublishedTmDto.setAgentName(agentName);
            }

            Optional<LineInfo> trademarkUsageLine = lines
                .stream()
                .filter(line -> {
                    String wordInfo = line.getAllWordsFromSameLineWithInfo();
                    return wordInfo != null && (wordInfo.contains("Proposed to be Used") || wordInfo.contains("Used Since"));
                })
                .findFirst();
            if (trademarkUsageLine.isPresent()) {
                int trademarkUsageIdx = lines.indexOf(trademarkUsageLine.get());

                Optional<String> agentAddress = lines
                    .subList(agentAddressLineIdx + 2, trademarkUsageIdx)
                    .stream()
                    .map(LineInfo::getAllWordsFromSameLineWithInfo)
                    .reduce((intialString, currentString) -> intialString + " " + currentString);
                if (agentAddress.isPresent()) {
                    currentPublishedTmDto.setAgentAddress(agentAddress.get());
                }
            }

            currentPublishedTmDto.setTextIndexes(PublishedTmDTO.AGENT_NAME_ADDRESS, agentAddressLineIdx);
        }
    }

    private void extractProprietorNameAndAddress(List<LineInfo> lines) {
        Map<String, Integer> textIndexMap = currentPublishedTmDto.getTextIndexMap();
        if (textIndexMap.containsKey(PublishedTmDTO.APPLICATION_NUMBER_DATE)) {
            int applicationNumberDateIdx = textIndexMap.get(PublishedTmDTO.APPLICATION_NUMBER_DATE);
            String proprietorName = lines.get(applicationNumberDateIdx + 1).getAllWordsFromSameLineWithInfo();
            currentPublishedTmDto.setProprietorName(proprietorName);

            int proprietorAddressMaxIdx = textIndexMap.containsKey(PublishedTmDTO.AGENT_NAME_ADDRESS)
                ? textIndexMap.get(PublishedTmDTO.AGENT_NAME_ADDRESS)
                : textIndexMap.get(PublishedTmDTO.TRADEMARK_USAGE);
            // increasing the fromIndex by 2 because it's inclusive and next index is for proprietor name
            Optional<String> proprietorAddressText = lines
                .subList(applicationNumberDateIdx + 2, proprietorAddressMaxIdx)
                .stream()
                .map(LineInfo::getAllWordsFromSameLineWithInfo)
                .reduce((intialString, currentString) -> intialString + " " + currentString);

            if (proprietorAddressText.isPresent()) {
                currentPublishedTmDto.setProprietorAddress(proprietorAddressText.get());
            }
        }
    }

    private void extractApplicationNumberAndDate(List<LineInfo> lines) {
        if (currentPublishedTmDto.getApplicationNo() == null && currentPublishedTmDto.getApplicationDate() == null) {
            for (LineInfo line : lines) {
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words != null) {
                    Matcher matcher = applicationInfoPattern.matcher(words);
                    boolean matched = matcher.find();
                    if (matched) {
                        Long applicationNo = Long.valueOf(matcher.group(1));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate applicationDate = LocalDate.parse(matcher.group(2), formatter);
                        currentPublishedTmDto.setApplicationNo(applicationNo);
                        currentPublishedTmDto.setApplicationDate(applicationDate);

                        currentPublishedTmDto.setTextIndexes(PublishedTmDTO.APPLICATION_NUMBER_DATE, lines.indexOf(line));
                        break;
                    }
                }
            }
        }
    }

    private void extractTrademark(List<LineInfo> lines) {
        Map<String, Integer> textIndexMap = currentPublishedTmDto.getTextIndexMap();
        if (textIndexMap.containsKey(PublishedTmDTO.TM_CLASS) && textIndexMap.containsKey(PublishedTmDTO.APPLICATION_NUMBER_DATE)) {
            List<LineInfo> subLine = lines.subList(
                textIndexMap.get(PublishedTmDTO.TM_CLASS) + 1,
                textIndexMap.get(PublishedTmDTO.APPLICATION_NUMBER_DATE)
            );
            for (LineInfo line : subLine) {
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words != null) {
                    if (words.contains("mark u/s 71(1)") || words.contains("\0")) {
                        continue;
                    }
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
        if (currentPublishedTmDto.getJournalNo() == null && currentPublishedTmDto.getTmClass() == null) {
            for (int idx = 0; idx < lines.size(); idx++) {
                LineInfo line = lines.get(idx);
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words != null) {
                    Matcher matcher = tmClassPattern.matcher(words);
                    boolean matched = matcher.find();
                    if (matched) {
                        int journalNo = Integer.parseInt(matcher.group(1));
                        int tmClass = Integer.parseInt(matcher.group(2));
                        currentPublishedTmDto.setJournalNo(journalNo);
                        currentPublishedTmDto.setTmClass(tmClass);
                        currentPublishedTmDto.setTextIndexes(PublishedTmDTO.TM_CLASS, idx);
                        break;
                    }
                }
            }
        }
    }

    private String saveToFileSystem(PdfImage pdfImage) {
        if (
            currentPublishedTmDto.getApplicationNo() == null ||
            currentPublishedTmDto.getTmClass() == null ||
            currentPublishedTmDto.getJournalNo() == null
        ) {
            return null;
        }
        byte[] content = pdfImage.getImageContent();
        String extensionType = pdfImage.getImageType();
        String applicationNumber = currentPublishedTmDto.getApplicationNo().toString();
        String tmClass = currentPublishedTmDto.getTmClass().toString();
        String journalNo = currentPublishedTmDto.getJournalNo().toString();
        String resourcesDir = Paths.get(baseUploadDirectory).toAbsolutePath().toString();
        String filePath = journalNo + "-" + tmClass + "-" + applicationNumber + "." + extensionType;
        Path newFile = Paths.get(resourcesDir, filePath);
        try {
            Files.createDirectories(newFile.getParent());
            Files.write(newFile, content);
        } catch (IOException e) {
            log.error("unable to save image, Reason: {}", e.getLocalizedMessage());
        }
        return filePath;
    }

    private void deleteTmImg(String imgUrl) {
        String resourcesDir = Paths.get(baseUploadDirectory).toAbsolutePath().toString();
        Path imgPath = Paths.get(String.join("/", resourcesDir, imgUrl));
        try {
            Files.delete(imgPath);
            log.info("File deleted successfully : {} ", imgUrl);
        } catch (IOException e) {
            log.error("Failed to delete the file, Reason: {}", e.getLocalizedMessage());
        }
    }

    public List<String> generatePhonetics(String trademark) {
        List<String> subWords = new ArrayList<>(Arrays.asList(trademark.split(" ")));
        subWords.add(trademark);
        return subWords.stream().map(word -> phoneticsServiceExtended.generatePhonetics(word)).toList();
    }

    private boolean isInfoMissing(PublishedTmDTO tm) {
        return !(
            tm != null &&
            tm.getTmClass() != null &&
            tm.getApplicationNo() != null &&
            tm.getApplicationDate() != null &&
            tm.getHeadOffice() != null &&
            tm.getJournalNo() != null &&
            (tm.getName() != null || tm.getImgUrl() != null)
        );
    }

    private void writeErrorsToJson(List<PublishedTmDTO> errors) {
        log.info("{} trademarks with missing information are going to be stored in the json file.", errors.size());
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonDir = Paths.get(baseErrorsDirectory).toAbsolutePath().toString();
        String fileName = new Date().getTime() + "-errors.json";
        File file = new File(Paths.get(jsonDir, fileName).toString());
        try {
            if (file.createNewFile()) {
                log.info("File created : {}", file.getName());
            }
            mapper.writeValue(file, errors);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<PublishedTmDTO> processTmClass99(PublishedTmDTO tm) {
        List<String> tmClasses = new ArrayList<>();
        List<PublishedTmDTO> class99Trademarks = new ArrayList<>();

        // this threshold is used to remove unwanted words in the details
        int subStringThreshold = 3;

        Matcher matcher = multiTmClassPattern.matcher(tm.getDetails());
        while (matcher.find()) {
            tmClasses.add(matcher.group(1));
            PublishedTmDTO trademarkDto = new PublishedTmDTO(tm);
            int idx = matcher.start(1);
            int tmClass = Integer.parseInt(matcher.group(1));
            trademarkDto.setTmClass(tmClass);

            // Save the end of the current match
            int endOfCurrentMatch = matcher.end();

            // Look for the next occurrence
            if (matcher.find()) {
                int nextTmClassIndex = matcher.start(1);
                String details = trademarkDto.getDetails().substring(idx + subStringThreshold, nextTmClassIndex - subStringThreshold);
                trademarkDto.setDetails(details);
            } else {
                String details = trademarkDto.getDetails().substring(idx + subStringThreshold);
                trademarkDto.setDetails(details);
            }
            class99Trademarks.add(trademarkDto);

            // Reset the matcher back to the end of the last match
            matcher.region(endOfCurrentMatch, tm.getDetails().length());
        }
        return class99Trademarks;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateTrademarkStatusFromJournal(String pdfFilePath) {
        PdfDocument pdfDoc;

        try {
            pdfDoc = new PdfDocument(new PdfReader(pdfFilePath));
        } catch (IOException e) {
            throw new InternalServerAlertException("Unable to read pdf file, " + pdfFilePath + " Reason: " + e.getLocalizedMessage());
        }
        for (int i = 1; i < pdfDoc.getNumberOfPages(); i++) {
            CustomTextExtractionStrategy strategy = new CustomTextExtractionStrategy();
            PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);
            processor.processPageContent(pdfDoc.getPage(i));

            String pageContent = PdfTextExtractor.getTextFromPage(pdfDoc.getPage(i));
            String registeredApplicationsExpectedPageContent =
                "Following Trade Mark applications have been Registered and registration certificates";
            String renewalApplicationsExpectedPageContent = "Following Trade Marks Registration Renewed for a Period Of Ten Years";
            if (
                !(pageContent.contains(renewalApplicationsExpectedPageContent) ||
                    pageContent.contains(registeredApplicationsExpectedPageContent))
            ) {
                continue;
            }

            List<LineInfo> lines = strategy.getLines();
            for (LineInfo line : lines) {
                String words = line.getAllWordsFromSameLineWithInfo();
                if (words == null) {
                    continue;
                }
                if (pageContent.contains(renewalApplicationsExpectedPageContent)) {
                    String regex = "^(\\d{5,7})\\s+(\\d{1,2})\\s+(\\d{2}/\\d{2}/\\d{4})";

                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(words);

                    if (matcher.find()) {
                        Long applicationNo = Long.valueOf(matcher.group(1));
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate renewalDate = LocalDate.parse(matcher.group(3), formatter);

                        List<PublishedTm> publishedTms = publishedTmRepositoryExtended.findTrademarksByApplicationNo(applicationNo);
                        for (PublishedTm publishedTm : publishedTms) {
                            publishedTm.setRenewalDate(renewalDate);
                            publishedTm.setTrademarkStatus("Renewed");
                            publishedTmRepositoryExtended.save(publishedTm);
                        }
                    }
                } else {
                    String regex = "(\\d{5,7})";
                    Pattern pattern = Pattern.compile(regex);
                    List<String> applicationNumbers = new ArrayList<>();

                    for (WordInfo word : line.getWords()) {
                        Matcher matcher = pattern.matcher(word.getText());
                        if (matcher.find()) {
                            applicationNumbers.add(matcher.group());
                        }
                    }
                    // convert the application numbers to long and then find the trademarks and
                    // update the status to registered
                    for (String applicationNumber : applicationNumbers) {
                        Long applicationNo = Long.valueOf(applicationNumber);
                        List<PublishedTm> publishedTms = publishedTmRepositoryExtended.findTrademarksByApplicationNo(applicationNo);
                        for (PublishedTm publishedTm : publishedTms) {
                            publishedTm.setTrademarkStatus("Registered");
                            publishedTmRepositoryExtended.save(publishedTm);
                        }
                    }
                }
            }
        }
        pdfDoc.close();
    }

    @Async
    public void readPdfFilesFromFileSystem(int start, int end) {
        for (int i = start; i <= end; i++) {
            Path path = Paths.get(basePdfDirectory, String.valueOf(i)).toAbsolutePath();
            File baseDirectory = path.toFile();

            if (baseDirectory.exists() && baseDirectory.isDirectory()) {
                log.info("Processing directory: {}", baseDirectory.getAbsolutePath());
                processDirectory(baseDirectory);

                // ✅ Give GC a chance every 10 journals
                if ((i - start) % 10 == 0) {
                    System.gc(); // hint to JVM
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {} // breathing room
                }
            } else {
                log.warn("Directory not found: {}", baseDirectory.getAbsolutePath());
            }
        }
    }
}
