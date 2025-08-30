package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.extended.dto.TrademarkWithLogoDto;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.Trademark}.
 */
@Service
@Transactional
public class TrademarkService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkService.class);

    private final TrademarkRepository trademarkRepository;

    private final TrademarkMapper trademarkMapper;

    private final DocumentsService documentsService;

    public TrademarkService(TrademarkRepository trademarkRepository, TrademarkMapper trademarkMapper, DocumentsService documentsService) {
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
        this.documentsService = documentsService;
    }

    /**
     * Save a trademark.
     *
     * @param trademarkDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkDTO save(TrademarkDTO trademarkDTO) {
        LOG.debug("Request to save Trademark : {}", trademarkDTO);
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
        LOG.debug("Request to update Trademark : {}", trademarkDTO);
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
        LOG.debug("Request to partially update Trademark : {}", trademarkDTO);

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
     * Get all the trademarks with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TrademarkDTO> findAllWithEagerRelationships(Pageable pageable) {
        return trademarkRepository.findAllWithEagerRelationships(pageable).map(trademarkMapper::toDto);
    }

    /**
     * Get one trademark by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TrademarkDTO> findOne(Long id) {
        LOG.debug("Request to get Trademark : {}", id);
        return trademarkRepository.findOneWithEagerRelationships(id).map(trademarkMapper::toDto);
    }

    /**
     * Delete the trademark by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Trademark : {}", id);
        trademarkRepository.deleteById(id);
    }

    /**
     * Get one trademark by id with logo document.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public TrademarkWithLogoDto findOneWithLogo(Long id) {
        LOG.debug("Request to get Trademark : {}", id);
        Optional<TrademarkDTO> trademarkDtoOptional = trademarkRepository.findOneWithEagerRelationships(id).map(trademarkMapper::toDto);
        TrademarkWithLogoDto trademarkWithLogoDto = new TrademarkWithLogoDto();
        trademarkDtoOptional.ifPresent(trademarkDto -> {
            trademarkWithLogoDto.setTrademark(trademarkDto);
            documentsService.findByTrademark(trademarkMapper.toEntity(trademarkDto)).ifPresent(trademarkWithLogoDto::setDocument);
        });
        return trademarkWithLogoDto;
    }
}
