package com.bassi.tmapp.web.rest.extended;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.TrademarkQueryService;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.extended.TrademarkServiceExtended;
import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;
import com.bassi.tmapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bassi.tmapp.domain.Trademark}.
 */
@RestController
@RequestMapping("/api/extended/test")
public class TestResourceExtended {

    private ITextPdfReaderService iTextPdfReaderService;

    TestResourceExtended(ITextPdfReaderService iTextPdfReaderService) {
        this.iTextPdfReaderService = iTextPdfReaderService;
    }

    @GetMapping("/pdfs/tm-class")
    public void processTmClass(@RequestParam("pdfPath") String pdfPath) {
        iTextPdfReaderService.readPdfAndProcessTmClasses(pdfPath);
    }

    @GetMapping("/pdfs/leads")
    public void processLeads(@RequestParam("pdfPath") String pdfPath) {
        iTextPdfReaderService.readPdfAndProcessLeads(pdfPath);
    }
}
