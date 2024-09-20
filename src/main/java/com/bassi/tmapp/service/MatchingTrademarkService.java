package com.bassi.tmapp.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;


@Service
public class MatchingTrademarkService {
	
    private final Logger log = LoggerFactory.getLogger(MatchingTrademarkService .class);

	
	private PublishedTmRepository publishedTmRepository;
	private PublishedTmService publishedTmService;
	
	MatchingTrademarkService ( PublishedTmRepository publishedTmRepository ,PublishedTmService publishedTmService) {
		this.publishedTmRepository  =  publishedTmRepository ;
		this.publishedTmService = publishedTmService;
	}
	
	public byte[] exportTrademarks(Integer journalNo) {
		MatchingTmExportService fileExportedExportService = new MatchingTmExportService("Trademark Journal");
		 List<MatchingTrademarkDto> matchingTrademarkExportDtoList = publishedTmService.findMatchingTrademarkByJournal(journalNo); 
        if (matchingTrademarkExportDtoList.isEmpty()) {
            return fileExportedExportService.export().toByteArray();
        }
		
		
        log.info("Found: " + matchingTrademarkExportDtoList.size() + " for Export.");
        fileExportedExportService.writeRecordToCurrentSheets(matchingTrademarkExportDtoList);
        return fileExportedExportService.export().toByteArray();
    }


	public List<Object> findAllMatchingTrademarksByClass(int tmClass, int journalNo) {
		return publishedTmRepository.findAllMatchingTrademarksByJournalAndClass(journalNo, tmClass);
	}

}
