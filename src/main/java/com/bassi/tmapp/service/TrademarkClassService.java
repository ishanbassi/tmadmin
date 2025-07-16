package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.repository.TrademarkClassRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TrademarkClass}.
 */
@Service
@Transactional
public class TrademarkClassService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkClassService.class);

    private final TrademarkClassRepository trademarkClassRepository;

    public TrademarkClassService(TrademarkClassRepository trademarkClassRepository) {
        this.trademarkClassRepository = trademarkClassRepository;
    }

    /**
     * Save a trademarkClass.
     *
     * @param trademarkClass the entity to save.
     * @return the persisted entity.
     */
    public TrademarkClass save(TrademarkClass trademarkClass) {
        LOG.debug("Request to save TrademarkClass : {}", trademarkClass);
        return trademarkClassRepository.save(trademarkClass);
    }

    /**
     * Update a trademarkClass.
     *
     * @param trademarkClass the entity to save.
     * @return the persisted entity.
     */
    public TrademarkClass update(TrademarkClass trademarkClass) {
        LOG.debug("Request to update TrademarkClass : {}", trademarkClass);
        return trademarkClassRepository.save(trademarkClass);
    }

    /**
     * Partially update a trademarkClass.
     *
     * @param trademarkClass the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkClass> partialUpdate(TrademarkClass trademarkClass) {
        LOG.debug("Request to partially update TrademarkClass : {}", trademarkClass);

        return trademarkClassRepository
            .findById(trademarkClass.getId())
            .map(existingTrademarkClass -> {
                if (trademarkClass.getCode() != null) {
                    existingTrademarkClass.setCode(trademarkClass.getCode());
                }
                if (trademarkClass.getTmClass() != null) {
                    existingTrademarkClass.setTmClass(trademarkClass.getTmClass());
                }
                if (trademarkClass.getKeyword() != null) {
                    existingTrademarkClass.setKeyword(trademarkClass.getKeyword());
                }
                if (trademarkClass.getTitle() != null) {
                    existingTrademarkClass.setTitle(trademarkClass.getTitle());
                }
                if (trademarkClass.getDescription() != null) {
                    existingTrademarkClass.setDescription(trademarkClass.getDescription());
                }
                if (trademarkClass.getCreatedDate() != null) {
                    existingTrademarkClass.setCreatedDate(trademarkClass.getCreatedDate());
                }
                if (trademarkClass.getModifiedDate() != null) {
                    existingTrademarkClass.setModifiedDate(trademarkClass.getModifiedDate());
                }
                if (trademarkClass.getDeleted() != null) {
                    existingTrademarkClass.setDeleted(trademarkClass.getDeleted());
                }

                return existingTrademarkClass;
            })
            .map(trademarkClassRepository::save);
    }

    /**
     * Get one trademarkClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkClass> findOne(Long id) {
        LOG.debug("Request to get TrademarkClass : {}", id);
        return trademarkClassRepository.findById(id);
    }

    /**
     * Delete the trademarkClass by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrademarkClass : {}", id);
        trademarkClassRepository.deleteById(id);
    }
}
