package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.PublishedTmQueryService;
import com.bassi.tmapp.service.PublishedTmService;
import com.bassi.tmapp.service.criteria.PublishedTmCriteria;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@RestController
@RequestMapping("/api/published-tms")
public class PublishedTmResource {

    private static final Logger LOG = LoggerFactory.getLogger(PublishedTmResource.class);

    private static final String ENTITY_NAME = "publishedTm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublishedTmService publishedTmService;

    private final PublishedTmRepository publishedTmRepository;

    private final PublishedTmQueryService publishedTmQueryService;

    public PublishedTmResource(
        PublishedTmService publishedTmService,
        PublishedTmRepository publishedTmRepository,
        PublishedTmQueryService publishedTmQueryService
    ) {
        this.publishedTmService = publishedTmService;
        this.publishedTmRepository = publishedTmRepository;
        this.publishedTmQueryService = publishedTmQueryService;
    }

    /**
     * {@code POST  /published-tms} : Create a new publishedTm.
     *
     * @param publishedTm the publishedTm to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publishedTm, or with status {@code 400 (Bad Request)} if the publishedTm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PublishedTm> createPublishedTm(@RequestBody PublishedTm publishedTm) throws URISyntaxException {
        LOG.debug("REST request to save PublishedTm : {}", publishedTm);
        if (publishedTm.getId() != null) {
            throw new BadRequestAlertException("A new publishedTm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        publishedTm = publishedTmService.save(publishedTm);
        return ResponseEntity.created(new URI("/api/published-tms/" + publishedTm.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, publishedTm.getId().toString()))
            .body(publishedTm);
    }

    /**
     * {@code PUT  /published-tms/:id} : Updates an existing publishedTm.
     *
     * @param id the id of the publishedTm to save.
     * @param publishedTm the publishedTm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTm,
     * or with status {@code 400 (Bad Request)} if the publishedTm is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publishedTm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublishedTm> updatePublishedTm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTm publishedTm
    ) throws URISyntaxException {
        LOG.debug("REST request to update PublishedTm : {}, {}", id, publishedTm);
        if (publishedTm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        publishedTm = publishedTmService.update(publishedTm);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTm.getId().toString()))
            .body(publishedTm);
    }

    /**
     * {@code PATCH  /published-tms/:id} : Partial updates given fields of an existing publishedTm, field will ignore if it is null
     *
     * @param id the id of the publishedTm to save.
     * @param publishedTm the publishedTm to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTm,
     * or with status {@code 400 (Bad Request)} if the publishedTm is not valid,
     * or with status {@code 404 (Not Found)} if the publishedTm is not found,
     * or with status {@code 500 (Internal Server Error)} if the publishedTm couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PublishedTm> partialUpdatePublishedTm(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTm publishedTm
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PublishedTm partially : {}, {}", id, publishedTm);
        if (publishedTm.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTm.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PublishedTm> result = publishedTmService.partialUpdate(publishedTm);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTm.getId().toString())
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
        LOG.debug("REST request to get PublishedTms by criteria: {}", criteria);

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
        LOG.debug("REST request to count PublishedTms by criteria: {}", criteria);
        return ResponseEntity.ok().body(publishedTmQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /published-tms/:id} : get the "id" publishedTm.
     *
     * @param id the id of the publishedTm to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publishedTm, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublishedTm> getPublishedTm(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PublishedTm : {}", id);
        Optional<PublishedTm> publishedTm = publishedTmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publishedTm);
    }

    /**
     * {@code DELETE  /published-tms/:id} : delete the "id" publishedTm.
     *
     * @param id the id of the publishedTm to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublishedTm(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PublishedTm : {}", id);
        publishedTmService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
