package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.UserEventsTrackingRepository;
import com.bassi.tmapp.service.UserEventsTrackingService;
import com.bassi.tmapp.service.dto.UserEventsTrackingDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.UserEventsTracking}.
 */
@RestController
@RequestMapping("/api/user-events-trackings")
public class UserEventsTrackingResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserEventsTrackingResource.class);

    private static final String ENTITY_NAME = "userEventsTracking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserEventsTrackingService userEventsTrackingService;

    private final UserEventsTrackingRepository userEventsTrackingRepository;

    public UserEventsTrackingResource(
        UserEventsTrackingService userEventsTrackingService,
        UserEventsTrackingRepository userEventsTrackingRepository
    ) {
        this.userEventsTrackingService = userEventsTrackingService;
        this.userEventsTrackingRepository = userEventsTrackingRepository;
    }

    /**
     * {@code POST  /user-events-trackings} : Create a new userEventsTracking.
     *
     * @param userEventsTrackingDTO the userEventsTrackingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userEventsTrackingDTO, or with status {@code 400 (Bad Request)} if the userEventsTracking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserEventsTrackingDTO> createUserEventsTracking(@RequestBody UserEventsTrackingDTO userEventsTrackingDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save UserEventsTracking : {}", userEventsTrackingDTO);
        if (userEventsTrackingDTO.getId() != null) {
            throw new BadRequestAlertException("A new userEventsTracking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userEventsTrackingDTO = userEventsTrackingService.save(userEventsTrackingDTO);
        return ResponseEntity.created(new URI("/api/user-events-trackings/" + userEventsTrackingDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userEventsTrackingDTO.getId().toString()))
            .body(userEventsTrackingDTO);
    }

    /**
     * {@code PUT  /user-events-trackings/:id} : Updates an existing userEventsTracking.
     *
     * @param id the id of the userEventsTrackingDTO to save.
     * @param userEventsTrackingDTO the userEventsTrackingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventsTrackingDTO,
     * or with status {@code 400 (Bad Request)} if the userEventsTrackingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userEventsTrackingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserEventsTrackingDTO> updateUserEventsTracking(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserEventsTrackingDTO userEventsTrackingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserEventsTracking : {}, {}", id, userEventsTrackingDTO);
        if (userEventsTrackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventsTrackingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventsTrackingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userEventsTrackingDTO = userEventsTrackingService.update(userEventsTrackingDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userEventsTrackingDTO.getId().toString()))
            .body(userEventsTrackingDTO);
    }

    /**
     * {@code PATCH  /user-events-trackings/:id} : Partial updates given fields of an existing userEventsTracking, field will ignore if it is null
     *
     * @param id the id of the userEventsTrackingDTO to save.
     * @param userEventsTrackingDTO the userEventsTrackingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userEventsTrackingDTO,
     * or with status {@code 400 (Bad Request)} if the userEventsTrackingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userEventsTrackingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userEventsTrackingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserEventsTrackingDTO> partialUpdateUserEventsTracking(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserEventsTrackingDTO userEventsTrackingDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserEventsTracking partially : {}, {}", id, userEventsTrackingDTO);
        if (userEventsTrackingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userEventsTrackingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userEventsTrackingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserEventsTrackingDTO> result = userEventsTrackingService.partialUpdate(userEventsTrackingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userEventsTrackingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-events-trackings} : get all the userEventsTrackings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userEventsTrackings in body.
     */
    @GetMapping("")
    public List<UserEventsTrackingDTO> getAllUserEventsTrackings() {
        LOG.debug("REST request to get all UserEventsTrackings");
        return userEventsTrackingService.findAll();
    }

    /**
     * {@code GET  /user-events-trackings/:id} : get the "id" userEventsTracking.
     *
     * @param id the id of the userEventsTrackingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userEventsTrackingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserEventsTrackingDTO> getUserEventsTracking(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserEventsTracking : {}", id);
        Optional<UserEventsTrackingDTO> userEventsTrackingDTO = userEventsTrackingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userEventsTrackingDTO);
    }

    /**
     * {@code DELETE  /user-events-trackings/:id} : delete the "id" userEventsTracking.
     *
     * @param id the id of the userEventsTrackingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserEventsTracking(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserEventsTracking : {}", id);
        userEventsTrackingService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
