package com.bassi.tmapp.service.extended;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.repository.extended.PublishedTmRepositoryExtended;
import com.bassi.tmapp.service.PublishedTmQueryService;
import com.bassi.tmapp.service.criteria.PublishedTmCriteria;
import com.bassi.tmapp.service.dto.MatchingTrademarkDto;
import com.bassi.tmapp.service.dto.PublishedTmDTO;
import com.bassi.tmapp.service.extended.pdfService.ITextPdfReaderService;
import com.bassi.tmapp.service.mapper.PublishedTmMapper;
import com.bassi.tmapp.service.webScraping.TrademarkScrapingService;
import com.bassi.tmapp.web.rest.errors.InternalServerAlertException;

import jakarta.persistence.EntityManager;
import tech.jhipster.service.Criteria;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.PublishedTm}.
 */
@Service
@Transactional
public class PublishedTmServiceExtended {

    private static final Logger log = LoggerFactory.getLogger(PublishedTmServiceExtended.class);

    private final PublishedTmRepositoryExtended publishedTmRepositoryExtended;

    private final PublishedTmMapper publishedTmMapper;
    private final ITextPdfReaderService pdfReaderService;
    private final PublishedTmPhoneticsServiceExtended publishedTmPhoneticsService;
    private final EntityManager em;
    private final PublishedTmQueryService publishedTmQueryService;
    private final TrademarkScrapingService trademarkScrapingService;
    
    @Value("${file-upload-base-path}")
    private String baseUploadDirectory;

	public PublishedTmServiceExtended(PublishedTmRepositoryExtended publishedTmRepositoryExtended,
			PublishedTmMapper publishedTmMapper, ITextPdfReaderService pdfReaderService,
			PublishedTmPhoneticsServiceExtended publishedTmPhoneticsService, EntityManager em,
			PublishedTmQueryService publishedTmQueryService, TrademarkScrapingService trademarkScrapingService) {
        this.publishedTmRepositoryExtended = publishedTmRepositoryExtended;
        this.publishedTmMapper = publishedTmMapper;
        this.pdfReaderService = pdfReaderService;
        this.publishedTmPhoneticsService = publishedTmPhoneticsService;
        this.em = em;
        this.publishedTmQueryService = publishedTmQueryService;
        this.trademarkScrapingService = trademarkScrapingService;
        
        
        
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
        publishedTm = publishedTmRepositoryExtended.save(publishedTm);
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
        publishedTm = publishedTmRepositoryExtended.save(publishedTm);
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

        return publishedTmRepositoryExtended
            .findById(publishedTmDTO.getId())
            .map(existingPublishedTm -> {
                publishedTmMapper.partialUpdate(existingPublishedTm, publishedTmDTO);

                return existingPublishedTm;
            })
            .map(publishedTmRepositoryExtended::save)
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
        return publishedTmRepositoryExtended.findById(id).map(publishedTmMapper::toDto);
    }

    /**
     * Delete the publishedTm by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PublishedTm : {}", id);
        publishedTmRepositoryExtended.deleteById(id);
    }
    public void processTrademarkExtraction() {
    	// download latest pdf files based on journal
    	Integer journalNo = trademarkScrapingService.downloadPdf();
    	if(journalNo == null) {
    		throw new InternalServerAlertException("Process is aborted because journal No is null");
    	}
    	//read pdf files based on journal
    	readPdfFile(journalNo);
    	
		// scrape journal trademarks
    	scrapeJournalTrademarks(journalNo);
    }
    public void scrapeJournalTrademarks(Integer journalNo) {
		List<PublishedTm> publishedTms = publishedTmRepositoryExtended.findTrademarksWhereNameIsNull(journalNo);    	
    	trademarkScrapingService.scrape(publishedTms);

    }

	public void readPdfFile(int journalNo) {
		pdfReaderService.readPdfFilesFromFileSystem(journalNo);
	}

	public void generateMissingPhonetics(int journalNo) {
		List<PublishedTm> trademarks = publishedTmRepositoryExtended.findTrademarksWherePhoneticsMissing(journalNo);
		publishedTmPhoneticsService.saveAll(trademarks);
		
	}
	 
	public List<MatchingTrademarkDto> findMatchingTrademarkByJournal(Integer journalNo) {
		String sqlQuery = "WITH published AS "
				+ "(SELECT tm.*, ph.phonetic_pk , ph.sanitized_tm as pub_sanintized_tm FROM published_tm tm "
				+ " INNER JOIN published_tm_phonetics ph on tm.id = ph.published_tm_id WHERE journal_no =" + journalNo + " AND ph.complete=true), "
				+ "registered AS "
				+ "(SELECT tm.name, tm.tm_class, ph.phonetic_pk, ph.sanitized_tm as reg_sanintized_tm, ph.complete FROM trademark tm "
				+ " INNER JOIN phonetics ph on tm.id = ph.trademark_id WHERE ph.complete=true) "
				+ "SELECT tm.name as matchingTrademark, t.name as  registeredTrademark , tm.tm_class as tmClass, "
				+ "tm.application_no as applicationNo, tm.details , tm.journal_no as journalNo , tm.proprietor_name as proprietorName ,tm.proprietor_address as proprietorAddress, "
				+ "tm.agent_name as agentName , tm.agent_address as agentAddress, levenshtein(pub_sanintized_tm,reg_sanintized_tm) as distance "
				+ "FROM published tm "
				+ "  INNER JOIN registered t on tm.phonetic_pk = t.phonetic_pk and  tm.tm_class = t.tm_class"
				+ " ORDER BY tm.tm_class,distance" ;
		List<MatchingTrademarkDto> trademarks = em.createNativeQuery(sqlQuery, MatchingTrademarkDto.class).getResultList();
		return trademarks;
	}
	
	public void softDeleteByJournalNo(Integer journalNo) {
        log.debug("Request to delete PublishedTm having journalNo : {}", journalNo);
        publishedTmRepositoryExtended.softDeleteByJournalNo(journalNo);
    }
	
	public void deleteByJournalNo(PublishedTmCriteria criteria) {
        log.debug("Request to delete PublishedTm having journalNo : {}", criteria);
        Pageable page = PageRequest.of(0, 100);
                
        Page<PublishedTm> trademarkPage  = publishedTmQueryService.findByCriteria(criteria, page);
        while (trademarkPage.hasContent()) {
        	deleteTrademarkImages(trademarkPage.getContent());
        	
        	if(trademarkPage.hasNext()) {
				trademarkPage = publishedTmRepositoryExtended.findAll(trademarkPage.nextPageable()); 
			}
			else {
				break;
			}
		}
        Integer journalNo = criteria.getJournalNo().getEquals();
        publishedTmRepositoryExtended.deleteByJournalNo(journalNo);
        
        
    }
	private void deleteTrademarkImages(List<PublishedTm> list) {
		for(PublishedTm tm:list) {
			if(tm.getImgUrl() != null) {
				log.info("Going to delete tm image");
				String resourcesDir = Paths.get(baseUploadDirectory).toAbsolutePath().toString();		
				Path imgPath = Paths.get(String.join("/" , resourcesDir, tm.getImgUrl()));
				try {
					Files.delete(imgPath);
					log.info("File deleted successfully : {} ", tm.getImgUrl());
				}
				catch(IOException e) {
					log.error("Failed to delete the file, Reason: {}", e.getLocalizedMessage());
					
				}
			}
		}
		
		
	}
	
	public Integer calculateLevenshteinDistance(String name1, String name2) {
		LevenshteinDistance distance = new LevenshteinDistance();
		return distance.apply(name1,name2);
	}

	
	
}
