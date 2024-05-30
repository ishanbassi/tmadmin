package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.PublishedTmPhoneticsRepository;
import com.bassi.tmapp.service.PublishedTmPhoneticsService;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.PublishedTmPhonetics}.
 */
@RestController
@RequestMapping("/api/published-tm-phonetics")
public class PublishedTmPhoneticsResource {

    private final Logger log = LoggerFactory.getLogger(PublishedTmPhoneticsResource.class);

    private static final String ENTITY_NAME = "publishedTmPhonetics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PublishedTmPhoneticsService publishedTmPhoneticsService;

    private final PublishedTmPhoneticsRepository publishedTmPhoneticsRepository;

    public PublishedTmPhoneticsResource(
        PublishedTmPhoneticsService publishedTmPhoneticsService,
        PublishedTmPhoneticsRepository publishedTmPhoneticsRepository
    ) {
        this.publishedTmPhoneticsService = publishedTmPhoneticsService;
        this.publishedTmPhoneticsRepository = publishedTmPhoneticsRepository;
    }

    /**
     * {@code POST  /published-tm-phonetics} : Create a new publishedTmPhonetics.
     *
     * @param publishedTmPhoneticsDTO the publishedTmPhoneticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publishedTmPhoneticsDTO, or with status {@code 400 (Bad Request)} if the publishedTmPhonetics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PublishedTmPhoneticsDTO> createPublishedTmPhonetics(@RequestBody PublishedTmPhoneticsDTO publishedTmPhoneticsDTO)
        throws URISyntaxException {
        log.debug("REST request to save PublishedTmPhonetics : {}", publishedTmPhoneticsDTO);
        if (publishedTmPhoneticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new publishedTmPhonetics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        publishedTmPhoneticsDTO = publishedTmPhoneticsService.save(publishedTmPhoneticsDTO);
        return ResponseEntity.created(new URI("/api/published-tm-phonetics/" + publishedTmPhoneticsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, publishedTmPhoneticsDTO.getId().toString()))
            .body(publishedTmPhoneticsDTO);
    }

    /**
     * {@code PUT  /published-tm-phonetics/:id} : Updates an existing publishedTmPhonetics.
     *
     * @param id the id of the publishedTmPhoneticsDTO to save.
     * @param publishedTmPhoneticsDTO the publishedTmPhoneticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTmPhoneticsDTO,
     * or with status {@code 400 (Bad Request)} if the publishedTmPhoneticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publishedTmPhoneticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PublishedTmPhoneticsDTO> updatePublishedTmPhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTmPhoneticsDTO publishedTmPhoneticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PublishedTmPhonetics : {}, {}", id, publishedTmPhoneticsDTO);
        if (publishedTmPhoneticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTmPhoneticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmPhoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        publishedTmPhoneticsDTO = publishedTmPhoneticsService.update(publishedTmPhoneticsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTmPhoneticsDTO.getId().toString()))
            .body(publishedTmPhoneticsDTO);
    }

    /**
     * {@code PATCH  /published-tm-phonetics/:id} : Partial updates given fields of an existing publishedTmPhonetics, field will ignore if it is null
     *
     * @param id the id of the publishedTmPhoneticsDTO to save.
     * @param publishedTmPhoneticsDTO the publishedTmPhoneticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publishedTmPhoneticsDTO,
     * or with status {@code 400 (Bad Request)} if the publishedTmPhoneticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the publishedTmPhoneticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the publishedTmPhoneticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PublishedTmPhoneticsDTO> partialUpdatePublishedTmPhonetics(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PublishedTmPhoneticsDTO publishedTmPhoneticsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PublishedTmPhonetics partially : {}, {}", id, publishedTmPhoneticsDTO);
        if (publishedTmPhoneticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publishedTmPhoneticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publishedTmPhoneticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PublishedTmPhoneticsDTO> result = publishedTmPhoneticsService.partialUpdate(publishedTmPhoneticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publishedTmPhoneticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /published-tm-phonetics} : get all the publishedTmPhonetics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishedTmPhonetics in body.
     */
    @GetMapping("")
    public List<PublishedTmPhoneticsDTO> getAllPublishedTmPhonetics() {
        log.debug("REST request to get all PublishedTmPhonetics");
        return publishedTmPhoneticsService.findAll();
    }

    /**
     * {@code GET  /published-tm-phonetics/:id} : get the "id" publishedTmPhonetics.
     *
     * @param id the id of the publishedTmPhoneticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publishedTmPhoneticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PublishedTmPhoneticsDTO> getPublishedTmPhonetics(@PathVariable("id") Long id) {
        log.debug("REST request to get PublishedTmPhonetics : {}", id);
        Optional<PublishedTmPhoneticsDTO> publishedTmPhoneticsDTO = publishedTmPhoneticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(publishedTmPhoneticsDTO);
    }

    /**
     * {@code DELETE  /published-tm-phonetics/:id} : delete the "id" publishedTmPhonetics.
     *
     * @param id the id of the publishedTmPhoneticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublishedTmPhonetics(@PathVariable("id") Long id) {
        log.debug("REST request to delete PublishedTmPhonetics : {}", id);
        publishedTmPhoneticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
