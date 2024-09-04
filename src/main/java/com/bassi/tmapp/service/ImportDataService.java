package com.bassi.tmapp.service;

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

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import net.bytebuddy.asm.Advice.This;


@Service
public class ImportDataService {
	
	private TrademarkService trademarkService;
	private PhoneticsService phoneticsService;
    private final TrademarkRepository trademarkRepository;

	
	ImportDataService(TrademarkService trademarkService, PhoneticsService phoneticsService,TrademarkRepository trademarkRepository) {
		this.trademarkService = trademarkService;
		this.phoneticsService =  phoneticsService;
		this.trademarkRepository = trademarkRepository;
	}

	private final Logger log = LoggerFactory.getLogger(ImportDataService.class);
	
	
	public void  importTrademarks(MultipartFile file) {
		if(file == null) {
			log.error("Failed to import trademarks, file is null");
			throw new InternalAuthenticationServiceException("Provide a valid csv file with trademarks");
		}
		
		importTrademarksFromCSVFile(file);
	}


	private void importTrademarksFromCSVFile(MultipartFile file) {
		List<Trademark> trademarks  = new ArrayList<>();		
    	Map<Integer,String> errors = new HashMap<>();
		try {
	    	int row = 1;
			Reader  reader = new InputStreamReader(file.getInputStream());
			try (CSVReader csvReader = new CSVReader(reader)) {
				String[] line;
				while((line = csvReader.readNext()) != null) {
					trademarks.add(createTrademarkFromCsvLine(line,errors, row));
					row = row + 1;
				}
			}
		} catch (IOException | CsvValidationException e) {
            log.error("Failed to read or parse CSV file", e);
			errors.put(errors.size(), e.getMessage());
			e.printStackTrace();
		}
		saveTrademarksAndGeneratePhonetics(trademarks);
		

		
	}


	private void saveTrademarksAndGeneratePhonetics(List<Trademark> trademarks) {
		List<Trademark> savedTrademarks = trademarkRepository.saveAll(trademarks);
		phoneticsService.saveAll(savedTrademarks);
		
	}


	private Trademark createTrademarkFromCsvLine(String[] line,Map<Integer,String> errors, int row ) {
		try {
			Trademark trademark = new Trademark();
			trademark.setName(line[0]);
			Long applicationNo = Long.valueOf(line[1]);
			trademark.setApplicationNo(applicationNo);
			Integer tmClass = Integer.valueOf(line[2]);
			trademark.setTmClass(tmClass);
			return trademark;
		}
		catch(Exception e) {
			log.error("Error at line no. {} , reason: {}", row , e.getMessage());
			errors.put(row, e.getMessage());
			return null;
		}
		
		
//		 trademarkDto = trademarkService.save(trademarkDto);
//		 
//		 log.info("going to save phonetics for the trademark");
//		 PhoneticsDTO phoneticDto = new PhoneticsDTO();
//		 
		 
		
	}
        
}
