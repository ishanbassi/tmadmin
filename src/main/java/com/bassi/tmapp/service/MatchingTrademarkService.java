package com.bassi.tmapp.service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

	import com.bassi.tmapp.service.dto.MatchingTrademarktDto;


@Service
public class MatchingTrademarkService {
	
    private final Logger log = LoggerFactory.getLogger(MatchingTrademarkService .class);

	
//	private MatchingTmExportService MatchingTmExportService;
//	
//	MatchingTrademarkService (MatchingTmExportService MatchingTmExportService) {
//		this.MatchingTmExportService = MatchingTmExportService;
//	}
	
	public byte[] exportTrademarks( List<MatchingTrademarktDto> matchingTrademarkExportDtoList) {
		MatchingTmExportService fileExportedExportService = new MatchingTmExportService("Trademark Journal");
        if (matchingTrademarkExportDtoList.isEmpty()) {
            return fileExportedExportService.export().toByteArray();
        }
		
		
        log.info("Found: " + matchingTrademarkExportDtoList.size() + " for Export.");
        fileExportedExportService.writeRecordToCurrentSheets(matchingTrademarkExportDtoList);
        return fileExportedExportService.export().toByteArray();
    }

}
