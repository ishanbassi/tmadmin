package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.repository.extended.PublishedTmPhoneticsRepositoryExtended;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.TrademarkService;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;
import com.bassi.tmapp.service.dto.TrademarkSimiliarityResultDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MatchingTrademarkService {

    private final Logger log = LoggerFactory.getLogger(MatchingTrademarkService.class);

    private PublishedTmRepositoryExtended publishedTmRepositoryExtended;
    private PublishedTmServiceExtended publishedTmServiceExtended;
    private PublishedTmPhoneticsRepositoryExtended publishedTmPhoneticsRepositoryExtended;
    private final TrademarkService trademarkService;

    MatchingTrademarkService(
        PublishedTmRepositoryExtended publishedTmRepositoryExtended,
        PublishedTmServiceExtended publishedTmServiceExtended,
        PublishedTmPhoneticsRepositoryExtended publishedTmPhoneticsRepositoryExtended,
        TrademarkService trademarkService
    ) {
        this.publishedTmRepositoryExtended = publishedTmRepositoryExtended;
        this.publishedTmServiceExtended = publishedTmServiceExtended;
        this.publishedTmPhoneticsRepositoryExtended = publishedTmPhoneticsRepositoryExtended;
        this.trademarkService = trademarkService;
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

    public byte[] exportTrademarksV2(Integer journalNo) {
        MatchingTmExportServiceV2 fileExportedExportService = new MatchingTmExportServiceV2("Trademark Journal");
        List<TrademarkSimiliarityResultDTO> trademarkSimiliarityResultDTOs = trademarkService.runWeeklyComparison(journalNo);
        if (trademarkSimiliarityResultDTOs.isEmpty()) {
            return fileExportedExportService.export().toByteArray();
        }

        log.info("Found: " + trademarkSimiliarityResultDTOs.size() + " for Export.");
        fileExportedExportService.writeRecordToCurrentSheets(trademarkSimiliarityResultDTOs);
        return fileExportedExportService.export().toByteArray();
    }
}
