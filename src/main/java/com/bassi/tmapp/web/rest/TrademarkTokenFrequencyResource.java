package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TrademarkTokenFrequencyRepository;
import com.bassi.tmapp.service.TrademarkTokenFrequencyService;
import com.bassi.tmapp.service.dto.TrademarkTokenFrequencyDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TrademarkTokenFrequency}.
 */
@RestController
@RequestMapping("/api/trademark-token-frequencies")
public class TrademarkTokenFrequencyResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkTokenFrequencyResource.class);

    private static final String ENTITY_NAME = "trademarkTokenFrequency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkTokenFrequencyService trademarkTokenFrequencyService;

    private final TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository;

    public TrademarkTokenFrequencyResource(
        TrademarkTokenFrequencyService trademarkTokenFrequencyService,
        TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository
    ) {
        this.trademarkTokenFrequencyService = trademarkTokenFrequencyService;
        this.trademarkTokenFrequencyRepository = trademarkTokenFrequencyRepository;
    }

    /**
     * {@code POST  /trademark-token-frequencies} : Create a new trademarkTokenFrequency.
     *
     * @param trademarkTokenFrequencyDTO the trademarkTokenFrequencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademarkTokenFrequencyDTO, or with status {@code 400 (Bad Request)} if the trademarkTokenFrequency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrademarkTokenFrequencyDTO> createTrademarkTokenFrequency(
        @RequestBody TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save TrademarkTokenFrequency : {}", trademarkTokenFrequencyDTO);
        if (trademarkTokenFrequencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new trademarkTokenFrequency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademarkTokenFrequencyDTO = trademarkTokenFrequencyService.save(trademarkTokenFrequencyDTO);
        return ResponseEntity.created(new URI("/api/trademark-token-frequencies/" + trademarkTokenFrequencyDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademarkTokenFrequencyDTO.getId().toString())
            )
            .body(trademarkTokenFrequencyDTO);
    }

    /**
     * {@code PUT  /trademark-token-frequencies/:id} : Updates an existing trademarkTokenFrequency.
     *
     * @param id the id of the trademarkTokenFrequencyDTO to save.
     * @param trademarkTokenFrequencyDTO the trademarkTokenFrequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkTokenFrequencyDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkTokenFrequencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademarkTokenFrequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrademarkTokenFrequencyDTO> updateTrademarkTokenFrequency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrademarkTokenFrequency : {}, {}", id, trademarkTokenFrequencyDTO);
        if (trademarkTokenFrequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkTokenFrequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkTokenFrequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademarkTokenFrequencyDTO = trademarkTokenFrequencyService.update(trademarkTokenFrequencyDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkTokenFrequencyDTO.getId().toString()))
            .body(trademarkTokenFrequencyDTO);
    }

    /**
     * {@code PATCH  /trademark-token-frequencies/:id} : Partial updates given fields of an existing trademarkTokenFrequency, field will ignore if it is null
     *
     * @param id the id of the trademarkTokenFrequencyDTO to save.
     * @param trademarkTokenFrequencyDTO the trademarkTokenFrequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkTokenFrequencyDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkTokenFrequencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trademarkTokenFrequencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademarkTokenFrequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrademarkTokenFrequencyDTO> partialUpdateTrademarkTokenFrequency(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrademarkTokenFrequency partially : {}, {}", id, trademarkTokenFrequencyDTO);
        if (trademarkTokenFrequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkTokenFrequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkTokenFrequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkTokenFrequencyDTO> result = trademarkTokenFrequencyService.partialUpdate(trademarkTokenFrequencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkTokenFrequencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trademark-token-frequencies} : get all the trademarkTokenFrequencies.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trademarkTokenFrequencies in body.
     */
    @GetMapping("")
    public List<TrademarkTokenFrequencyDTO> getAllTrademarkTokenFrequencies() {
        LOG.debug("REST request to get all TrademarkTokenFrequencies");
        return trademarkTokenFrequencyService.findAll();
    }

    /**
     * {@code GET  /trademark-token-frequencies/:id} : get the "id" trademarkTokenFrequency.
     *
     * @param id the id of the trademarkTokenFrequencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkTokenFrequencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrademarkTokenFrequencyDTO> getTrademarkTokenFrequency(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrademarkTokenFrequency : {}", id);
        Optional<TrademarkTokenFrequencyDTO> trademarkTokenFrequencyDTO = trademarkTokenFrequencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademarkTokenFrequencyDTO);
    }

    /**
     * {@code DELETE  /trademark-token-frequencies/:id} : delete the "id" trademarkTokenFrequency.
     *
     * @param id the id of the trademarkTokenFrequencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademarkTokenFrequency(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrademarkTokenFrequency : {}", id);
        trademarkTokenFrequencyService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/stop-words")
    public ResponseEntity<Void> saveStopWordsInFrequencyTable() {
        trademarkTokenFrequencyService.saveStopWordsInFrequencyTable();
        return ResponseEntity.noContent().build();
    }
}
