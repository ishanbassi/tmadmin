package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.PhoneticsRepository;
import com.bassi.tmapp.service.PhoneticsService;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
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
     * @param phoneticsDTO the phoneticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phoneticsDTO, or with status {@code 400 (Bad Request)} if the phonetics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhoneticsDTO> createPhonetics(@RequestBody PhoneticsDTO phoneticsDTO) throws URISyntaxException {
        log.debug("REST request to save Phonetics : {}", phoneticsDTO);
        if (phoneticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new phonetics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        phoneticsDTO = phoneticsService.save(phoneticsDTO);
        return ResponseEntity.created(new URI("/api/phonetics/" + phoneticsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, phoneticsDTO.getId().toString()))
            .body(phoneticsDTO);
    }

    /**
     * {@code PUT  /phonetics/:id} : Updates an existing phonetics.
     *
     * @param id the id of the phoneticsDTO to save.
     * @param phoneticsDTO the phoneticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneticsDTO,
     * or with status {@code 400 (Bad Request)} if the phoneticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phoneticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhoneticsDTO> updatePhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneticsDTO phoneticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Phonetics : {}, {}", id, phoneticsDTO);
        if (phoneticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        phoneticsDTO = phoneticsService.update(phoneticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phoneticsDTO.getId().toString()))
            .body(phoneticsDTO);
    }

    /**
     * {@code PATCH  /phonetics/:id} : Partial updates given fields of an existing phonetics, field will ignore if it is null
     *
     * @param id the id of the phoneticsDTO to save.
     * @param phoneticsDTO the phoneticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phoneticsDTO,
     * or with status {@code 400 (Bad Request)} if the phoneticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the phoneticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the phoneticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhoneticsDTO> partialUpdatePhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PhoneticsDTO phoneticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phonetics partially : {}, {}", id, phoneticsDTO);
        if (phoneticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phoneticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhoneticsDTO> result = phoneticsService.partialUpdate(phoneticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, phoneticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /phonetics} : get all the phonetics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phonetics in body.
     */
    @GetMapping("")
    public List<PhoneticsDTO> getAllPhonetics() {
        log.debug("REST request to get all Phonetics");
        return phoneticsService.findAll();
    }

    /**
     * {@code GET  /phonetics/:id} : get the "id" phonetics.
     *
     * @param id the id of the phoneticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phoneticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhoneticsDTO> getPhonetics(@PathVariable("id") Long id) {
        log.debug("REST request to get Phonetics : {}", id);
        Optional<PhoneticsDTO> phoneticsDTO = phoneticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phoneticsDTO);
    }

    /**
     * {@code DELETE  /phonetics/:id} : delete the "id" phonetics.
     *
     * @param id the id of the phoneticsDTO to delete.
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
