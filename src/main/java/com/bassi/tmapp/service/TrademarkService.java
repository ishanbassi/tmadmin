package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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

    private final Logger log = LoggerFactory.getLogger(TrademarkService.class);

    private final TrademarkRepository trademarkRepository;

    private final TrademarkMapper trademarkMapper;

    public TrademarkService(TrademarkRepository trademarkRepository, TrademarkMapper trademarkMapper) {
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
    }

    /**
     * Save a trademark.
     *
     * @param trademarkDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkDTO save(TrademarkDTO trademarkDTO) {
        log.debug("Request to save Trademark : {}", trademarkDTO);
        Trademark trademark = trademarkMapper.toEntity(trademarkDTO);
        trademark = trademarkRepository.save(trademark);
        return trademarkMapper.toDto(trademark);
    }

    /**
     * Update a trademark.
     *
     * @param trademarkDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkDTO update(TrademarkDTO trademarkDTO) {
        log.debug("Request to update Trademark : {}", trademarkDTO);
        Trademark trademark = trademarkMapper.toEntity(trademarkDTO);
        trademark = trademarkRepository.save(trademark);
        return trademarkMapper.toDto(trademark);
    }

    /**
     * Partially update a trademark.
     *
     * @param trademarkDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkDTO> partialUpdate(TrademarkDTO trademarkDTO) {
        log.debug("Request to partially update Trademark : {}", trademarkDTO);

        return trademarkRepository
            .findById(trademarkDTO.getId())
            .map(existingTrademark -> {
                trademarkMapper.partialUpdate(existingTrademark, trademarkDTO);

                return existingTrademark;
            })
            .map(trademarkRepository::save)
            .map(trademarkMapper::toDto);
    }

    /**
     * Get all the trademarks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrademarkDTO> findAll() {
        log.debug("Request to get all Trademarks");
        return trademarkRepository.findAll().stream().map(trademarkMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trademark by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkDTO> findOne(Long id) {
        log.debug("Request to get Trademark : {}", id);
        return trademarkRepository.findById(id).map(trademarkMapper::toDto);
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
