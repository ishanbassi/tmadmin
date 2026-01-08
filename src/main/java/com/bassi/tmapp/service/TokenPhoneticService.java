package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.service.dto.TokenPhoneticDTO;
import com.bassi.tmapp.service.mapper.TokenPhoneticMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bassi.tmapp.domain.TokenPhonetic}.
 */
@Service
@Transactional
public class TokenPhoneticService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenPhoneticService.class);

    private final TokenPhoneticRepository tokenPhoneticRepository;

    private final TokenPhoneticMapper tokenPhoneticMapper;

    public TokenPhoneticService(TokenPhoneticRepository tokenPhoneticRepository, TokenPhoneticMapper tokenPhoneticMapper) {
        this.tokenPhoneticRepository = tokenPhoneticRepository;
        this.tokenPhoneticMapper = tokenPhoneticMapper;
    }

    /**
     * Save a tokenPhonetic.
     *
     * @param tokenPhoneticDTO the entity to save.
     * @return the persisted entity.
     */
    public TokenPhoneticDTO save(TokenPhoneticDTO tokenPhoneticDTO) {
        LOG.debug("Request to save TokenPhonetic : {}", tokenPhoneticDTO);
        TokenPhonetic tokenPhonetic = tokenPhoneticMapper.toEntity(tokenPhoneticDTO);
        tokenPhonetic = tokenPhoneticRepository.save(tokenPhonetic);
        return tokenPhoneticMapper.toDto(tokenPhonetic);
    }

    /**
     * Update a tokenPhonetic.
     *
     * @param tokenPhoneticDTO the entity to save.
     * @return the persisted entity.
     */
    public TokenPhoneticDTO update(TokenPhoneticDTO tokenPhoneticDTO) {
        LOG.debug("Request to update TokenPhonetic : {}", tokenPhoneticDTO);
        TokenPhonetic tokenPhonetic = tokenPhoneticMapper.toEntity(tokenPhoneticDTO);
        tokenPhonetic = tokenPhoneticRepository.save(tokenPhonetic);
        return tokenPhoneticMapper.toDto(tokenPhonetic);
    }

    /**
     * Partially update a tokenPhonetic.
     *
     * @param tokenPhoneticDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TokenPhoneticDTO> partialUpdate(TokenPhoneticDTO tokenPhoneticDTO) {
        LOG.debug("Request to partially update TokenPhonetic : {}", tokenPhoneticDTO);

        return tokenPhoneticRepository
            .findById(tokenPhoneticDTO.getId())
            .map(existingTokenPhonetic -> {
                tokenPhoneticMapper.partialUpdate(existingTokenPhonetic, tokenPhoneticDTO);

                return existingTokenPhonetic;
            })
            .map(tokenPhoneticRepository::save)
            .map(tokenPhoneticMapper::toDto);
    }

    /**
     * Get all the tokenPhonetics.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TokenPhoneticDTO> findAll() {
        LOG.debug("Request to get all TokenPhonetics");
        return tokenPhoneticRepository.findAll().stream().map(tokenPhoneticMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tokenPhonetic by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TokenPhoneticDTO> findOne(Long id) {
        LOG.debug("Request to get TokenPhonetic : {}", id);
        return tokenPhoneticRepository.findById(id).map(tokenPhoneticMapper::toDto);
    }

    /**
     * Delete the tokenPhonetic by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TokenPhonetic : {}", id);
        tokenPhoneticRepository.deleteById(id);
    }
}
