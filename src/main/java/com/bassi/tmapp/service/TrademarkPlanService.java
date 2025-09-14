package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TrademarkPlan;
import com.bassi.tmapp.repository.TrademarkPlanRepository;
import com.bassi.tmapp.service.dto.TrademarkPlanDTO;
import com.bassi.tmapp.service.mapper.TrademarkPlanMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TrademarkPlan}.
 */
@Service
@Transactional
public class TrademarkPlanService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkPlanService.class);

    private final TrademarkPlanRepository trademarkPlanRepository;

    private final TrademarkPlanMapper trademarkPlanMapper;

    public TrademarkPlanService(TrademarkPlanRepository trademarkPlanRepository, TrademarkPlanMapper trademarkPlanMapper) {
        this.trademarkPlanRepository = trademarkPlanRepository;
        this.trademarkPlanMapper = trademarkPlanMapper;
    }

    /**
     * Save a trademarkPlan.
     *
     * @param trademarkPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkPlanDTO save(TrademarkPlanDTO trademarkPlanDTO) {
        LOG.debug("Request to save TrademarkPlan : {}", trademarkPlanDTO);
        TrademarkPlan trademarkPlan = trademarkPlanMapper.toEntity(trademarkPlanDTO);
        trademarkPlan = trademarkPlanRepository.save(trademarkPlan);
        return trademarkPlanMapper.toDto(trademarkPlan);
    }

    /**
     * Update a trademarkPlan.
     *
     * @param trademarkPlanDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkPlanDTO update(TrademarkPlanDTO trademarkPlanDTO) {
        LOG.debug("Request to update TrademarkPlan : {}", trademarkPlanDTO);
        TrademarkPlan trademarkPlan = trademarkPlanMapper.toEntity(trademarkPlanDTO);
        trademarkPlan = trademarkPlanRepository.save(trademarkPlan);
        return trademarkPlanMapper.toDto(trademarkPlan);
    }

    /**
     * Partially update a trademarkPlan.
     *
     * @param trademarkPlanDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkPlanDTO> partialUpdate(TrademarkPlanDTO trademarkPlanDTO) {
        LOG.debug("Request to partially update TrademarkPlan : {}", trademarkPlanDTO);

        return trademarkPlanRepository
            .findById(trademarkPlanDTO.getId())
            .map(existingTrademarkPlan -> {
                trademarkPlanMapper.partialUpdate(existingTrademarkPlan, trademarkPlanDTO);

                return existingTrademarkPlan;
            })
            .map(trademarkPlanRepository::save)
            .map(trademarkPlanMapper::toDto);
    }

    /**
     * Get all the trademarkPlans.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrademarkPlanDTO> findAll() {
        LOG.debug("Request to get all TrademarkPlans");
        return trademarkPlanRepository.findAll().stream().map(trademarkPlanMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trademarkPlan by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkPlanDTO> findOne(Long id) {
        LOG.debug("Request to get TrademarkPlan : {}", id);
        return trademarkPlanRepository.findById(id).map(trademarkPlanMapper::toDto);
    }

    /**
     * Delete the trademarkPlan by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrademarkPlan : {}", id);
        trademarkPlanRepository.deleteById(id);
    }
}
