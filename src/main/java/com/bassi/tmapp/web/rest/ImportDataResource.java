package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.service.ImportDataServiceExtended;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/import")
public class ImportDataResource {

    private static final Logger log = LoggerFactory.getLogger(ImportDataResource.class);

    private final ImportDataServiceExtended importDataService;

    ImportDataResource(ImportDataServiceExtended importDataService) {
        this.importDataService = importDataService;
    }

    @PostMapping("/trademark/read")
    public ResponseEntity<String> readTrademarksFromExcel(@RequestParam("file") MultipartFile file) {
        log.info("Going to read trademark from csv  file");
        importDataService.importTrademarks(file);
        return ResponseEntity.ok("All Trademarks read from the excel file.");
    }
}
