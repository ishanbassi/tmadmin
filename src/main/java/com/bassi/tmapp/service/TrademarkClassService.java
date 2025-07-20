package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.repository.TrademarkClassRepository;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.mapper.TrademarkClassMapper;
import java.util.List;
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

    private final TrademarkClassMapper trademarkClassMapper;

    public TrademarkClassService(TrademarkClassRepository trademarkClassRepository, TrademarkClassMapper trademarkClassMapper) {
        this.trademarkClassRepository = trademarkClassRepository;
        this.trademarkClassMapper = trademarkClassMapper;
    }

    /**
     * Save a trademarkClass.
     *
     * @param trademarkClassDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkClassDTO save(TrademarkClassDTO trademarkClassDTO) {
        LOG.debug("Request to save TrademarkClass : {}", trademarkClassDTO);
        TrademarkClass trademarkClass = trademarkClassMapper.toEntity(trademarkClassDTO);
        trademarkClass = trademarkClassRepository.save(trademarkClass);
        return trademarkClassMapper.toDto(trademarkClass);
    }

    /**
     * Update a trademarkClass.
     *
     * @param trademarkClassDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkClassDTO update(TrademarkClassDTO trademarkClassDTO) {
        LOG.debug("Request to update TrademarkClass : {}", trademarkClassDTO);
        TrademarkClass trademarkClass = trademarkClassMapper.toEntity(trademarkClassDTO);
        trademarkClass = trademarkClassRepository.save(trademarkClass);
        return trademarkClassMapper.toDto(trademarkClass);
    }

    /**
     * Partially update a trademarkClass.
     *
     * @param trademarkClassDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkClassDTO> partialUpdate(TrademarkClassDTO trademarkClassDTO) {
        LOG.debug("Request to partially update TrademarkClass : {}", trademarkClassDTO);

        return trademarkClassRepository
            .findById(trademarkClassDTO.getId())
            .map(existingTrademarkClass -> {
                trademarkClassMapper.partialUpdate(existingTrademarkClass, trademarkClassDTO);

                return existingTrademarkClass;
            })
            .map(trademarkClassRepository::save)
            .map(trademarkClassMapper::toDto);
    }

    /**
     * Get one trademarkClass by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkClassDTO> findOne(Long id) {
        LOG.debug("Request to get TrademarkClass : {}", id);
        return trademarkClassRepository.findById(id).map(trademarkClassMapper::toDto);
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

    public List<TrademarkClassDTO> saveAll(List<TrademarkClassDTO> trademarkClassDTO) {
        LOG.debug("Request to save TrademarkClass : {}", trademarkClassDTO);
        List<TrademarkClass> trademarkClass = trademarkClassMapper.toEntity(trademarkClassDTO);
        trademarkClass = trademarkClassRepository.saveAll(trademarkClass);
        return trademarkClassMapper.toDto(trademarkClass);
    }
}
