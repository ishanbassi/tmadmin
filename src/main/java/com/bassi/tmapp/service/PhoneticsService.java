package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Phonetics;
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.PhoneticsRepository;
import com.bassi.tmapp.service.dto.PhoneticsDTO;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.PhoneticsMapper;
import com.bassi.tmapp.service.mapper.TrademarkMapper;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static final Logger log = LoggerFactory.getLogger(PhoneticsService.class);

    private final PhoneticsRepository phoneticsRepository;

    private final PhoneticsMapper phoneticsMapper;
    private final TrademarkMapper trademarkMapper;
    private final WordSanitizationService wordSanitizationService;


	public PhoneticsService(PhoneticsRepository phoneticsRepository, PhoneticsMapper phoneticsMapper,
			TrademarkMapper trademarkMapper, WordSanitizationService wordSanitizationService) {
        this.phoneticsRepository = phoneticsRepository;
        this.phoneticsMapper = phoneticsMapper;
        this.trademarkMapper = trademarkMapper;
        this.wordSanitizationService = wordSanitizationService;
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
    
    
    
    
    public List<Phonetics> saveAll(List<Trademark> trademarks) {
		List<Phonetics> phoneticsList =  trademarks.stream()
				.filter(tm -> tm.getName() != null && !tm.getName().isBlank())
				.map(tm -> {
					String sanitizedTrademark  = this.wordSanitizationService.sanitizeWord(tm.getName().trim());
					List<String> subWords = Arrays
							.asList(sanitizedTrademark.split(" "))
							;
					List<PhoneticsDTO> phoneticDtoList = new ArrayList<>();
					if(subWords.size() == 1) {
						phoneticDtoList.add(generateDto(sanitizedTrademark,tm, true));
						return  phoneticsMapper.toEntity(phoneticDtoList);
					}
					phoneticDtoList = subWords
							.stream()
							.map(x -> generateDto(x,tm,false))
							.toList();
					List<PhoneticsDTO> modifiableDtoList = new ArrayList<>(phoneticDtoList);
					
					modifiableDtoList.add(generateDto(sanitizedTrademark,tm,true)); 
					return  phoneticsMapper.toEntity(modifiableDtoList);
				})
				.flatMap(List::stream)
				.toList();
				
		return phoneticsRepository.saveAll(phoneticsList);
	}
	
    public String generatePhonetics(String val) {
    	if(val == null || val.isBlank()) return null;
    	DoubleMetaphone dm = new DoubleMetaphone();
    	dm.setMaxCodeLen(100);
    	return dm.doubleMetaphone(val);
    }
    
    private PhoneticsDTO generateDto(String name , Trademark tm, Boolean completed ) {
    	String phonetics = generatePhonetics(name);
    	TrademarkDTO trademarkDto = trademarkMapper.toDto(tm);
		return  new PhoneticsDTO(name,phonetics,completed,trademarkDto);
    }
}
