package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.TrademarkTokenService;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TrademarkToken}.
 */
@RestController
@RequestMapping("/api/trademark-tokens")
public class TrademarkTokenResource {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkTokenResource.class);

    private static final String ENTITY_NAME = "trademarkToken";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrademarkTokenService trademarkTokenService;

    private final TrademarkTokenRepository trademarkTokenRepository;

    public TrademarkTokenResource(TrademarkTokenService trademarkTokenService, TrademarkTokenRepository trademarkTokenRepository) {
        this.trademarkTokenService = trademarkTokenService;
        this.trademarkTokenRepository = trademarkTokenRepository;
    }

    /**
     * {@code POST  /trademark-tokens} : Create a new trademarkToken.
     *
     * @param trademarkTokenDTO the trademarkTokenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trademarkTokenDTO, or with status {@code 400 (Bad Request)} if the trademarkToken has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TrademarkTokenDTO> createTrademarkToken(@RequestBody TrademarkTokenDTO trademarkTokenDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TrademarkToken : {}", trademarkTokenDTO);
        if (trademarkTokenDTO.getId() != null) {
            throw new BadRequestAlertException("A new trademarkToken cannot already have an ID", ENTITY_NAME, "idexists");
        }
        trademarkTokenDTO = trademarkTokenService.save(trademarkTokenDTO);
        return ResponseEntity.created(new URI("/api/trademark-tokens/" + trademarkTokenDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, trademarkTokenDTO.getId().toString()))
            .body(trademarkTokenDTO);
    }

    /**
     * {@code PUT  /trademark-tokens/:id} : Updates an existing trademarkToken.
     *
     * @param id the id of the trademarkTokenDTO to save.
     * @param trademarkTokenDTO the trademarkTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkTokenDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkTokenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trademarkTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrademarkTokenDTO> updateTrademarkToken(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkTokenDTO trademarkTokenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TrademarkToken : {}, {}", id, trademarkTokenDTO);
        if (trademarkTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkTokenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkTokenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        trademarkTokenDTO = trademarkTokenService.update(trademarkTokenDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkTokenDTO.getId().toString()))
            .body(trademarkTokenDTO);
    }

    /**
     * {@code PATCH  /trademark-tokens/:id} : Partial updates given fields of an existing trademarkToken, field will ignore if it is null
     *
     * @param id the id of the trademarkTokenDTO to save.
     * @param trademarkTokenDTO the trademarkTokenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trademarkTokenDTO,
     * or with status {@code 400 (Bad Request)} if the trademarkTokenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the trademarkTokenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the trademarkTokenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrademarkTokenDTO> partialUpdateTrademarkToken(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrademarkTokenDTO trademarkTokenDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TrademarkToken partially : {}, {}", id, trademarkTokenDTO);
        if (trademarkTokenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trademarkTokenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trademarkTokenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrademarkTokenDTO> result = trademarkTokenService.partialUpdate(trademarkTokenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, trademarkTokenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /trademark-tokens} : get all the trademarkTokens.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trademarkTokens in body.
     */
    @GetMapping("")
    public List<TrademarkTokenDTO> getAllTrademarkTokens() {
        LOG.debug("REST request to get all TrademarkTokens");
        return trademarkTokenService.findAll();
    }

    /**
     * {@code GET  /trademark-tokens/:id} : get the "id" trademarkToken.
     *
     * @param id the id of the trademarkTokenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trademarkTokenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrademarkTokenDTO> getTrademarkToken(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TrademarkToken : {}", id);
        Optional<TrademarkTokenDTO> trademarkTokenDTO = trademarkTokenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(trademarkTokenDTO);
    }

    /**
     * {@code DELETE  /trademark-tokens/:id} : delete the "id" trademarkToken.
     *
     * @param id the id of the trademarkTokenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrademarkToken(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TrademarkToken : {}", id);
        trademarkTokenService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
