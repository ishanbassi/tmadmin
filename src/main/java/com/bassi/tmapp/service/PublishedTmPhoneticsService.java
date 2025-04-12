package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.repository.PublishedTmPhoneticsRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTmPhonetics}.
 */
@Service
@Transactional
public class PublishedTmPhoneticsService {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmPhoneticsService.class);

    private final PublishedTmPhoneticsRepository publishedTmPhoneticsRepository;

    public PublishedTmPhoneticsService(PublishedTmPhoneticsRepository publishedTmPhoneticsRepository) {
        this.publishedTmPhoneticsRepository = publishedTmPhoneticsRepository;
    }

    /**
     * Save a publishedTmPhonetics.
     *
     * @param publishedTmPhonetics the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmPhonetics save(PublishedTmPhonetics publishedTmPhonetics) {
        log.debug("Request to save PublishedTmPhonetics : {}", publishedTmPhonetics);
        return publishedTmPhoneticsRepository.save(publishedTmPhonetics);
    }

    /**
     * Update a publishedTmPhonetics.
     *
     * @param publishedTmPhonetics the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmPhonetics update(PublishedTmPhonetics publishedTmPhonetics) {
        log.debug("Request to update PublishedTmPhonetics : {}", publishedTmPhonetics);
        return publishedTmPhoneticsRepository.save(publishedTmPhonetics);
    }

    /**
     * Partially update a publishedTmPhonetics.
     *
     * @param publishedTmPhonetics the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublishedTmPhonetics> partialUpdate(PublishedTmPhonetics publishedTmPhonetics) {
        log.debug("Request to partially update PublishedTmPhonetics : {}", publishedTmPhonetics);

        return publishedTmPhoneticsRepository
            .findById(publishedTmPhonetics.getId())
            .map(existingPublishedTmPhonetics -> {
                if (publishedTmPhonetics.getSanitizedTm() != null) {
                    existingPublishedTmPhonetics.setSanitizedTm(publishedTmPhonetics.getSanitizedTm());
                }
                if (publishedTmPhonetics.getPhoneticPk() != null) {
                    existingPublishedTmPhonetics.setPhoneticPk(publishedTmPhonetics.getPhoneticPk());
                }
                if (publishedTmPhonetics.getPhoneticSk() != null) {
                    existingPublishedTmPhonetics.setPhoneticSk(publishedTmPhonetics.getPhoneticSk());
                }
                if (publishedTmPhonetics.getComplete() != null) {
                    existingPublishedTmPhonetics.setComplete(publishedTmPhonetics.getComplete());
                }

                return existingPublishedTmPhonetics;
            })
            .map(publishedTmPhoneticsRepository::save);
    }

    /**
     * Get all the publishedTmPhonetics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PublishedTmPhonetics> findAll() {
        log.debug("Request to get all PublishedTmPhonetics");
        return publishedTmPhoneticsRepository.findAll();
    }

    /**
     * Get one publishedTmPhonetics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublishedTmPhonetics> findOne(Long id) {
        log.debug("Request to get PublishedTmPhonetics : {}", id);
        return publishedTmPhoneticsRepository.findById(id);
    }

    /**
     * Delete the publishedTmPhonetics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PublishedTmPhonetics : {}", id);
        publishedTmPhoneticsRepository.deleteById(id);
    }
}
