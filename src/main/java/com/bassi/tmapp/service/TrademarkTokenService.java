package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import com.bassi.tmapp.service.mapper.TrademarkTokenMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TrademarkToken}.
 */
@Service
@Transactional
public class TrademarkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkTokenService.class);

    private final TrademarkTokenRepository trademarkTokenRepository;

    private final TrademarkTokenMapper trademarkTokenMapper;

    public TrademarkTokenService(TrademarkTokenRepository trademarkTokenRepository, TrademarkTokenMapper trademarkTokenMapper) {
        this.trademarkTokenRepository = trademarkTokenRepository;
        this.trademarkTokenMapper = trademarkTokenMapper;
    }

    /**
     * Save a trademarkToken.
     *
     * @param trademarkTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkTokenDTO save(TrademarkTokenDTO trademarkTokenDTO) {
        LOG.debug("Request to save TrademarkToken : {}", trademarkTokenDTO);
        TrademarkToken trademarkToken = trademarkTokenMapper.toEntity(trademarkTokenDTO);
        trademarkToken = trademarkTokenRepository.save(trademarkToken);
        return trademarkTokenMapper.toDto(trademarkToken);
    }

    /**
     * Update a trademarkToken.
     *
     * @param trademarkTokenDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkTokenDTO update(TrademarkTokenDTO trademarkTokenDTO) {
        LOG.debug("Request to update TrademarkToken : {}", trademarkTokenDTO);
        TrademarkToken trademarkToken = trademarkTokenMapper.toEntity(trademarkTokenDTO);
        trademarkToken = trademarkTokenRepository.save(trademarkToken);
        return trademarkTokenMapper.toDto(trademarkToken);
    }

    /**
     * Partially update a trademarkToken.
     *
     * @param trademarkTokenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TrademarkTokenDTO> partialUpdate(TrademarkTokenDTO trademarkTokenDTO) {
        LOG.debug("Request to partially update TrademarkToken : {}", trademarkTokenDTO);

        return trademarkTokenRepository
            .findById(trademarkTokenDTO.getId())
            .map(existingTrademarkToken -> {
                trademarkTokenMapper.partialUpdate(existingTrademarkToken, trademarkTokenDTO);

                return existingTrademarkToken;
            })
            .map(trademarkTokenRepository::save)
            .map(trademarkTokenMapper::toDto);
    }

    /**
     * Get all the trademarkTokens.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TrademarkTokenDTO> findAll() {
        LOG.debug("Request to get all TrademarkTokens");
        return trademarkTokenRepository
            .findAll()
            .stream()
            .map(trademarkTokenMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one trademarkToken by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkTokenDTO> findOne(Long id) {
        LOG.debug("Request to get TrademarkToken : {}", id);
        return trademarkTokenRepository.findById(id).map(trademarkTokenMapper::toDto);
    }

    /**
     * Delete the trademarkToken by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TrademarkToken : {}", id);
        trademarkTokenRepository.deleteById(id);
    }
}
