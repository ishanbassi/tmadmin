package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.mapper.LeadMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Lead}.
 */
@Service
@Transactional
public class LeadService {

    private static final Logger LOG = LoggerFactory.getLogger(LeadService.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    public LeadService(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    /**
     * Save a lead.
     *
     * @param leadDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadDTO save(LeadDTO leadDTO) {
        LOG.debug("Request to save Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        return leadMapper.toDto(lead);
    }

    /**
     * Update a lead.
     *
     * @param leadDTO the entity to save.
     * @return the persisted entity.
     */
    public LeadDTO update(LeadDTO leadDTO) {
        LOG.debug("Request to update Lead : {}", leadDTO);
        Lead lead = leadMapper.toEntity(leadDTO);
        lead = leadRepository.save(lead);
        return leadMapper.toDto(lead);
    }

    /**
     * Partially update a lead.
     *
     * @param leadDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LeadDTO> partialUpdate(LeadDTO leadDTO) {
        LOG.debug("Request to partially update Lead : {}", leadDTO);

        return leadRepository
            .findById(leadDTO.getId())
            .map(existingLead -> {
                leadMapper.partialUpdate(existingLead, leadDTO);

                return existingLead;
            })
            .map(leadRepository::save)
            .map(leadMapper::toDto);
    }

    /**
     * Get one lead by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LeadDTO> findOne(Long id) {
        LOG.debug("Request to get Lead : {}", id);
        return leadRepository.findById(id).map(leadMapper::toDto);
    }

    /**
     * Delete the lead by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Lead : {}", id);
        leadRepository.deleteById(id);
    }
}
