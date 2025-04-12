package com.bassi.tmapp.web.rest.extended;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.PublishedTmQueryService;
import com.bassi.tmapp.service.criteria.PublishedTmCriteria;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.extended.PublishedTmServiceExtended;
import com.bassi.tmapp.web.rest.errors.BadRequestAlertException;
import java.io.IOException;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@RestController
@RequestMapping("/api/extended/published-tms")
public class PublishedTmResourceExtended {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmResourceExtended.class);

    private static final String ENTITY_NAME = "publishedTm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublishedTmServiceExtended publishedTmServiceExtended;

    private final PublishedTmRepository publishedTmRepository;

    private final PublishedTmQueryService publishedTmQueryService;

    public PublishedTmResourceExtended(
        PublishedTmServiceExtended publishedTmServiceExtended,
        PublishedTmRepository publishedTmRepository,
        PublishedTmQueryService publishedTmQueryService
    ) {
        this.publishedTmServiceExtended = publishedTmServiceExtended;
        this.publishedTmRepository = publishedTmRepository;
        this.publishedTmQueryService = publishedTmQueryService;
    }

    /**
     * {@code POST  /published-tms} : Create a new publishedTm.
     *
     * @param publishedTmDTO the publishedTmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publishedTmDTO, or with status {@code 400 (Bad Request)} if the publishedTm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PublishedTmDTO> createPublishedTm(@RequestBody PublishedTmDTO publishedTmDTO) throws URISyntaxException {
        log.debug("REST request to save PublishedTm : {}", publishedTmDTO);
        if (publishedTmDTO.getId() != null) {
            throw new BadRequestAlertException("A new publishedTm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        publishedTmDTO = publishedTmServiceExtended.save(publishedTmDTO);
        return ResponseEntity.created(new URI("/api/published-tms/" + publishedTmDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, publishedTmDTO.getId().toString()))
            .body(publishedTmDTO);
    }

    /**
     * {@code PUT  /published-tms/:id} : Updates an existing publishedTm.
     *
     * @param id the id of the publishedTmDTO to save.
     * @param publishedTmDTO the publishedTmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTmDTO,
     * or with status {@code 400 (Bad Request)} if the publishedTmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publishedTmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublishedTmDTO> updatePublishedTm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTmDTO publishedTmDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PublishedTm : {}, {}", id, publishedTmDTO);
        if (publishedTmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        publishedTmDTO = publishedTmServiceExtended.update(publishedTmDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTmDTO.getId().toString()))
            .body(publishedTmDTO);
    }

    /**
     * {@code PATCH  /published-tms/:id} : Partial updates given fields of an existing publishedTm, field will ignore if it is null
     *
     * @param id the id of the publishedTmDTO to save.
     * @param publishedTmDTO the publishedTmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTmDTO,
     * or with status {@code 400 (Bad Request)} if the publishedTmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the publishedTmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the publishedTmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PublishedTmDTO> partialUpdatePublishedTm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTmDTO publishedTmDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PublishedTm partially : {}, {}", id, publishedTmDTO);
        if (publishedTmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PublishedTmDTO> result = publishedTmServiceExtended.partialUpdate(publishedTmDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTmDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /published-tms} : get all the publishedTms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishedTms in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PublishedTm>> getAllPublishedTms(
        PublishedTmCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get PublishedTms by criteria: {}", criteria);

        Page<PublishedTm> page = publishedTmQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /published-tms/count} : count all the publishedTms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPublishedTms(PublishedTmCriteria criteria) {
        log.debug("REST request to count PublishedTms by criteria: {}", criteria);
        return ResponseEntity.ok().body(publishedTmQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /published-tms/:id} : get the "id" publishedTm.
     *
     * @param id the id of the publishedTmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publishedTmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublishedTmDTO> getPublishedTm(@PathVariable("id") Long id) {
        log.debug("REST request to get PublishedTm : {}", id);
        Optional<PublishedTmDTO> publishedTmDTO = publishedTmServiceExtended.findOne(id);
        return ResponseUtil.wrapOrNotFound(publishedTmDTO);
    }

    /**
     * {@code DELETE  /published-tms/:id} : delete the "id" publishedTm.
     *
     * @param id the id of the publishedTmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublishedTm(@PathVariable("id") Long id) {
        log.debug("REST request to delete PublishedTm : {}", id);
        publishedTmServiceExtended.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/extract/{journalNo}")
    public String extractPublishedTm(@PathVariable("journalNo") String journalNo) {
        publishedTmServiceExtended.readPdfFile(journalNo);
        return "Trademarks extraction has been initialized";
    }

    @PostMapping("/generate-phonetics/{journalNo}")
    public String generateMissingPhonetics(@PathVariable("journalNo") int journalNo) {
        publishedTmServiceExtended.generateMissingPhonetics(journalNo);
        return "Phonetics generated for missing trademarks";
    }

    @DeleteMapping("/soft-delete/{journalNo}")
    public ResponseEntity<Void> softDeletePublishedTrademarksByJournalNo(@PathVariable("journalNo") int journalNo) {
        publishedTmServiceExtended.softDeleteByJournalNo(journalNo);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePublishedTrademarksByJournalNo(PublishedTmCriteria criteria) {
        publishedTmServiceExtended.deleteByJournalNo(criteria);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/process/trademark-extraction")
    public String processTrademarkExtraction() throws IOException {
        publishedTmServiceExtended.processTrademarkExtraction();
        return "Trademarks extraction has been initialized";
    }

    @PostMapping("/scrape/journal/{journalNo}")
    public String scapeJournalTrademarks(@PathVariable("journalNo") int journalNo) {
        publishedTmServiceExtended.scrapeJournalTrademarks(journalNo);
        return "Trademarks scraping has been initialized";
    }

    @PostMapping("/extract-scrape/{journalNo}")
    public String extractPublishedTmAndSrcapeImageMarks(@PathVariable("journalNo") int journalNo) {
        publishedTmServiceExtended.readAndscrapeJournalTrademarks(journalNo);
        return "Trademarks scraping has been initialized";
    }

    @PostMapping("/extract/specific")
    public String extractPublishedTm(@RequestParam("journalNo") String journalNo, @RequestParam("filePath") String filePath) {
        publishedTmServiceExtended.readPdfFileByPath(filePath, journalNo);
        return "Trademarks extraction has been initialized";
    }

    @PostMapping("/update-status/journal/{journalNo}")
    public String updateTrademarkStatusFromJournal(@PathVariable("journalNo") String journalNo) {
        publishedTmServiceExtended.updateTrademarkStatusFromJournal(journalNo);
        return "Trademark Status Updation has completed";
    }

    @PostMapping("/download/pdfs")
    public String downloadJournalPdfs(@RequestParam("start") Integer start, @RequestParam("end") Integer end) {
        publishedTmServiceExtended.downloadJournalPdfs(start, end);
        return "Trademarks extraction has been initialized";
    }
}
