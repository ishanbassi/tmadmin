package com.bassi.tmapp.service.extended;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.repository.PublishedTmPhoneticsRepository;
import com.bassi.tmapp.service.CommonUtilService;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.bassi.tmapp.service.mapper.PublishedTmPhoneticsMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class PublishedTmPhoneticsServiceExtended {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmPhoneticsServiceExtended.class);

    private final PublishedTmPhoneticsRepository publishedTmPhoneticsRepository;
    private final PublishedTmPhoneticsMapper publishedTmPhoneticsMapper;
    private final WordSanitizationService wordSanitizationService;
	private final PublishedTmMapper publishedTmMapper;
	private final CommonUtilService commonUtilService;

    

    public PublishedTmPhoneticsServiceExtended(
        PublishedTmPhoneticsRepository publishedTmPhoneticsRepository,
        PublishedTmPhoneticsMapper publishedTmPhoneticsMapper,
        WordSanitizationService wordSanitizationService,
        PublishedTmMapper publishedTmMapper,
        CommonUtilService commonUtilService
    ) {
        this.publishedTmPhoneticsRepository = publishedTmPhoneticsRepository;
        this.publishedTmPhoneticsMapper = publishedTmPhoneticsMapper;
        this.wordSanitizationService = wordSanitizationService;
        this.publishedTmMapper = publishedTmMapper;
        this.commonUtilService = commonUtilService; 
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
		List<PublishedTmPhonetics> publishedTmPhoneticsList =  publishedTrademarks.stream()
				.filter(tm -> tm.getName() != null && !tm.getName().isBlank())
				.map(tm -> {
					String sanitizedTrademark  = this.wordSanitizationService.sanitizeWord(tm.getName().trim());
					List<String> subWords = Arrays
							.asList(sanitizedTrademark.split(" "))
							;
					
					List<PublishedTmPhoneticsDTO> phoneticDtoList = new ArrayList<>();
					
					if(subWords.size() == 1) {
						phoneticDtoList.add(generateDto(sanitizedTrademark,tm, true));
						return  publishedTmPhoneticsMapper.toEntity(phoneticDtoList);
					}
					phoneticDtoList = subWords
							.stream()
							.map(x -> generateDto(x,tm,false))
							.toList();
					
					List<PublishedTmPhoneticsDTO> modifiableDtoList = new ArrayList<>(phoneticDtoList);
					
					modifiableDtoList.add(generateDto(sanitizedTrademark,tm,true)); 
					return  publishedTmPhoneticsMapper.toEntity(modifiableDtoList);
				})
				.flatMap(List::stream)
				.toList();
				
		return publishedTmPhoneticsRepository.saveAll(publishedTmPhoneticsList);
	}
	
    public String generatePhonetics(String val) {
    	if(val == null || val.isBlank()) return null;
    	DoubleMetaphone dm = new DoubleMetaphone();
    	dm.setMaxCodeLen(100);
    	return dm.doubleMetaphone(val);
    }
    
    private PublishedTmPhoneticsDTO generateDto(String name , PublishedTm tm, Boolean completed ) {
    	String phonetics = generatePhonetics(name);
		PublishedTmDTO publishedTmDto = publishedTmMapper.toDto(tm);
		return  new PublishedTmPhoneticsDTO(name,phonetics,completed,publishedTmDto);
    }
    
    
}
