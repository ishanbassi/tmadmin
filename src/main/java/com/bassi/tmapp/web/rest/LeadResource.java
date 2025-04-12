package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.LeadQueryService;
import com.bassi.tmapp.service.LeadService;
import com.bassi.tmapp.service.criteria.LeadCriteria;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.Lead}.
 */
@RestController
@RequestMapping("/api/leads")
public class LeadResource {

    private static final Logger log = LoggerFactory.getLogger(LeadResource.class);

    private static final String ENTITY_NAME = "lead";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeadService leadService;

    private final LeadRepository leadRepository;

    private final LeadQueryService leadQueryService;

    public LeadResource(LeadService leadService, LeadRepository leadRepository, LeadQueryService leadQueryService) {
        this.leadService = leadService;
        this.leadRepository = leadRepository;
        this.leadQueryService = leadQueryService;
    }

    /**
     * {@code POST  /leads} : Create a new lead.
     *
     * @param lead the lead to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lead, or with status {@code 400 (Bad Request)} if the lead has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Lead> createLead(@RequestBody Lead lead) throws URISyntaxException {
        log.debug("REST request to save Lead : {}", lead);
        if (lead.getId() != null) {
            throw new BadRequestAlertException("A new lead cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lead = leadService.save(lead);
        return ResponseEntity.created(new URI("/api/leads/" + lead.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, lead.getId().toString()))
            .body(lead);
    }

    /**
     * {@code PUT  /leads/:id} : Updates an existing lead.
     *
     * @param id the id of the lead to save.
     * @param lead the lead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lead,
     * or with status {@code 400 (Bad Request)} if the lead is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Lead> updateLead(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lead lead)
        throws URISyntaxException {
        log.debug("REST request to update Lead : {}, {}", id, lead);
        if (lead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lead = leadService.update(lead);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lead.getId().toString()))
            .body(lead);
    }

    /**
     * {@code PATCH  /leads/:id} : Partial updates given fields of an existing lead, field will ignore if it is null
     *
     * @param id the id of the lead to save.
     * @param lead the lead to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lead,
     * or with status {@code 400 (Bad Request)} if the lead is not valid,
     * or with status {@code 404 (Not Found)} if the lead is not found,
     * or with status {@code 500 (Internal Server Error)} if the lead couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Lead> partialUpdateLead(@PathVariable(value = "id", required = false) final Long id, @RequestBody Lead lead)
        throws URISyntaxException {
        log.debug("REST request to partial update Lead partially : {}, {}", id, lead);
        if (lead.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lead.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leadRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Lead> result = leadService.partialUpdate(lead);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lead.getId().toString())
        );
    }

    /**
     * {@code GET  /leads} : get all the leads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leads in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Lead>> getAllLeads(LeadCriteria criteria) {
        log.debug("REST request to get Leads by criteria: {}", criteria);

        List<Lead> entityList = leadQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /leads/count} : count all the leads.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLeads(LeadCriteria criteria) {
        log.debug("REST request to count Leads by criteria: {}", criteria);
        return ResponseEntity.ok().body(leadQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /leads/:id} : get the "id" lead.
     *
     * @param id the id of the lead to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lead, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lead> getLead(@PathVariable("id") Long id) {
        log.debug("REST request to get Lead : {}", id);
        Optional<Lead> lead = leadService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lead);
    }

    /**
     * {@code DELETE  /leads/:id} : delete the "id" lead.
     *
     * @param id the id of the lead to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable("id") Long id) {
        log.debug("REST request to delete Lead : {}", id);
        leadService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
