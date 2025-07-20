package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TrademarkClassRepository;
import com.bassi.tmapp.service.TrademarkClassQueryService;
import com.bassi.tmapp.service.TrademarkClassService;
import com.bassi.tmapp.service.criteria.TrademarkClassCriteria;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TrademarkClass}.
 */
@RestController
@RequestMapping("/api/trademark-classes")
public class TrademarkClassResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkClassResource.class);

    private static final String ENTITY_NAME = "trademarkClass";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkClassService trademarkClassService;

    private final TrademarkClassRepository trademarkClassRepository;

    private final TrademarkClassQueryService trademarkClassQueryService;

    public TrademarkClassResource(
        TrademarkClassService trademarkClassService,
        TrademarkClassRepository trademarkClassRepository,
        TrademarkClassQueryService trademarkClassQueryService
    ) {
        this.trademarkClassService = trademarkClassService;
        this.trademarkClassRepository = trademarkClassRepository;
        this.trademarkClassQueryService = trademarkClassQueryService;
    }

    /**
     * {@code POST  /trademark-classes} : Create a new trademarkClass.
     *
     * @param trademarkClassDTO the trademarkClassDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademarkClassDTO, or with status {@code 400 (Bad Request)} if the trademarkClass has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrademarkClassDTO> createTrademarkClass(@RequestBody TrademarkClassDTO trademarkClassDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrademarkClass : {}", trademarkClassDTO);
        if (trademarkClassDTO.getId() != null) {
            throw new BadRequestAlertException("A new trademarkClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademarkClassDTO = trademarkClassService.save(trademarkClassDTO);
        return ResponseEntity.created(new URI("/api/trademark-classes/" + trademarkClassDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademarkClassDTO.getId().toString()))
            .body(trademarkClassDTO);
    }

    /**
     * {@code PUT  /trademark-classes/:id} : Updates an existing trademarkClass.
     *
     * @param id the id of the trademarkClassDTO to save.
     * @param trademarkClassDTO the trademarkClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkClassDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkClassDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademarkClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrademarkClassDTO> updateTrademarkClass(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkClassDTO trademarkClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrademarkClass : {}, {}", id, trademarkClassDTO);
        if (trademarkClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademarkClassDTO = trademarkClassService.update(trademarkClassDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkClassDTO.getId().toString()))
            .body(trademarkClassDTO);
    }

    /**
     * {@code PATCH  /trademark-classes/:id} : Partial updates given fields of an existing trademarkClass, field will ignore if it is null
     *
     * @param id the id of the trademarkClassDTO to save.
     * @param trademarkClassDTO the trademarkClassDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkClassDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkClassDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trademarkClassDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademarkClassDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrademarkClassDTO> partialUpdateTrademarkClass(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkClassDTO trademarkClassDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrademarkClass partially : {}, {}", id, trademarkClassDTO);
        if (trademarkClassDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkClassDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkClassRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkClassDTO> result = trademarkClassService.partialUpdate(trademarkClassDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkClassDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trademark-classes} : get all the trademarkClasses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trademarkClasses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TrademarkClassDTO>> getAllTrademarkClasses(
        TrademarkClassCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TrademarkClasses by criteria: {}", criteria);

        Page<TrademarkClassDTO> page = trademarkClassQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /trademark-classes/count} : count all the trademarkClasses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTrademarkClasses(TrademarkClassCriteria criteria) {
        LOG.debug("REST request to count TrademarkClasses by criteria: {}", criteria);
        return ResponseEntity.ok().body(trademarkClassQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /trademark-classes/:id} : get the "id" trademarkClass.
     *
     * @param id the id of the trademarkClassDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkClassDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrademarkClassDTO> getTrademarkClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrademarkClass : {}", id);
        Optional<TrademarkClassDTO> trademarkClassDTO = trademarkClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademarkClassDTO);
    }

    /**
     * {@code DELETE  /trademark-classes/:id} : delete the "id" trademarkClass.
     *
     * @param id the id of the trademarkClassDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademarkClass(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrademarkClass : {}", id);
        trademarkClassService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
