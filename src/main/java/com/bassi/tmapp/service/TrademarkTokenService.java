package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.PublishedTmPhonetics;
import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.dto.PartialTokenPhoneticDto;
import com.bassi.tmapp.service.dto.PartialTrademarkTokenDto;
import com.bassi.tmapp.service.dto.PublishedTmPhoneticsDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.mapper.TrademarkTokenMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private final WordSanitizationService wordSanitizationService;

    private final TokenPhoneticService tokenPhoneticService;

    public TrademarkTokenService(
        TrademarkTokenRepository trademarkTokenRepository,
        TrademarkTokenMapper trademarkTokenMapper,
        WordSanitizationService wordSanitizationService,
        TokenPhoneticService tokenPhoneticService
    ) {
        this.trademarkTokenRepository = trademarkTokenRepository;
        this.trademarkTokenMapper = trademarkTokenMapper;
        this.wordSanitizationService = wordSanitizationService;
        this.tokenPhoneticService = tokenPhoneticService;
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

    public void saveTokensAndGeneratePhoneticCode(List<Trademark> trademarks) {
        for (Trademark tm : trademarks) {
            if (tm.getName() == null || tm.getName().isBlank()) {
                return;
            }
            saveTokens(tm);
        }
    }

    public void saveTokensAndGeneratePhoneticCode(Trademark tm) {
        if (tm.getName() == null || tm.getName().isBlank()) {
            return;
        }
        saveTokens(tm);
    }

    private void saveTokens(Trademark tm) {
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(tm.getName().trim());
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            // i+1 refers to the position of the word
            TrademarkToken token = generateTrademarkToken(subWords.get(i), tm, i + 1);
            token = trademarkTokenRepository.save(token);
            tokenPhoneticService.generateAndSavePhoneticToken(token);
        }
    }

    public List<TokenPhonetic> generateTrademarkTokenPhonetics(String trademark) {
        if (trademark == null) return Collections.emptyList();
        List<TokenPhonetic> tokenPhonetics = new ArrayList<>();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            TrademarkToken token = generateTrademarkToken(subWords.get(i), null, i + 1);
            TokenPhonetic tokenPhonetic = tokenPhoneticService.generatePhoneticToken(token);
            tokenPhonetics.add(tokenPhonetic);
        }
        return tokenPhonetics;
    }

    public List<PartialTokenPhoneticDto> generatePartialTrademarkTokenPhonetics(String trademark) {
        if (trademark == null) return Collections.emptyList();
        List<PartialTokenPhoneticDto> tokenPhonetics = new ArrayList<>();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            PartialTrademarkTokenDto token = generatePartialTrademarkToken(subWords.get(i), null, i + 1);
            PartialTokenPhoneticDto tokenPhonetic = tokenPhoneticService.generatePartialPhoneticToken(token);
            tokenPhonetics.add(tokenPhonetic);
        }
        return tokenPhonetics;
    }

    public List<TrademarkToken> generateTrademarkTokens(String trademark) {
        if (trademark == null) return Collections.emptyList();
        List<TrademarkToken> trademarkTokens = new ArrayList<>();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            TrademarkToken token = generateTrademarkToken(subWords.get(i), null, i + 1);
            trademarkTokens.add(token);
        }
        return trademarkTokens;
    }

    public List<PartialTrademarkTokenDto> generatePartialTrademarkTokens(String trademark) {
        if (trademark == null) return Collections.emptyList();
        List<PartialTrademarkTokenDto> trademarkTokens = new ArrayList<>();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            PartialTrademarkTokenDto token = generatePartialTrademarkToken(subWords.get(i), null, i + 1);
            trademarkTokens.add(token);
        }
        return trademarkTokens;
    }

    private TrademarkToken generateTrademarkToken(String word, Trademark tm, int position) {
        TrademarkToken token = new TrademarkToken();
        token.setTokenText(word);
        token.setTrademark(tm);
        if (word.length() <= 2) {
            token.setTokenType(TrademarkTokenType.DESCRIPTIVE);
        } else {
            token.setTokenType(TrademarkTokenType.CORE);
        }

        token.setPosition(position);
        return token;
    }

    private PartialTrademarkTokenDto generatePartialTrademarkToken(String word, Trademark tm, int position) {
        PartialTrademarkTokenDto token = new PartialTrademarkTokenDto();
        token.setTokenText(word);
        token.setTrademarkId(tm == null ? null : tm.getId());
        if (word.length() <= 2) {
            token.setTokenType(TrademarkTokenType.DESCRIPTIVE);
        } else {
            token.setTokenType(TrademarkTokenType.CORE);
        }

        token.setPosition(position);

        return token;
    }

    public void recreateTokens(Trademark trademark) {
        trademarkTokenRepository.deleteByTrademark(trademark.getId());
        saveTokensAndGeneratePhoneticCode(trademark);
    }
}
