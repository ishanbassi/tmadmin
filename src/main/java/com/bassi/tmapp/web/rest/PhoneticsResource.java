package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.repository.PhoneticsRepository;
import com.bassi.tmapp.service.PhoneticsService;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.Phonetics}.
 */
@RestController
@RequestMapping("/api/phonetics")
public class PhoneticsResource {

    private static final Logger log = LoggerFactory.getLogger(PhoneticsResource.class);

    private static final String ENTITY_NAME = "phonetics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhoneticsService phoneticsService;

    private final PhoneticsRepository phoneticsRepository;

    public PhoneticsResource(PhoneticsService phoneticsService, PhoneticsRepository phoneticsRepository) {
        this.phoneticsService = phoneticsService;
        this.phoneticsRepository = phoneticsRepository;
    }

    /**
     * {@code POST  /phonetics} : Create a new phonetics.
     *
     * @param phonetics the phonetics to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phonetics, or with status {@code 400 (Bad Request)} if the phonetics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Phonetics> createPhonetics(@RequestBody Phonetics phonetics) throws URISyntaxException {
        log.debug("REST request to save Phonetics : {}", phonetics);
        if (phonetics.getId() != null) {
            throw new BadRequestAlertException("A new phonetics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        phonetics = phoneticsService.save(phonetics);
        return ResponseEntity.created(new URI("/api/phonetics/" + phonetics.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, phonetics.getId().toString()))
            .body(phonetics);
    }

    /**
     * {@code PUT  /phonetics/:id} : Updates an existing phonetics.
     *
     * @param id the id of the phonetics to save.
     * @param phonetics the phonetics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonetics,
     * or with status {@code 400 (Bad Request)} if the phonetics is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phonetics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Phonetics> updatePhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Phonetics phonetics
    ) throws URISyntaxException {
        log.debug("REST request to update Phonetics : {}, {}", id, phonetics);
        if (phonetics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonetics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        phonetics = phoneticsService.update(phonetics);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phonetics.getId().toString()))
            .body(phonetics);
    }

    /**
     * {@code PATCH  /phonetics/:id} : Partial updates given fields of an existing phonetics, field will ignore if it is null
     *
     * @param id the id of the phonetics to save.
     * @param phonetics the phonetics to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phonetics,
     * or with status {@code 400 (Bad Request)} if the phonetics is not valid,
     * or with status {@code 404 (Not Found)} if the phonetics is not found,
     * or with status {@code 500 (Internal Server Error)} if the phonetics couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Phonetics> partialUpdatePhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Phonetics phonetics
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phonetics partially : {}, {}", id, phonetics);
        if (phonetics.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phonetics.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Phonetics> result = phoneticsService.partialUpdate(phonetics);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phonetics.getId().toString())
        );
    }

    /**
     * {@code GET  /phonetics} : get all the phonetics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phonetics in body.
     */
    @GetMapping("")
    public List<Phonetics> getAllPhonetics() {
        log.debug("REST request to get all Phonetics");
        return phoneticsService.findAll();
    }

    /**
     * {@code GET  /phonetics/:id} : get the "id" phonetics.
     *
     * @param id the id of the phonetics to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phonetics, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Phonetics> getPhonetics(@PathVariable("id") Long id) {
        log.debug("REST request to get Phonetics : {}", id);
        Optional<Phonetics> phonetics = phoneticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phonetics);
    }

    /**
     * {@code DELETE  /phonetics/:id} : delete the "id" phonetics.
     *
     * @param id the id of the phonetics to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhonetics(@PathVariable("id") Long id) {
        log.debug("REST request to delete Phonetics : {}", id);
        phoneticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
