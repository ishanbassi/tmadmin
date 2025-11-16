package com.bassi.tmapp.web.rest;

import com.bassi.tmapp.repository.DocumentsRepository;
import com.bassi.tmapp.service.DocumentsService;
import com.bassi.tmapp.service.dto.DocumentsDTO;
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
 * REST controller for managing {@link com.bassi.tmapp.domain.Documents}.
 */
@RestController
@RequestMapping("/api/documents")
public class DocumentsResource {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentsResource.class);

    private static final String ENTITY_NAME = "documents";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentsService documentsService;

    private final DocumentsRepository documentsRepository;

    public DocumentsResource(DocumentsService documentsService, DocumentsRepository documentsRepository) {
        this.documentsService = documentsService;
        this.documentsRepository = documentsRepository;
    }

    /**
     * {@code POST  /documents} : Create a new documents.
     *
     * @param documentsDTO the documentsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentsDTO, or with status {@code 400 (Bad Request)} if the documents has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DocumentsDTO> createDocuments(@RequestBody DocumentsDTO documentsDTO) throws URISyntaxException {
        LOG.debug("REST request to save Documents : {}", documentsDTO);
        if (documentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new documents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentsDTO = documentsService.save(documentsDTO);
        return ResponseEntity.created(new URI("/api/documents/" + documentsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, documentsDTO.getId().toString()))
            .body(documentsDTO);
    }

    /**
     * {@code PUT  /documents/:id} : Updates an existing documents.
     *
     * @param id the id of the documentsDTO to save.
     * @param documentsDTO the documentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentsDTO,
     * or with status {@code 400 (Bad Request)} if the documentsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DocumentsDTO> updateDocuments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentsDTO documentsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Documents : {}, {}", id, documentsDTO);
        if (documentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        documentsDTO = documentsService.update(documentsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentsDTO.getId().toString()))
            .body(documentsDTO);
    }

    /**
     * {@code PATCH  /documents/:id} : Partial updates given fields of an existing documents, field will ignore if it is null
     *
     * @param id the id of the documentsDTO to save.
     * @param documentsDTO the documentsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentsDTO,
     * or with status {@code 400 (Bad Request)} if the documentsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the documentsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DocumentsDTO> partialUpdateDocuments(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DocumentsDTO documentsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Documents partially : {}, {}", id, documentsDTO);
        if (documentsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DocumentsDTO> result = documentsService.partialUpdate(documentsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, documentsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /documents} : get all the documents.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DocumentsDTO>> getAllDocuments(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Documents");
        Page<DocumentsDTO> page = documentsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /documents/:id} : get the "id" documents.
     *
     * @param id the id of the documentsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DocumentsDTO> getDocuments(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Documents : {}", id);
        Optional<DocumentsDTO> documentsDTO = documentsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentsDTO);
    }

    /**
     * {@code DELETE  /documents/:id} : delete the "id" documents.
     *
     * @param id the id of the documentsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocuments(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Documents : {}", id);
        documentsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/save")
    public ResponseEntity<DocumentsDTO> createDocumentsAndSaveFile(@RequestBody DocumentsDTO documentsDTO) throws URISyntaxException {
        LOG.debug("REST request to save Documents : {}", documentsDTO);
        if (documentsDTO.getId() != null) {
            throw new BadRequestAlertException("A new documents cannot already have an ID", ENTITY_NAME, "idexists");
        }
        documentsDTO = documentsService.saveDocumentAndSaveFile(documentsDTO);
        return ResponseEntity.created(new URI("/api/documents/" + documentsDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, documentsDTO.getId().toString()))
            .body(documentsDTO);
    }
}
