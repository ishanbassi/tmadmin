package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Trademark}.
 */
@Service
@Transactional
public class TrademarkService {

    private static final Logger log = LoggerFactory.getLogger(TrademarkService.class);

    private final TrademarkRepository trademarkRepository;

    public TrademarkService(TrademarkRepository trademarkRepository) {
        this.trademarkRepository = trademarkRepository;
    }

    /**
     * Save a trademark.
     *
     * @param trademark the entity to save.
     * @return the persisted entity.
     */
    public Trademark save(Trademark trademark) {
        log.debug("Request to save Trademark : {}", trademark);
        return trademarkRepository.save(trademark);
    }

    /**
     * Update a trademark.
     *
     * @param trademark the entity to save.
     * @return the persisted entity.
     */
    public Trademark update(Trademark trademark) {
        log.debug("Request to update Trademark : {}", trademark);
        return trademarkRepository.save(trademark);
    }

    /**
     * Partially update a trademark.
     *
     * @param trademark the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Trademark> partialUpdate(Trademark trademark) {
        log.debug("Request to partially update Trademark : {}", trademark);

        return trademarkRepository
            .findById(trademark.getId())
            .map(existingTrademark -> {
                if (trademark.getName() != null) {
                    existingTrademark.setName(trademark.getName());
                }
                if (trademark.getDetails() != null) {
                    existingTrademark.setDetails(trademark.getDetails());
                }
                if (trademark.getApplicationNo() != null) {
                    existingTrademark.setApplicationNo(trademark.getApplicationNo());
                }
                if (trademark.getApplicationDate() != null) {
                    existingTrademark.setApplicationDate(trademark.getApplicationDate());
                }
                if (trademark.getAgentName() != null) {
                    existingTrademark.setAgentName(trademark.getAgentName());
                }
                if (trademark.getAgentAddress() != null) {
                    existingTrademark.setAgentAddress(trademark.getAgentAddress());
                }
                if (trademark.getProprietorName() != null) {
                    existingTrademark.setProprietorName(trademark.getProprietorName());
                }
                if (trademark.getProprietorAddress() != null) {
                    existingTrademark.setProprietorAddress(trademark.getProprietorAddress());
                }
                if (trademark.getHeadOffice() != null) {
                    existingTrademark.setHeadOffice(trademark.getHeadOffice());
                }
                if (trademark.getImgUrl() != null) {
                    existingTrademark.setImgUrl(trademark.getImgUrl());
                }
                if (trademark.getTmClass() != null) {
                    existingTrademark.setTmClass(trademark.getTmClass());
                }
                if (trademark.getJournalNo() != null) {
                    existingTrademark.setJournalNo(trademark.getJournalNo());
                }
                if (trademark.getDeleted() != null) {
                    existingTrademark.setDeleted(trademark.getDeleted());
                }
                if (trademark.getUsage() != null) {
                    existingTrademark.setUsage(trademark.getUsage());
                }
                if (trademark.getAssociatedTms() != null) {
                    existingTrademark.setAssociatedTms(trademark.getAssociatedTms());
                }
                if (trademark.getTrademarkStatus() != null) {
                    existingTrademark.setTrademarkStatus(trademark.getTrademarkStatus());
                }
                if (trademark.getCreatedDate() != null) {
                    existingTrademark.setCreatedDate(trademark.getCreatedDate());
                }
                if (trademark.getModifiedDate() != null) {
                    existingTrademark.setModifiedDate(trademark.getModifiedDate());
                }

                return existingTrademark;
            })
            .map(trademarkRepository::save);
    }

    /**
     * Get one trademark by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Trademark> findOne(Long id) {
        log.debug("Request to get Trademark : {}", id);
        return trademarkRepository.findById(id);
    }

    /**
     * Delete the trademark by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Trademark : {}", id);
        trademarkRepository.deleteById(id);
    }
}
