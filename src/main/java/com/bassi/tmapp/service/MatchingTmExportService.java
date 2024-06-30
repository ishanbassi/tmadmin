package com.bassi.tmapp.service;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.service.dto.MatchingTrademarkExportDto;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;


public class MatchingTmExportService extends CSVExportService<MatchingTrademarkExportDto>{
	
	
	public MatchingTmExportService(String sheetName) {
		super(sheetName);
	}
	
	
	public MatchingTmExportService(boolean hasHeader , String sheetName) {
			super(hasHeader, sheetName);
	}

	@Override
	public void writeHeader() {
		String[] headers = {
				"Matching Trademark",
				"Registered Trademark",
				"Class",
				"Details",
				"Application Number",
				"Agent Name",
				"Agent Address",
				"Proprietor Name",
				"Proprietor Address",
				"Head Office",
				"Journal No.",
				"Usage"

		};
		csvWriter.writeNext(headers);
	}

	@Override
	public void writeRow( MatchingTrademarkExportDto element, int rowNr) {
		PublishedTmDTO publishedTmDTO = element.getPublishedTmDTO();
		TrademarkDTO trademarkDTO= element.getTrademarkDto();
		csvWriter.writeNext(new String[] {
				publishedTmDTO.getName(),
				trademarkDTO.getName(),
				publishedTmDTO.getClass().toString(),
				publishedTmDTO.getDetails(),
				publishedTmDTO.getApplicationNo().toString(),
				publishedTmDTO.getAgentName(),
				publishedTmDTO.getAgentAddress(),
				publishedTmDTO.getProprietorName(),
				publishedTmDTO.getProprietorAddress(),
				publishedTmDTO.getHeadOffice().toString(),
				publishedTmDTO.getJournalNo().toString(),
				publishedTmDTO.getUsage()
				
				
		});
	}


}
