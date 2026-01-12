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
            "Score",
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
                getApplicationNumber(element.getPublishedTradmark()),
                getAgentName(element.getPublishedTradmark()),
                getAgentAddress(element.getPublishedTradmark()),
                getProprietorName(element.getPublishedTradmark()),
                getProprietorAddress(element.getPublishedTradmark()),
                getJournalNumber(element.getPublishedTradmark()),
                getPageNumber(element.getPublishedTradmark()),
                Double.toString(element.getScore()),
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
        if (tm == null || tm.getTmClass() == null) {
            return "";
        }
        return tm.getTmClass().toString();
    }

    private String getDetails(Trademark tm) {
        if (tm == null || tm.getDetails() == null) {
            return "";
        }
        return tm.getDetails();
    }

    private String getApplicationNumber(Trademark tm) {
        if (tm == null || tm.getApplicationNo() == null) {
            return "";
        }
        return tm.getApplicationNo().toString();
    }

    private String getAgentName(Trademark tm) {
        if (tm == null || tm.getAgentName() == null) {
            return "";
        }
        return tm.getAgentName();
    }

    private String getAgentAddress(Trademark tm) {
        if (tm == null || tm.getAgentAddress() == null) {
            return "";
        }
        return tm.getAgentAddress();
    }

    private String getProprietorName(Trademark tm) {
        if (tm == null || tm.getProprietorName() == null) {
            return "";
        }
        return tm.getProprietorName();
    }

    private String getProprietorAddress(Trademark tm) {
        if (tm == null || tm.getProprietorAddress() == null) {
            return "";
        }
        return tm.getProprietorAddress();
    }

    private String getJournalNumber(Trademark tm) {
        if (tm == null || tm.getJournalNo() == null) {
            return "";
        }
        return tm.getJournalNo().toString();
    }

    private String getPageNumber(Trademark tm) {
        if (tm == null || tm.getPageNo() == null) {
            return "";
        }
        return tm.getPageNo().toString();
    }
}
