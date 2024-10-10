package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.service.TmAgentService;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TmAgent}.
 */
@RestController
@RequestMapping("/api/tm-agents")
public class TmAgentResource {

    private static final Logger log = LoggerFactory.getLogger(TmAgentResource.class);

    private static final String ENTITY_NAME = "tmAgent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TmAgentService tmAgentService;

    private final TmAgentRepository tmAgentRepository;

    public TmAgentResource(TmAgentService tmAgentService, TmAgentRepository tmAgentRepository) {
        this.tmAgentService = tmAgentService;
        this.tmAgentRepository = tmAgentRepository;
    }

    /**
     * {@code POST  /tm-agents} : Create a new tmAgent.
     *
     * @param tmAgent the tmAgent to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tmAgent, or with status {@code 400 (Bad Request)} if the tmAgent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TmAgent> createTmAgent(@RequestBody TmAgent tmAgent) throws URISyntaxException {
        log.debug("REST request to save TmAgent : {}", tmAgent);
        if (tmAgent.getId() != null) {
            throw new BadRequestAlertException("A new tmAgent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tmAgent = tmAgentService.save(tmAgent);
        return ResponseEntity.created(new URI("/api/tm-agents/" + tmAgent.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tmAgent.getId().toString()))
            .body(tmAgent);
    }

    /**
     * {@code PUT  /tm-agents/:id} : Updates an existing tmAgent.
     *
     * @param id the id of the tmAgent to save.
     * @param tmAgent the tmAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tmAgent,
     * or with status {@code 400 (Bad Request)} if the tmAgent is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tmAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TmAgent> updateTmAgent(@PathVariable(value = "id", required = false) final Long id, @RequestBody TmAgent tmAgent)
        throws URISyntaxException {
        log.debug("REST request to update TmAgent : {}, {}", id, tmAgent);
        if (tmAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tmAgent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tmAgentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tmAgent = tmAgentService.update(tmAgent);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tmAgent.getId().toString()))
            .body(tmAgent);
    }

    /**
     * {@code PATCH  /tm-agents/:id} : Partial updates given fields of an existing tmAgent, field will ignore if it is null
     *
     * @param id the id of the tmAgent to save.
     * @param tmAgent the tmAgent to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tmAgent,
     * or with status {@code 400 (Bad Request)} if the tmAgent is not valid,
     * or with status {@code 404 (Not Found)} if the tmAgent is not found,
     * or with status {@code 500 (Internal Server Error)} if the tmAgent couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TmAgent> partialUpdateTmAgent(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TmAgent tmAgent
    ) throws URISyntaxException {
        log.debug("REST request to partial update TmAgent partially : {}, {}", id, tmAgent);
        if (tmAgent.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tmAgent.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tmAgentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TmAgent> result = tmAgentService.partialUpdate(tmAgent);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tmAgent.getId().toString())
        );
    }

    /**
     * {@code GET  /tm-agents} : get all the tmAgents.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tmAgents in body.
     */
    @GetMapping("")
    public List<TmAgent> getAllTmAgents() {
        log.debug("REST request to get all TmAgents");
        return tmAgentService.findAll();
    }

    /**
     * {@code GET  /tm-agents/:id} : get the "id" tmAgent.
     *
     * @param id the id of the tmAgent to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tmAgent, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TmAgent> getTmAgent(@PathVariable("id") Long id) {
        log.debug("REST request to get TmAgent : {}", id);
        Optional<TmAgent> tmAgent = tmAgentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tmAgent);
    }

    /**
     * {@code DELETE  /tm-agents/:id} : delete the "id" tmAgent.
     *
     * @param id the id of the tmAgent to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTmAgent(@PathVariable("id") Long id) {
        log.debug("REST request to delete TmAgent : {}", id);
        tmAgentService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
