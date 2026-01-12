package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.HeadOffice;
import com.bassi.tmapp.domain.enumeration.TrademarkStatus;
import com.bassi.tmapp.service.CSVExportService;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;
import com.bassi.tmapp.service.dto.TrademarkSimiliarityResultDTO;

public class MatchingTmExportServiceV2 extends CSVExportService<TrademarkSimiliarityResultDTO> {

    public MatchingTmExportServiceV2(String sheetName) {
        super(sheetName);
    }

    public MatchingTmExportServiceV2(boolean hasHeader, String sheetName) {
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
            "Page No.",
        };
        csvWriter.writeNext(headers);
    }

    @Override
    public void writeRow(TrademarkSimiliarityResultDTO element, int rowNr) {
        csvWriter.writeNext(
            new String[] {
                getTrademarkName(element.getPublishedTradmark()),
                getTrademarkName(element.getClientTrademark()),
                getTrademarkClass(element.getPublishedTradmark()),
                getDetails(element.getPublishedTradmark()),
                getApplicationNumber(element.getClientTrademark()),
                getAgentName(element.getClientTrademark()),
                getAgentAddress(element.getClientTrademark()),
                getProprietorName(element.getClientTrademark()),
                getProprietorAddress(element.getClientTrademark()),
                getJournalNumber(element.getClientTrademark()),
                getPageNumber(element.getClientTrademark()),
            }
        );
    }

    private String getTrademarkName(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getName();
    }

    private String getTrademarkClass(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getTmClass().toString();
    }

    private String getDetails(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getDetails().toString();
    }

    private String getApplicationNumber(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getApplicationNo().toString();
    }

    private String getAgentName(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getAgentName().toString();
    }

    private String getAgentAddress(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getAgentAddress().toString();
    }

    private String getProprietorName(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getAgentName().toString();
    }

    private String getProprietorAddress(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getProprietorAddress().toString();
    }

    private String getJournalNumber(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getJournalNo().toString();
    }

    private String getPageNumber(Trademark tm) {
        if (tm == null) {
            return "";
        }
        return tm.getPageNo().toString();
    }
}
