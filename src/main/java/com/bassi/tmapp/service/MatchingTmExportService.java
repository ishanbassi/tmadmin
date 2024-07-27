package com.bassi.tmapp.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.service.dto.MatchingTrademarktDto;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import static java.util.Map.entry;



public class MatchingTmExportService extends CSVExportService<MatchingTrademarktDto>{
	
	
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
				"Usage",
				"Trademark Status"

		};
		csvWriter.writeNext(headers);
	}

	@Override
	public void writeRow( MatchingTrademarktDto element, int rowNr) {
		PublishedTmDTO publishedTmDTO = element.getPublishedTmDTO();
		TrademarkDTO trademarkDTO= element.getTrademarkDto();
		csvWriter.writeNext(new String[] {
				publishedTmDTO.getName(),
				trademarkDTO.getName(),
				publishedTmDTO.getClass().toString(),
				publishedTmDTO.getDetails(),
				getApplicationNo(publishedTmDTO.getApplicationNo()),
				publishedTmDTO.getAgentName(),
				publishedTmDTO.getAgentAddress(),
				publishedTmDTO.getProprietorName(),
				publishedTmDTO.getProprietorAddress(),
				getHeadOffice(publishedTmDTO.getHeadOffice()),
				publishedTmDTO.getJournalNo().toString(),
				publishedTmDTO.getUsage(),
				getTrademarkStatus(publishedTmDTO.getTrademarkStatus())
				
				
		});
	}

	private String getHeadOffice(HeadOffice headOffice) {
		if(headOffice == null) {
			return "";
		}
		return headOffice.name();
	}


	private String getTrademarkStatus(TrademarkStatus trademarkStatus) {
		if(trademarkStatus == null) {
			return "";
		}
		return trademarkStatus.name();
	}
	
	private String getApplicationNo(Long applicationNo) {
		if(applicationNo == null) {
			return "";
		}
		return applicationNo.toString();
	}


}
