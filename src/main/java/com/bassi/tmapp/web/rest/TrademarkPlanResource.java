package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TrademarkPlanRepository;
import com.bassi.tmapp.service.TrademarkPlanService;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import com.bassi.tmapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bassi.tmapp.domain.TrademarkPlan}.
 */
@RestController
@RequestMapping("/api/trademark-plans")
public class TrademarkPlanResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkPlanResource.class);

    private static final String ENTITY_NAME = "trademarkPlan";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkPlanService trademarkPlanService;

    private final TrademarkPlanRepository trademarkPlanRepository;

    public TrademarkPlanResource(TrademarkPlanService trademarkPlanService, TrademarkPlanRepository trademarkPlanRepository) {
        this.trademarkPlanService = trademarkPlanService;
        this.trademarkPlanRepository = trademarkPlanRepository;
    }

    /**
     * {@code POST  /trademark-plans} : Create a new trademarkPlan.
     *
     * @param trademarkPlanDTO the trademarkPlanDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademarkPlanDTO, or with status {@code 400 (Bad Request)} if the trademarkPlan has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrademarkPlanDTO> createTrademarkPlan(@RequestBody TrademarkPlanDTO trademarkPlanDTO) throws URISyntaxException {
        LOG.debug("REST request to save TrademarkPlan : {}", trademarkPlanDTO);
        if (trademarkPlanDTO.getId() != null) {
            throw new BadRequestAlertException("A new trademarkPlan cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademarkPlanDTO = trademarkPlanService.save(trademarkPlanDTO);
        return ResponseEntity.created(new URI("/api/trademark-plans/" + trademarkPlanDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademarkPlanDTO.getId().toString()))
            .body(trademarkPlanDTO);
    }

    /**
     * {@code PUT  /trademark-plans/:id} : Updates an existing trademarkPlan.
     *
     * @param id the id of the trademarkPlanDTO to save.
     * @param trademarkPlanDTO the trademarkPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkPlanDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkPlanDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademarkPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrademarkPlanDTO> updateTrademarkPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkPlanDTO trademarkPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrademarkPlan : {}, {}", id, trademarkPlanDTO);
        if (trademarkPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademarkPlanDTO = trademarkPlanService.update(trademarkPlanDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkPlanDTO.getId().toString()))
            .body(trademarkPlanDTO);
    }

    /**
     * {@code PATCH  /trademark-plans/:id} : Partial updates given fields of an existing trademarkPlan, field will ignore if it is null
     *
     * @param id the id of the trademarkPlanDTO to save.
     * @param trademarkPlanDTO the trademarkPlanDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkPlanDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkPlanDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trademarkPlanDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademarkPlanDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrademarkPlanDTO> partialUpdateTrademarkPlan(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkPlanDTO trademarkPlanDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrademarkPlan partially : {}, {}", id, trademarkPlanDTO);
        if (trademarkPlanDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkPlanDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkPlanRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkPlanDTO> result = trademarkPlanService.partialUpdate(trademarkPlanDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkPlanDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trademark-plans} : get all the trademarkPlans.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trademarkPlans in body.
     */
    @GetMapping("")
    public List<TrademarkPlanDTO> getAllTrademarkPlans() {
        LOG.debug("REST request to get all TrademarkPlans");
        return trademarkPlanService.findAll();
    }

    /**
     * {@code GET  /trademark-plans/:id} : get the "id" trademarkPlan.
     *
     * @param id the id of the trademarkPlanDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkPlanDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrademarkPlanDTO> getTrademarkPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrademarkPlan : {}", id);
        Optional<TrademarkPlanDTO> trademarkPlanDTO = trademarkPlanService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademarkPlanDTO);
    }

    /**
     * {@code DELETE  /trademark-plans/:id} : delete the "id" trademarkPlan.
     *
     * @param id the id of the trademarkPlanDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademarkPlan(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrademarkPlan : {}", id);
        trademarkPlanService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
