package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TmAgent}.
 */
@Service
@Transactional
public class TmAgentService {

    private static final Logger LOG = LoggerFactory.getLogger(TmAgentService.class);

    private final TmAgentRepository tmAgentRepository;

    public TmAgentService(TmAgentRepository tmAgentRepository) {
        this.tmAgentRepository = tmAgentRepository;
    }

    /**
     * Save a tmAgent.
     *
     * @param tmAgent the entity to save.
     * @return the persisted entity.
     */
    public TmAgent save(TmAgent tmAgent) {
        LOG.debug("Request to save TmAgent : {}", tmAgent);
        return tmAgentRepository.save(tmAgent);
    }

    /**
     * Update a tmAgent.
     *
     * @param tmAgent the entity to save.
     * @return the persisted entity.
     */
    public TmAgent update(TmAgent tmAgent) {
        LOG.debug("Request to update TmAgent : {}", tmAgent);
        return tmAgentRepository.save(tmAgent);
    }

    /**
     * Partially update a tmAgent.
     *
     * @param tmAgent the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TmAgent> partialUpdate(TmAgent tmAgent) {
        LOG.debug("Request to partially update TmAgent : {}", tmAgent);

        return tmAgentRepository
            .findById(tmAgent.getId())
            .map(existingTmAgent -> {
                if (tmAgent.getFullName() != null) {
                    existingTmAgent.setFullName(tmAgent.getFullName());
                }
                if (tmAgent.getAddress() != null) {
                    existingTmAgent.setAddress(tmAgent.getAddress());
                }
                if (tmAgent.getCreatedDate() != null) {
                    existingTmAgent.setCreatedDate(tmAgent.getCreatedDate());
                }
                if (tmAgent.getModifiedDate() != null) {
                    existingTmAgent.setModifiedDate(tmAgent.getModifiedDate());
                }
                if (tmAgent.getDeleted() != null) {
                    existingTmAgent.setDeleted(tmAgent.getDeleted());
                }
                if (tmAgent.getCompanyName() != null) {
                    existingTmAgent.setCompanyName(tmAgent.getCompanyName());
                }
                if (tmAgent.getAgentCode() != null) {
                    existingTmAgent.setAgentCode(tmAgent.getAgentCode());
                }
                if (tmAgent.getEmail() != null) {
                    existingTmAgent.setEmail(tmAgent.getEmail());
                }

                return existingTmAgent;
            })
            .map(tmAgentRepository::save);
    }

    /**
     * Get one tmAgent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TmAgent> findOne(Long id) {
        LOG.debug("Request to get TmAgent : {}", id);
        return tmAgentRepository.findById(id);
    }

    /**
     * Delete the tmAgent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TmAgent : {}", id);
        tmAgentRepository.deleteById(id);
    }
}
