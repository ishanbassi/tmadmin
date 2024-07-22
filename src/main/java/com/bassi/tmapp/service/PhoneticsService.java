package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.repository.PhoneticsRepository;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
import com.bassi.tmapp.service.mapper.PhoneticsMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.codec.language.DoubleMetaphone;
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

    private final Logger log = LoggerFactory.getLogger(PhoneticsService.class);

    private final PhoneticsRepository phoneticsRepository;

    private final PhoneticsMapper phoneticsMapper;

    public PhoneticsService(PhoneticsRepository phoneticsRepository, PhoneticsMapper phoneticsMapper) {
        this.phoneticsRepository = phoneticsRepository;
        this.phoneticsMapper = phoneticsMapper;
    }

    /**
     * Save a phonetics.
     *
     * @param phoneticsDTO the entity to save.
     * @return the persisted entity.
     */
    public PhoneticsDTO save(PhoneticsDTO phoneticsDTO) {
        log.debug("Request to save Phonetics : {}", phoneticsDTO);
        Phonetics phonetics = phoneticsMapper.toEntity(phoneticsDTO);
        phonetics = phoneticsRepository.save(phonetics);
        return phoneticsMapper.toDto(phonetics);
    }

    /**
     * Update a phonetics.
     *
     * @param phoneticsDTO the entity to save.
     * @return the persisted entity.
     */
    public PhoneticsDTO update(PhoneticsDTO phoneticsDTO) {
        log.debug("Request to update Phonetics : {}", phoneticsDTO);
        Phonetics phonetics = phoneticsMapper.toEntity(phoneticsDTO);
        phonetics = phoneticsRepository.save(phonetics);
        return phoneticsMapper.toDto(phonetics);
    }

    /**
     * Partially update a phonetics.
     *
     * @param phoneticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhoneticsDTO> partialUpdate(PhoneticsDTO phoneticsDTO) {
        log.debug("Request to partially update Phonetics : {}", phoneticsDTO);

        return phoneticsRepository
            .findById(phoneticsDTO.getId())
            .map(existingPhonetics -> {
                phoneticsMapper.partialUpdate(existingPhonetics, phoneticsDTO);

                return existingPhonetics;
            })
            .map(phoneticsRepository::save)
            .map(phoneticsMapper::toDto);
    }

    /**
     * Get all the phonetics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PhoneticsDTO> findAll() {
        log.debug("Request to get all Phonetics");
        return phoneticsRepository.findAll().stream().map(phoneticsMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one phonetics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhoneticsDTO> findOne(Long id) {
        log.debug("Request to get Phonetics : {}", id);
        return phoneticsRepository.findById(id).map(phoneticsMapper::toDto);
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
    
    
    public String generatePhonetics(String val) {
    	if(val == null || val.isBlank()) return val;
    	DoubleMetaphone dm = new DoubleMetaphone();
    	dm.setMaxCodeLen(100);
    	return dm.doubleMetaphone(val);
    }
}
