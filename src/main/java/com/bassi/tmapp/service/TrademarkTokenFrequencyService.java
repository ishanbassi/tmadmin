package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TrademarkTokenFrequency;
import com.bassi.tmapp.repository.TrademarkTokenFrequencyRepository;
import com.bassi.tmapp.service.dto.TrademarkTokenFrequencyDTO;
import com.bassi.tmapp.service.mapper.TrademarkTokenFrequencyMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TrademarkTokenFrequency}.
 */
@Service
@Transactional
public class TrademarkTokenFrequencyService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkTokenFrequencyService.class);

    private final TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository;

    private final TrademarkTokenFrequencyMapper trademarkTokenFrequencyMapper;

    public TrademarkTokenFrequencyService(
        TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository,
        TrademarkTokenFrequencyMapper trademarkTokenFrequencyMapper
    ) {
        this.trademarkTokenFrequencyRepository = trademarkTokenFrequencyRepository;
        this.trademarkTokenFrequencyMapper = trademarkTokenFrequencyMapper;
    }

    /**
     * Save a trademarkTokenFrequency.
     *
     * @param trademarkTokenFrequencyDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkTokenFrequencyDTO save(TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO) {
        LOG.debug("Request to save TrademarkTokenFrequency : {}", trademarkTokenFrequencyDTO);
        TrademarkTokenFrequency trademarkTokenFrequency = trademarkTokenFrequencyMapper.toEntity(trademarkTokenFrequencyDTO);
        trademarkTokenFrequency = trademarkTokenFrequencyRepository.save(trademarkTokenFrequency);
        return trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);
    }

    /**
     * Update a trademarkTokenFrequency.
     *
     * @param trademarkTokenFrequencyDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkTokenFrequencyDTO update(TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO) {
        LOG.debug("Request to update TrademarkTokenFrequency : {}", trademarkTokenFrequencyDTO);
        TrademarkTokenFrequency trademarkTokenFrequency = trademarkTokenFrequencyMapper.toEntity(trademarkTokenFrequencyDTO);
        trademarkTokenFrequency = trademarkTokenFrequencyRepository.save(trademarkTokenFrequency);
        return trademarkTokenFrequencyMapper.toDto(trademarkTokenFrequency);
    }

    /**
     * Partially update a trademarkTokenFrequency.
     *
     * @param trademarkTokenFrequencyDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkTokenFrequencyDTO> partialUpdate(TrademarkTokenFrequencyDTO trademarkTokenFrequencyDTO) {
        LOG.debug("Request to partially update TrademarkTokenFrequency : {}", trademarkTokenFrequencyDTO);

        return trademarkTokenFrequencyRepository
            .findById(trademarkTokenFrequencyDTO.getId())
            .map(existingTrademarkTokenFrequency -> {
                trademarkTokenFrequencyMapper.partialUpdate(existingTrademarkTokenFrequency, trademarkTokenFrequencyDTO);

                return existingTrademarkTokenFrequency;
            })
            .map(trademarkTokenFrequencyRepository::save)
            .map(trademarkTokenFrequencyMapper::toDto);
    }

    /**
     * Get all the trademarkTokenFrequencies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrademarkTokenFrequencyDTO> findAll() {
        LOG.debug("Request to get all TrademarkTokenFrequencies");
        return trademarkTokenFrequencyRepository
            .findAll()
            .stream()
            .map(trademarkTokenFrequencyMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trademarkTokenFrequency by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkTokenFrequencyDTO> findOne(Long id) {
        LOG.debug("Request to get TrademarkTokenFrequency : {}", id);
        return trademarkTokenFrequencyRepository.findById(id).map(trademarkTokenFrequencyMapper::toDto);
    }

    /**
     * Delete the trademarkTokenFrequency by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrademarkTokenFrequency : {}", id);
        trademarkTokenFrequencyRepository.deleteById(id);
    }
}
