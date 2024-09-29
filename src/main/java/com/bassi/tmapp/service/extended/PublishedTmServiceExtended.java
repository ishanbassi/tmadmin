package com.bassi.tmapp.service.extended;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;

import jakarta.persistence.EntityManager;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@Service
@Transactional
public class PublishedTmServiceExtended {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmServiceExtended.class);

    private final PublishedTmRepository publishedTmRepository;

    private final PublishedTmMapper publishedTmMapper;
    private final ITextPdfReaderService pdfReaderService;
    private final PublishedTmPhoneticsServiceExtended publishedTmPhoneticsService;
    private final EntityManager em;

	public PublishedTmServiceExtended(PublishedTmRepository publishedTmRepository, PublishedTmMapper publishedTmMapper,
			ITextPdfReaderService pdfReaderService, PublishedTmPhoneticsServiceExtended publishedTmPhoneticsService,EntityManager em) {
        this.publishedTmRepository = publishedTmRepository;
        this.publishedTmMapper = publishedTmMapper;
        this.pdfReaderService = pdfReaderService;
        this.publishedTmPhoneticsService = publishedTmPhoneticsService;
        this.em = em;
        
    }

    /**
     * Save a publishedTm.
     *
     * @param publishedTmDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmDTO save(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to save PublishedTm : {}", publishedTmDTO);
        PublishedTm publishedTm = publishedTmMapper.toEntity(publishedTmDTO);
        publishedTm = publishedTmRepository.save(publishedTm);
        return publishedTmMapper.toDto(publishedTm);
    }

    /**
     * Update a publishedTm.
     *
     * @param publishedTmDTO the entity to save.
     * @return the persisted entity.
     */
    public PublishedTmDTO update(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to update PublishedTm : {}", publishedTmDTO);
        PublishedTm publishedTm = publishedTmMapper.toEntity(publishedTmDTO);
        publishedTm = publishedTmRepository.save(publishedTm);
        return publishedTmMapper.toDto(publishedTm);
    }

    /**
     * Partially update a publishedTm.
     *
     * @param publishedTmDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PublishedTmDTO> partialUpdate(PublishedTmDTO publishedTmDTO) {
        log.debug("Request to partially update PublishedTm : {}", publishedTmDTO);

        return publishedTmRepository
            .findById(publishedTmDTO.getId())
            .map(existingPublishedTm -> {
                publishedTmMapper.partialUpdate(existingPublishedTm, publishedTmDTO);

                return existingPublishedTm;
            })
            .map(publishedTmRepository::save)
            .map(publishedTmMapper::toDto);
    }

    /**
     * Get one publishedTm by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PublishedTmDTO> findOne(Long id) {
        log.debug("Request to get PublishedTm : {}", id);
        return publishedTmRepository.findById(id).map(publishedTmMapper::toDto);
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

	public void readPdfFile(int journalNo) {
		pdfReaderService.readPdfFilesFromFileSystem(journalNo);
	}

	public void generateMissingPhonetics(int journalNo) {
		List<PublishedTm> trademarks = publishedTmRepository.findTrademarksWherePhoneticsMissing(journalNo);
		publishedTmPhoneticsService.saveAll(trademarks);
		
	}
	 
	public List<MatchingTrademarkDto> findMatchingTrademarkByJournal(Integer journalNo) {
		String sqlQuery = "WITH published AS "
				+ "(SELECT tm.*, ph.phonetic_pk FROM published_tm tm "
				+ " INNER JOIN published_tm_phonetics ph on tm.id = ph.published_tm_id WHERE journal_no =" + journalNo + " AND ph.complete=true), "
				+ "registered AS "
				+ "(SELECT tm.name, tm.tm_class, ph.phonetic_pk, ph.complete FROM trademark tm "
				+ " INNER JOIN phonetics ph on tm.id = ph.trademark_id WHERE ph.complete=true) "
				+ "SELECT tm.name as matchingTrademark, t.name as  registeredTrademark , tm.tm_class as tmClass, "
				+ "tm.application_no as applicationNo, tm.details , tm.journal_no as journalNo , tm.proprietor_name as proprietorName ,tm.proprietor_address as proprietorAddress, "
				+ "tm.agent_name as agentName , tm.agent_address as agentAddress "
				+ "FROM published tm "
				+ "  INNER JOIN registered t on tm.phonetic_pk = t.phonetic_pk and  tm.tm_class = t.tm_class ORDER BY tm.tm_class" ;
		List<MatchingTrademarkDto> trademarks = em.createNativeQuery(sqlQuery, MatchingTrademarkDto.class).getResultList();
		return trademarks;
	}
}
