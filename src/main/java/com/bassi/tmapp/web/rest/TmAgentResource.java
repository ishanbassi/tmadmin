package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.service.TmAgentQueryService;
import com.bassi.tmapp.service.TmAgentService;
import com.bassi.tmapp.service.criteria.TmAgentCriteria;
import com.bassi.tmapp.service.dto.TmAgentDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TmAgent}.
 */
@RestController
@RequestMapping("/api/tm-agents")
public class TmAgentResource {

    private static final Logger LOG = LoggerFactory.getLogger(TmAgentResource.class);

    private static final String ENTITY_NAME = "tmAgent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TmAgentService tmAgentService;

    private final TmAgentRepository tmAgentRepository;

    private final TmAgentQueryService tmAgentQueryService;

    public TmAgentResource(TmAgentService tmAgentService, TmAgentRepository tmAgentRepository, TmAgentQueryService tmAgentQueryService) {
        this.tmAgentService = tmAgentService;
        this.tmAgentRepository = tmAgentRepository;
        this.tmAgentQueryService = tmAgentQueryService;
    }

    /**
     * {@code POST  /tm-agents} : Create a new tmAgent.
     *
     * @param tmAgentDTO the tmAgentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tmAgentDTO, or with status {@code 400 (Bad Request)} if the tmAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TmAgentDTO> createTmAgent(@RequestBody TmAgentDTO tmAgentDTO) throws URISyntaxException {
        LOG.debug("REST request to save TmAgent : {}", tmAgentDTO);
        if (tmAgentDTO.getId() != null) {
            throw new BadRequestAlertException("A new tmAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tmAgentDTO = tmAgentService.save(tmAgentDTO);
        return ResponseEntity.created(new URI("/api/tm-agents/" + tmAgentDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tmAgentDTO.getId().toString()))
            .body(tmAgentDTO);
    }

    /**
     * {@code PUT  /tm-agents/:id} : Updates an existing tmAgent.
     *
     * @param id the id of the tmAgentDTO to save.
     * @param tmAgentDTO the tmAgentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tmAgentDTO,
     * or with status {@code 400 (Bad Request)} if the tmAgentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tmAgentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TmAgentDTO> updateTmAgent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TmAgentDTO tmAgentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TmAgent : {}, {}", id, tmAgentDTO);
        if (tmAgentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tmAgentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tmAgentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tmAgentDTO = tmAgentService.update(tmAgentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tmAgentDTO.getId().toString()))
            .body(tmAgentDTO);
    }

    /**
     * {@code PATCH  /tm-agents/:id} : Partial updates given fields of an existing tmAgent, field will ignore if it is null
     *
     * @param id the id of the tmAgentDTO to save.
     * @param tmAgentDTO the tmAgentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tmAgentDTO,
     * or with status {@code 400 (Bad Request)} if the tmAgentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tmAgentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tmAgentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TmAgentDTO> partialUpdateTmAgent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TmAgentDTO tmAgentDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TmAgent partially : {}, {}", id, tmAgentDTO);
        if (tmAgentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tmAgentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tmAgentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TmAgentDTO> result = tmAgentService.partialUpdate(tmAgentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tmAgentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tm-agents} : get all the tmAgents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tmAgents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TmAgentDTO>> getAllTmAgents(
        TmAgentCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TmAgents by criteria: {}", criteria);

        Page<TmAgentDTO> page = tmAgentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tm-agents/count} : count all the tmAgents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTmAgents(TmAgentCriteria criteria) {
        LOG.debug("REST request to count TmAgents by criteria: {}", criteria);
        return ResponseEntity.ok().body(tmAgentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tm-agents/:id} : get the "id" tmAgent.
     *
     * @param id the id of the tmAgentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tmAgentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TmAgentDTO> getTmAgent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TmAgent : {}", id);
        Optional<TmAgentDTO> tmAgentDTO = tmAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tmAgentDTO);
    }

    /**
     * {@code DELETE  /tm-agents/:id} : delete the "id" tmAgent.
     *
     * @param id the id of the tmAgentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTmAgent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TmAgent : {}", id);
        tmAgentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
