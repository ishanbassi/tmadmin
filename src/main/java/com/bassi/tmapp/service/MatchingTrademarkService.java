package com.bassi.tmapp.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bassi.tmapp.repository.MatchingTmRepository;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.dto.MatchingTrademarktDto;


@Service
public class MatchingTrademarkService {
	
    private final Logger log = LoggerFactory.getLogger(MatchingTrademarkService .class);

	
	private PublishedTmRepository publishedTmRepository;
	
	MatchingTrademarkService ( PublishedTmRepository publishedTmRepository ) {
		this.publishedTmRepository  =  publishedTmRepository ;
	}
	
	public byte[] exportTrademarks( List<MatchingTrademarktDto> matchingTrademarkExportDtoList) {
		MatchingTmExportService fileExportedExportService = new MatchingTmExportService("Trademark Journal");
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
