package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.PhoneticsRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Phonetics}.
 */
@Service
@Transactional
public class PhoneticsService {

    private static final Logger log = LoggerFactory.getLogger(PhoneticsService.class);

    private final PhoneticsRepository phoneticsRepository;

    public PhoneticsService(PhoneticsRepository phoneticsRepository) {
        this.phoneticsRepository = phoneticsRepository;
    }

    /**
     * Save a phonetics.
     *
     * @param phonetics the entity to save.
     * @return the persisted entity.
     */
    public Phonetics save(Phonetics phonetics) {
        log.debug("Request to save Phonetics : {}", phonetics);
        return phoneticsRepository.save(phonetics);
    }

    /**
     * Update a phonetics.
     *
     * @param phonetics the entity to save.
     * @return the persisted entity.
     */
    public Phonetics update(Phonetics phonetics) {
        log.debug("Request to update Phonetics : {}", phonetics);
        return phoneticsRepository.save(phonetics);
    }

    /**
     * Partially update a phonetics.
     *
     * @param phonetics the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Phonetics> partialUpdate(Phonetics phonetics) {
        log.debug("Request to partially update Phonetics : {}", phonetics);

        return phoneticsRepository
            .findById(phonetics.getId())
            .map(existingPhonetics -> {
                if (phonetics.getSanitizedTm() != null) {
                    existingPhonetics.setSanitizedTm(phonetics.getSanitizedTm());
                }
                if (phonetics.getPhoneticPk() != null) {
                    existingPhonetics.setPhoneticPk(phonetics.getPhoneticPk());
                }
                if (phonetics.getPhoneticSk() != null) {
                    existingPhonetics.setPhoneticSk(phonetics.getPhoneticSk());
                }
                if (phonetics.getComplete() != null) {
                    existingPhonetics.setComplete(phonetics.getComplete());
                }

                return existingPhonetics;
            })
            .map(phoneticsRepository::save);
    }

    /**
     * Get all the phonetics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Phonetics> findAll() {
        log.debug("Request to get all Phonetics");
        return phoneticsRepository.findAll();
    }

    /**
     * Get one phonetics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Phonetics> findOne(Long id) {
        log.debug("Request to get Phonetics : {}", id);
        return phoneticsRepository.findById(id);
    }

    /**
     * Delete the phonetics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Phonetics : {}", id);
        phoneticsRepository.deleteById(id);
    }
}
