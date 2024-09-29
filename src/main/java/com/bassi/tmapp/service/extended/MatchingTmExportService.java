package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.service.CSVExportService;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;


public class MatchingTmExportService extends CSVExportService<MatchingTrademarkDto>{
	
	
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
				"Journal No.",

		};
		csvWriter.writeNext(headers);
	}

	@Override
	public void writeRow( MatchingTrademarkDto element, int rowNr) {
;
		
		csvWriter.writeNext(new String[] {
				element.getMatchingTrademark(),
				element.getRegisteredTrademark(),
				element.getTmClass().toString(),
				element.getDetails(),
				getApplicationNo(element.getApplicationNo()),
				element.getAgentName(),
				element.getAgentAddress(),
				element.getProprietorName(),
				element.getProprietorAddress(),
				element.getJournalNo().toString(),
				
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
