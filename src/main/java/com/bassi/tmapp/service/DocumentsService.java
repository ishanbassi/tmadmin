package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.repository.DocumentsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Documents}.
 */
@Service
@Transactional
public class DocumentsService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentsService.class);

    private final DocumentsRepository documentsRepository;

    public DocumentsService(DocumentsRepository documentsRepository) {
        this.documentsRepository = documentsRepository;
    }

    /**
     * Save a documents.
     *
     * @param documents the entity to save.
     * @return the persisted entity.
     */
    public Documents save(Documents documents) {
        LOG.debug("Request to save Documents : {}", documents);
        return documentsRepository.save(documents);
    }

    /**
     * Update a documents.
     *
     * @param documents the entity to save.
     * @return the persisted entity.
     */
    public Documents update(Documents documents) {
        LOG.debug("Request to update Documents : {}", documents);
        return documentsRepository.save(documents);
    }

    /**
     * Partially update a documents.
     *
     * @param documents the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Documents> partialUpdate(Documents documents) {
        LOG.debug("Request to partially update Documents : {}", documents);

        return documentsRepository
            .findById(documents.getId())
            .map(existingDocuments -> {
                if (documents.getDocumentType() != null) {
                    existingDocuments.setDocumentType(documents.getDocumentType());
                }
                if (documents.getFileUrl() != null) {
                    existingDocuments.setFileUrl(documents.getFileUrl());
                }
                if (documents.getCreatedDate() != null) {
                    existingDocuments.setCreatedDate(documents.getCreatedDate());
                }
                if (documents.getModifiedDate() != null) {
                    existingDocuments.setModifiedDate(documents.getModifiedDate());
                }
                if (documents.getDeleted() != null) {
                    existingDocuments.setDeleted(documents.getDeleted());
                }

                return existingDocuments;
            })
            .map(documentsRepository::save);
    }

    /**
     * Get all the documents.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Documents> findAll(Pageable pageable) {
        LOG.debug("Request to get all Documents");
        return documentsRepository.findAll(pageable);
    }

    /**
     * Get one documents by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Documents> findOne(Long id) {
        LOG.debug("Request to get Documents : {}", id);
        return documentsRepository.findById(id);
    }

    /**
     * Delete the documents by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Documents : {}", id);
        documentsRepository.deleteById(id);
    }
}
