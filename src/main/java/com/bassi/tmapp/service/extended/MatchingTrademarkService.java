package com.bassi.tmapp.service.extended;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.PublishedTmService;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;


@Service
public class MatchingTrademarkService {
	
    private final Logger log = LoggerFactory.getLogger(MatchingTrademarkService .class);

	
	private PublishedTmRepositoryExtended publishedTmRepositoryExtended;
	private PublishedTmServiceExtended publishedTmServiceExtended;
	
	MatchingTrademarkService ( PublishedTmRepositoryExtended publishedTmRepositoryExtended ,PublishedTmServiceExtended publishedTmServiceExtended) {
		this.publishedTmRepositoryExtended  =  publishedTmRepositoryExtended ;
		this.publishedTmServiceExtended = publishedTmServiceExtended;
	}
	
	public byte[] exportTrademarks(Integer journalNo) {
		MatchingTmExportService fileExportedExportService = new MatchingTmExportService("Trademark Journal");
		 List<MatchingTrademarkDto> matchingTrademarkExportDtoList = publishedTmServiceExtended.findMatchingTrademarkByJournal(journalNo); 
        if (matchingTrademarkExportDtoList.isEmpty()) {
            return fileExportedExportService.export().toByteArray();
        }
		
		
        log.info("Found: " + matchingTrademarkExportDtoList.size() + " for Export.");
        fileExportedExportService.writeRecordToCurrentSheets(matchingTrademarkExportDtoList);
        return fileExportedExportService.export().toByteArray();
    }


	public List<Object> findAllMatchingTrademarksByClass(int tmClass, int journalNo) {
		return publishedTmRepositoryExtended.findAllMatchingTrademarksByJournalAndClass(journalNo, tmClass);
	}

}
