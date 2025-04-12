package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@Service
@Transactional
public class PublishedTmService {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmService.class);

    private final PublishedTmRepository publishedTmRepository;

    public PublishedTmService(PublishedTmRepository publishedTmRepository) {
        this.publishedTmRepository = publishedTmRepository;
    }

    /**
     * Save a publishedTm.
     *
     * @param publishedTm the entity to save.
     * @return the persisted entity.
     */
    public PublishedTm save(PublishedTm publishedTm) {
        log.debug("Request to save PublishedTm : {}", publishedTm);
        return publishedTmRepository.save(publishedTm);
    }

    /**
     * Update a publishedTm.
     *
     * @param publishedTm the entity to save.
     * @return the persisted entity.
     */
    public PublishedTm update(PublishedTm publishedTm) {
        log.debug("Request to update PublishedTm : {}", publishedTm);
        return publishedTmRepository.save(publishedTm);
    }

    /**
     * Partially update a publishedTm.
     *
     * @param publishedTm the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublishedTm> partialUpdate(PublishedTm publishedTm) {
        log.debug("Request to partially update PublishedTm : {}", publishedTm);

        return publishedTmRepository
            .findById(publishedTm.getId())
            .map(existingPublishedTm -> {
                if (publishedTm.getName() != null) {
                    existingPublishedTm.setName(publishedTm.getName());
                }
                if (publishedTm.getDetails() != null) {
                    existingPublishedTm.setDetails(publishedTm.getDetails());
                }
                if (publishedTm.getApplicationNo() != null) {
                    existingPublishedTm.setApplicationNo(publishedTm.getApplicationNo());
                }
                if (publishedTm.getApplicationDate() != null) {
                    existingPublishedTm.setApplicationDate(publishedTm.getApplicationDate());
                }
                if (publishedTm.getAgentName() != null) {
                    existingPublishedTm.setAgentName(publishedTm.getAgentName());
                }
                if (publishedTm.getAgentAddress() != null) {
                    existingPublishedTm.setAgentAddress(publishedTm.getAgentAddress());
                }
                if (publishedTm.getProprietorName() != null) {
                    existingPublishedTm.setProprietorName(publishedTm.getProprietorName());
                }
                if (publishedTm.getProprietorAddress() != null) {
                    existingPublishedTm.setProprietorAddress(publishedTm.getProprietorAddress());
                }
                if (publishedTm.getHeadOffice() != null) {
                    existingPublishedTm.setHeadOffice(publishedTm.getHeadOffice());
                }
                if (publishedTm.getImgUrl() != null) {
                    existingPublishedTm.setImgUrl(publishedTm.getImgUrl());
                }
                if (publishedTm.getTmClass() != null) {
                    existingPublishedTm.setTmClass(publishedTm.getTmClass());
                }
                if (publishedTm.getJournalNo() != null) {
                    existingPublishedTm.setJournalNo(publishedTm.getJournalNo());
                }
                if (publishedTm.getDeleted() != null) {
                    existingPublishedTm.setDeleted(publishedTm.getDeleted());
                }
                if (publishedTm.getUsage() != null) {
                    existingPublishedTm.setUsage(publishedTm.getUsage());
                }
                if (publishedTm.getAssociatedTms() != null) {
                    existingPublishedTm.setAssociatedTms(publishedTm.getAssociatedTms());
                }
                if (publishedTm.getTrademarkStatus() != null) {
                    existingPublishedTm.setTrademarkStatus(publishedTm.getTrademarkStatus());
                }
                if (publishedTm.getCreatedDate() != null) {
                    existingPublishedTm.setCreatedDate(publishedTm.getCreatedDate());
                }
                if (publishedTm.getModifiedDate() != null) {
                    existingPublishedTm.setModifiedDate(publishedTm.getModifiedDate());
                }
                if (publishedTm.getRenewalDate() != null) {
                    existingPublishedTm.setRenewalDate(publishedTm.getRenewalDate());
                }
                if (publishedTm.getType() != null) {
                    existingPublishedTm.setType(publishedTm.getType());
                }
                if (publishedTm.getPageNo() != null) {
                    existingPublishedTm.setPageNo(publishedTm.getPageNo());
                }

                return existingPublishedTm;
            })
            .map(publishedTmRepository::save);
    }

    /**
     * Get one publishedTm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublishedTm> findOne(Long id) {
        log.debug("Request to get PublishedTm : {}", id);
        return publishedTmRepository.findById(id);
    }

    /**
     * Delete the publishedTm by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PublishedTm : {}", id);
        publishedTmRepository.deleteById(id);
    }
}
