package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.repository.PublishedTmPhoneticsRepository;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.mapper.PublishedTmPhoneticsMapper;
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
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTmPhonetics}.
 */
@Service
@Transactional
public class PublishedTmPhoneticsService {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmPhoneticsService.class);

    private final PublishedTmPhoneticsRepository publishedTmPhoneticsRepository;
    private final PublishedTmPhoneticsMapper publishedTmPhoneticsMapper;
    private final WordSanitizationService wordSanitizationService;

    public PublishedTmPhoneticsService(
        PublishedTmPhoneticsRepository publishedTmPhoneticsRepository,
        PublishedTmPhoneticsMapper publishedTmPhoneticsMapper,
        WordSanitizationService wordSanitizationService
    ) {
        this.publishedTmPhoneticsRepository = publishedTmPhoneticsRepository;
        this.publishedTmPhoneticsMapper = publishedTmPhoneticsMapper;
        this.wordSanitizationService = wordSanitizationService;
    }

    /**
     * Save a publishedTmPhonetics.
     *
     * @param publishedTmPhoneticsDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmPhoneticsDTO save(PublishedTmPhoneticsDTO publishedTmPhoneticsDTO) {
        log.debug("Request to save PublishedTmPhonetics : {}", publishedTmPhoneticsDTO);
        PublishedTmPhonetics publishedTmPhonetics = publishedTmPhoneticsMapper.toEntity(publishedTmPhoneticsDTO);
        publishedTmPhonetics = publishedTmPhoneticsRepository.save(publishedTmPhonetics);
        return publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);
    }

    /**
     * Update a publishedTmPhonetics.
     *
     * @param publishedTmPhoneticsDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmPhoneticsDTO update(PublishedTmPhoneticsDTO publishedTmPhoneticsDTO) {
        log.debug("Request to update PublishedTmPhonetics : {}", publishedTmPhoneticsDTO);
        PublishedTmPhonetics publishedTmPhonetics = publishedTmPhoneticsMapper.toEntity(publishedTmPhoneticsDTO);
        publishedTmPhonetics = publishedTmPhoneticsRepository.save(publishedTmPhonetics);
        return publishedTmPhoneticsMapper.toDto(publishedTmPhonetics);
    }

    /**
     * Partially update a publishedTmPhonetics.
     *
     * @param publishedTmPhoneticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublishedTmPhoneticsDTO> partialUpdate(PublishedTmPhoneticsDTO publishedTmPhoneticsDTO) {
        log.debug("Request to partially update PublishedTmPhonetics : {}", publishedTmPhoneticsDTO);

        return publishedTmPhoneticsRepository
            .findById(publishedTmPhoneticsDTO.getId())
            .map(existingPublishedTmPhonetics -> {
                publishedTmPhoneticsMapper.partialUpdate(existingPublishedTmPhonetics, publishedTmPhoneticsDTO);

                return existingPublishedTmPhonetics;
            })
            .map(publishedTmPhoneticsRepository::save)
            .map(publishedTmPhoneticsMapper::toDto);
    }

    /**
     * Get all the publishedTmPhonetics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PublishedTmPhoneticsDTO> findAll() {
        log.debug("Request to get all PublishedTmPhonetics");
        return publishedTmPhoneticsRepository
            .findAll()
            .stream()
            .map(publishedTmPhoneticsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one publishedTmPhonetics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublishedTmPhoneticsDTO> findOne(Long id) {
        log.debug("Request to get PublishedTmPhonetics : {}", id);
        return publishedTmPhoneticsRepository.findById(id).map(publishedTmPhoneticsMapper::toDto);
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

	public List<PublishedTmPhonetics> saveAll(List<PublishedTm> publishedTrademarks) {
		List<PublishedTmPhonetics> publishedTmPhoneticsList =  publishedTrademarks.stream().filter(tm -> tm.getName() != null && !tm.getName().isBlank()).map(tm -> {
			String sanitizedTm = this.wordSanitizationService.sanitizeWord(tm.getName());
			PublishedTmPhonetics tmPhonetics = new PublishedTmPhonetics();
			tmPhonetics.setPublishedTm(tm);
			tmPhonetics.setPhoneticPk(generatePhonetics(tm.getName()));
			tmPhonetics.setSanitizedTm(sanitizedTm);

			return tmPhonetics;

		}).toList();
		return publishedTmPhoneticsRepository.saveAll(publishedTmPhoneticsList);
	}
	
    public String generatePhonetics(String val) {
    	if(val == null || val.isBlank()) return null;
    	DoubleMetaphone dm = new DoubleMetaphone();
    	dm.setMaxCodeLen(100);
    	return dm.doubleMetaphone(val);
    }
}
