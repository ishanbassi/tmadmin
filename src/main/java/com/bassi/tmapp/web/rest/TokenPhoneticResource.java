package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.service.TokenPhoneticService;
import com.bassi.tmapp.service.dto.TokenPhoneticDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.TokenPhonetic}.
 */
@RestController
@RequestMapping("/api/token-phonetics")
public class TokenPhoneticResource {

    private static final Logger LOG = LoggerFactory.getLogger(TokenPhoneticResource.class);

    private static final String ENTITY_NAME = "tokenPhonetic";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TokenPhoneticService tokenPhoneticService;

    private final TokenPhoneticRepository tokenPhoneticRepository;

    public TokenPhoneticResource(TokenPhoneticService tokenPhoneticService, TokenPhoneticRepository tokenPhoneticRepository) {
        this.tokenPhoneticService = tokenPhoneticService;
        this.tokenPhoneticRepository = tokenPhoneticRepository;
    }

    /**
     * {@code POST  /token-phonetics} : Create a new tokenPhonetic.
     *
     * @param tokenPhoneticDTO the tokenPhoneticDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tokenPhoneticDTO, or with status {@code 400 (Bad Request)} if the tokenPhonetic has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TokenPhoneticDTO> createTokenPhonetic(@RequestBody TokenPhoneticDTO tokenPhoneticDTO) throws URISyntaxException {
        LOG.debug("REST request to save TokenPhonetic : {}", tokenPhoneticDTO);
        if (tokenPhoneticDTO.getId() != null) {
            throw new BadRequestAlertException("A new tokenPhonetic cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tokenPhoneticDTO = tokenPhoneticService.save(tokenPhoneticDTO);
        return ResponseEntity.created(new URI("/api/token-phonetics/" + tokenPhoneticDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, tokenPhoneticDTO.getId().toString()))
            .body(tokenPhoneticDTO);
    }

    /**
     * {@code PUT  /token-phonetics/:id} : Updates an existing tokenPhonetic.
     *
     * @param id the id of the tokenPhoneticDTO to save.
     * @param tokenPhoneticDTO the tokenPhoneticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tokenPhoneticDTO,
     * or with status {@code 400 (Bad Request)} if the tokenPhoneticDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tokenPhoneticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TokenPhoneticDTO> updateTokenPhonetic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TokenPhoneticDTO tokenPhoneticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TokenPhonetic : {}, {}", id, tokenPhoneticDTO);
        if (tokenPhoneticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tokenPhoneticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tokenPhoneticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tokenPhoneticDTO = tokenPhoneticService.update(tokenPhoneticDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tokenPhoneticDTO.getId().toString()))
            .body(tokenPhoneticDTO);
    }

    /**
     * {@code PATCH  /token-phonetics/:id} : Partial updates given fields of an existing tokenPhonetic, field will ignore if it is null
     *
     * @param id the id of the tokenPhoneticDTO to save.
     * @param tokenPhoneticDTO the tokenPhoneticDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tokenPhoneticDTO,
     * or with status {@code 400 (Bad Request)} if the tokenPhoneticDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tokenPhoneticDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tokenPhoneticDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TokenPhoneticDTO> partialUpdateTokenPhonetic(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TokenPhoneticDTO tokenPhoneticDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TokenPhonetic partially : {}, {}", id, tokenPhoneticDTO);
        if (tokenPhoneticDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tokenPhoneticDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tokenPhoneticRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TokenPhoneticDTO> result = tokenPhoneticService.partialUpdate(tokenPhoneticDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tokenPhoneticDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /token-phonetics} : get all the tokenPhonetics.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tokenPhonetics in body.
     */
    @GetMapping("")
    public List<TokenPhoneticDTO> getAllTokenPhonetics() {
        LOG.debug("REST request to get all TokenPhonetics");
        return tokenPhoneticService.findAll();
    }

    /**
     * {@code GET  /token-phonetics/:id} : get the "id" tokenPhonetic.
     *
     * @param id the id of the tokenPhoneticDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tokenPhoneticDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TokenPhoneticDTO> getTokenPhonetic(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TokenPhonetic : {}", id);
        Optional<TokenPhoneticDTO> tokenPhoneticDTO = tokenPhoneticService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tokenPhoneticDTO);
    }

    /**
     * {@code DELETE  /token-phonetics/:id} : delete the "id" tokenPhonetic.
     *
     * @param id the id of the tokenPhoneticDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTokenPhonetic(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TokenPhonetic : {}", id);
        tokenPhoneticService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
