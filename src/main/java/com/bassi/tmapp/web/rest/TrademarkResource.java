package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.DocumentsService;
import com.bassi.tmapp.service.TrademarkQueryService;
import com.bassi.tmapp.service.TrademarkService;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkOrderSummary;
import com.bassi.tmapp.service.extended.dto.TrademarkWithLogoDto;
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
@RequestMapping("/api/trademarks")
public class TrademarkResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkResource.class);

    private static final String ENTITY_NAME = "trademark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkService trademarkService;

    private final TrademarkRepository trademarkRepository;

    private final TrademarkQueryService trademarkQueryService;

    private final DocumentsService documentsService;

    public TrademarkResource(
        TrademarkService trademarkService,
        TrademarkRepository trademarkRepository,
        TrademarkQueryService trademarkQueryService,
        DocumentsService documentsService
    ) {
        this.trademarkService = trademarkService;
        this.trademarkRepository = trademarkRepository;
        this.trademarkQueryService = trademarkQueryService;
        this.documentsService = documentsService;
    }

    /**
     * {@code POST  /trademarks} : Create a new trademark.
     *
     * @param trademarkDTO the trademarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademarkDTO, or with status {@code 400 (Bad Request)} if the trademark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrademarkDTO> createTrademark(@RequestBody TrademarkDTO trademarkDTO) throws URISyntaxException {
        LOG.debug("REST request to save Trademark : {}", trademarkDTO);
        if (trademarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new trademark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademarkDTO = trademarkService.save(trademarkDTO);
        return ResponseEntity.created(new URI("/api/trademarks/" + trademarkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademarkDTO.getId().toString()))
            .body(trademarkDTO);
    }

    /**
     * {@code PUT  /trademarks/:id} : Updates an existing trademark.
     *
     * @param id the id of the trademarkDTO to save.
     * @param trademarkDTO the trademarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrademarkDTO> updateTrademark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkDTO trademarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Trademark : {}, {}", id, trademarkDTO);
        if (trademarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademarkDTO = trademarkService.update(trademarkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkDTO.getId().toString()))
            .body(trademarkDTO);
    }

    /**
     * {@code PATCH  /trademarks/:id} : Partial updates given fields of an existing trademark, field will ignore if it is null
     *
     * @param id the id of the trademarkDTO to save.
     * @param trademarkDTO the trademarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trademarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrademarkDTO> partialUpdateTrademark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkDTO trademarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Trademark partially : {}, {}", id, trademarkDTO);
        if (trademarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkDTO> result = trademarkService.partialUpdate(trademarkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trademarks} : get all the trademarks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trademarks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrademarkDTO>> getAllTrademarks(
        TrademarkCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Trademarks by criteria: {}", criteria);

        Page<TrademarkDTO> page = trademarkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trademarks/count} : count all the trademarks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrademarks(TrademarkCriteria criteria) {
        LOG.debug("REST request to count Trademarks by criteria: {}", criteria);
        return ResponseEntity.ok().body(trademarkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trademarks/:id} : get the "id" trademark.
     *
     * @param id the id of the trademarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrademarkDTO> getTrademark(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Trademark : {}", id);
        Optional<TrademarkDTO> trademarkDTO = trademarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademarkDTO);
    }

    /**
     * {@code DELETE  /trademarks/:id} : delete the "id" trademark.
     *
     * @param id the id of the trademarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademark(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Trademark : {}", id);
        trademarkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PatchMapping(value = "/onboarding/{id}")
    public ResponseEntity<TrademarkDTO> partialUpdateTrademarkAndCreateLogoDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkWithLogoDto trademarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Trademark partially : {}, {}", id, trademarkDTO);
        if (trademarkDTO.getTrademark() == null || trademarkDTO.getTrademark().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkDTO.getTrademark().getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkDTO> result = trademarkService.partialUpdate(trademarkDTO.getTrademark());
        if (trademarkDTO.getFile() != null) {
            documentsService.saveDocumentAndSaveFile(trademarkDTO.getDocument(), trademarkDTO.getFile());
        }

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkDTO.getTrademark().getId().toString())
        );
    }

    /**
     * {@code GET  /trademarks/:id} : get the "id" trademark with LOGO Document.
     *
     * @param id the id of the trademarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/logo/{id}")
    public ResponseEntity<TrademarkWithLogoDto> getTrademarkWithLogoDocument(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Trademark : {}", id);
        TrademarkWithLogoDto trademarkWithLogoDto = trademarkService.findOneWithLogo(id);
        return ResponseEntity.ok().body(trademarkWithLogoDto);
    }

    @GetMapping("/current-user")
    public ResponseEntity<List<TrademarkDTO>> getTrademarkForCurrentUser(
        @RequestParam(value = "documents", defaultValue = "false") Boolean documents
    ) {
        List<TrademarkDTO> trademarks = trademarkService.getTrademarkForCurrentUser(documents);
        return ResponseEntity.ok().body(trademarks);
    }
}
