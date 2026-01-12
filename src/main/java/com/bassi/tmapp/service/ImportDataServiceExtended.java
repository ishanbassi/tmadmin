package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.extended.PhoneticsServiceExtended;
import com.bassi.tmapp.service.extended.TrademarkServiceExtended;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImportDataServiceExtended {

    private TrademarkServiceExtended trademarkServiceExtended;
    private PhoneticsServiceExtended phoneticsServiceExtended;
    private final TrademarkRepository trademarkRepository;
    private TrademarkService trademarkService;

    ImportDataServiceExtended(
        TrademarkServiceExtended trademarkServiceExtended,
        PhoneticsServiceExtended phoneticsServiceExtended,
        TrademarkRepository trademarkRepository,
        TrademarkService trademarkService
    ) {
        this.trademarkServiceExtended = trademarkServiceExtended;
        this.phoneticsServiceExtended = phoneticsServiceExtended;
        this.trademarkRepository = trademarkRepository;
        this.trademarkService = trademarkService;
    }

    private final Logger log = LoggerFactory.getLogger(ImportDataServiceExtended.class);

    public void importTrademarks(MultipartFile file) {
        if (file == null) {
            log.error("Failed to import trademarks, file is null");
            throw new InternalAuthenticationServiceException("Provide a valid csv file with trademarks");
        }

        importTrademarksFromCSVFile(file);
    }

    private void importTrademarksFromCSVFile(MultipartFile file) {
        List<Trademark> trademarks = new ArrayList<>();
        Map<Integer, String> errors = new HashMap<>();
        try {
            int row = 1;
            Reader reader = new InputStreamReader(file.getInputStream());
            try (CSVReader csvReader = new CSVReader(reader)) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    trademarks.add(createTrademarkFromCsvLine(line, errors, row));
                    row = row + 1;
                }
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Failed to read or parse CSV file", e);
            errors.put(errors.size(), e.getMessage());
            e.printStackTrace();
        }
        saveTrademarksAndGenerateTokens(trademarks);
    }

    private void saveTrademarksAndGeneratePhonetics(List<Trademark> trademarks) {
        List<Trademark> savedTrademarks = trademarkRepository.saveAll(trademarks);
        phoneticsServiceExtended.saveAll(savedTrademarks);
    }

    private void saveTrademarksAndGenerateTokens(List<Trademark> trademarks) {
        for (Trademark tm : trademarks) {
            trademarkService.saveTrademarksAndGenerateTokens(tm, TrademarkSource.EXCEL);
        }
    }

    private Trademark createTrademarkFromCsvLine(String[] line, Map<Integer, String> errors, int row) {
        try {
            Trademark trademark = new Trademark();
            trademark.setName(line[2]);
            Long applicationNo = extractNumbers(line[1]);
            trademark.setApplicationNo(applicationNo);

            Integer tmClass = Integer.valueOf(line[1]);
            trademark.setTmClass(tmClass);
            trademark.setAgentName("BASSI AND ASSOCIATES");

            return trademark;
        } catch (Exception e) {
            log.error("Error at line no. {} , reason: {}", row, e.getMessage());
            errors.put(row, e.getMessage());
            return null;
        }
    }

    public static Long extractNumbers(String value) {
        if (value == null) return null;
        return Long.parseLong(value.replaceAll("\\D+", ""));
    }
}
