package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.service.dto.TmAgentDTO;
import com.bassi.tmapp.service.mapper.TmAgentMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final Logger log = LoggerFactory.getLogger(TmAgentService.class);

    private final TmAgentRepository tmAgentRepository;

    private final TmAgentMapper tmAgentMapper;

    public TmAgentService(TmAgentRepository tmAgentRepository, TmAgentMapper tmAgentMapper) {
        this.tmAgentRepository = tmAgentRepository;
        this.tmAgentMapper = tmAgentMapper;
    }

    /**
     * Save a tmAgent.
     *
     * @param tmAgentDTO the entity to save.
     * @return the persisted entity.
     */
    public TmAgentDTO save(TmAgentDTO tmAgentDTO) {
        log.debug("Request to save TmAgent : {}", tmAgentDTO);
        TmAgent tmAgent = tmAgentMapper.toEntity(tmAgentDTO);
        tmAgent = tmAgentRepository.save(tmAgent);
        return tmAgentMapper.toDto(tmAgent);
    }

    /**
     * Update a tmAgent.
     *
     * @param tmAgentDTO the entity to save.
     * @return the persisted entity.
     */
    public TmAgentDTO update(TmAgentDTO tmAgentDTO) {
        log.debug("Request to update TmAgent : {}", tmAgentDTO);
        TmAgent tmAgent = tmAgentMapper.toEntity(tmAgentDTO);
        tmAgent = tmAgentRepository.save(tmAgent);
        return tmAgentMapper.toDto(tmAgent);
    }

    /**
     * Partially update a tmAgent.
     *
     * @param tmAgentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TmAgentDTO> partialUpdate(TmAgentDTO tmAgentDTO) {
        log.debug("Request to partially update TmAgent : {}", tmAgentDTO);

        return tmAgentRepository
            .findById(tmAgentDTO.getId())
            .map(existingTmAgent -> {
                tmAgentMapper.partialUpdate(existingTmAgent, tmAgentDTO);

                return existingTmAgent;
            })
            .map(tmAgentRepository::save)
            .map(tmAgentMapper::toDto);
    }

    /**
     * Get all the tmAgents.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TmAgentDTO> findAll() {
        log.debug("Request to get all TmAgents");
        return tmAgentRepository.findAll().stream().map(tmAgentMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tmAgent by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TmAgentDTO> findOne(Long id) {
        log.debug("Request to get TmAgent : {}", id);
        return tmAgentRepository.findById(id).map(tmAgentMapper::toDto);
    }

    /**
     * Delete the tmAgent by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TmAgent : {}", id);
        tmAgentRepository.deleteById(id);
    }
}
