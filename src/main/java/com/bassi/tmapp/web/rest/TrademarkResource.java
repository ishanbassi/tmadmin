package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.TrademarkQueryService;
import com.bassi.tmapp.service.TrademarkService;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
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
import org.springframework.web.multipart.MultipartFile;
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

    private static final Logger log = LoggerFactory.getLogger(TrademarkResource.class);

    private static final String ENTITY_NAME = "trademark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkService trademarkService;

    private final TrademarkRepository trademarkRepository;

    private final TrademarkQueryService trademarkQueryService;

    public TrademarkResource(
        TrademarkService trademarkService,
        TrademarkRepository trademarkRepository,
        TrademarkQueryService trademarkQueryService
    ) {
        this.trademarkService = trademarkService;
        this.trademarkRepository = trademarkRepository;
        this.trademarkQueryService = trademarkQueryService;
    }

    /**
     * {@code POST  /trademarks} : Create a new trademark.
     *
     * @param trademark the trademark to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademark, or with status {@code 400 (Bad Request)} if the trademark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Trademark> createTrademark(@RequestBody Trademark trademark) throws URISyntaxException {
        log.debug("REST request to save Trademark : {}", trademark);
        if (trademark.getId() != null) {
            throw new BadRequestAlertException("A new trademark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademark = trademarkService.save(trademark);
        return ResponseEntity.created(new URI("/api/trademarks/" + trademark.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademark.getId().toString()))
            .body(trademark);
    }

    /**
     * {@code PUT  /trademarks/:id} : Updates an existing trademark.
     *
     * @param id the id of the trademark to save.
     * @param trademark the trademark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademark,
     * or with status {@code 400 (Bad Request)} if the trademark is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Trademark> updateTrademark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trademark trademark
    ) throws URISyntaxException {
        log.debug("REST request to update Trademark : {}, {}", id, trademark);
        if (trademark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademark = trademarkService.update(trademark);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademark.getId().toString()))
            .body(trademark);
    }

    /**
     * {@code PATCH  /trademarks/:id} : Partial updates given fields of an existing trademark, field will ignore if it is null
     *
     * @param id the id of the trademark to save.
     * @param trademark the trademark to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademark,
     * or with status {@code 400 (Bad Request)} if the trademark is not valid,
     * or with status {@code 404 (Not Found)} if the trademark is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademark couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Trademark> partialUpdateTrademark(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Trademark trademark
    ) throws URISyntaxException {
        log.debug("REST request to partial update Trademark partially : {}, {}", id, trademark);
        if (trademark.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademark.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Trademark> result = trademarkService.partialUpdate(trademark);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademark.getId().toString())
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
    public ResponseEntity<List<Trademark>> getAllTrademarks(
        TrademarkCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Trademarks by criteria: {}", criteria);

        Page<Trademark> page = trademarkQueryService.findByCriteria(criteria, pageable);
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
        log.debug("REST request to count Trademarks by criteria: {}", criteria);
        return ResponseEntity.ok().body(trademarkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trademarks/:id} : get the "id" trademark.
     *
     * @param id the id of the trademark to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademark, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Trademark> getTrademark(@PathVariable("id") Long id) {
        log.debug("REST request to get Trademark : {}", id);
        Optional<Trademark> trademark = trademarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademark);
    }

    /**
     * {@code DELETE  /trademarks/:id} : delete the "id" trademark.
     *
     * @param id the id of the trademark to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademark(@PathVariable("id") Long id) {
        log.debug("REST request to delete Trademark : {}", id);
        trademarkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    
}
