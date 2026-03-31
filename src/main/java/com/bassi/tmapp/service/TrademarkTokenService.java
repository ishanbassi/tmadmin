package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.repository.TrademarkTokenFrequencyRepository;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.constants.StopWords;
import com.bassi.tmapp.service.dto.PartialTokenPhoneticDto;
import com.bassi.tmapp.service.dto.PartialTrademarkTokenDto;
import com.bassi.tmapp.service.dto.TrademarkTokenDTO;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.mapper.TrademarkTokenMapper;
import com.google.common.collect.Lists;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing
 * {@link com.bassi.tmapp.domain.TrademarkToken}.
 */
@Service
@Transactional
public class TrademarkTokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkTokenService.class);
    private static final int BATCH_SIZE = 500;
    private static final int THREAD_POOL_SIZE = 4;

    private final TrademarkTokenRepository trademarkTokenRepository;

    private final TrademarkTokenMapper trademarkTokenMapper;

    private final WordSanitizationService wordSanitizationService;

    private final TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository;

    private final TokenPhoneticService tokenPhoneticService;
    private final TrademarkRepository trademarkRepository;
    private final TrademarkQueryService trademarkQueryService;
    private final TokenPhoneticRepository tokenPhoneticRepository;
    private final EntityManager entityManager;

    @Lazy
    @Autowired
    private TrademarkTokenService self;

    public TrademarkTokenService(
        TrademarkTokenRepository trademarkTokenRepository,
        TrademarkTokenMapper trademarkTokenMapper,
        WordSanitizationService wordSanitizationService,
        TokenPhoneticService tokenPhoneticService,
        TrademarkRepository trademarkRepository,
        EntityManager entityManager,
        TrademarkQueryService trademarkQueryService,
        TokenPhoneticRepository tokenPhoneticRepository,
        TrademarkTokenFrequencyRepository trademarkTokenFrequencyRepository
    ) {
        this.trademarkTokenRepository = trademarkTokenRepository;
        this.trademarkTokenMapper = trademarkTokenMapper;
        this.wordSanitizationService = wordSanitizationService;
        this.tokenPhoneticService = tokenPhoneticService;
        this.trademarkRepository = trademarkRepository;
        this.tokenPhoneticRepository = tokenPhoneticRepository;
        this.trademarkQueryService = trademarkQueryService;
        this.entityManager = entityManager;
        this.trademarkTokenFrequencyRepository = trademarkTokenFrequencyRepository;
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

    public List<TrademarkToken> generateTrademarkTokens(Trademark trademark) {
        if (trademark == null || trademark.getName() == null) return Collections.emptyList();
        List<TrademarkToken> trademarkTokens = new ArrayList<>();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark.getName());
        List<String> subWords = Arrays.asList(sanitizedTrademark.split(" "));
        for (int i = 0; i < subWords.size(); i++) {
            TrademarkToken token = generateTrademarkToken(subWords.get(i), trademark, i + 1);
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
        if (word.length() <= 2 || trademarkTokenFrequencyRepository.findByWord(word) != null) {
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
        if (word.length() <= 2 || trademarkTokenFrequencyRepository.findByWord(word) != null) {
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

    public void deleteByTrademarkJournal(Integer journalNo) {
        trademarkTokenRepository.deleteByTrademarkJournal(journalNo);
    }

    //	@Async
    //	public void createByTrademarkJournal(Integer journalNo) {
    //		int batchSize = 1000;
    //		TrademarkCriteria criteria = new TrademarkCriteria();
    //		IntegerFilter journalFilter = new IntegerFilter();
    //		journalFilter.setEquals(journalNo);
    //		criteria.setJournalNo(journalFilter);
    //		Pageable pageable = PageRequest.of(0, batchSize);
    //		Page<Trademark> page = trademarkQueryService.findTrademarkByCriteria(criteria, pageable);
    //		while (page.hasContent()) {
    //			for (Trademark trademark: page.getContent()) {
    //				saveTokensAndGeneratePhoneticCode(trademark);
    //			}
    //			if (page.hasNext()) {
    //				page = trademarkQueryService.findTrademarkByCriteria(criteria, page.nextPageable());
    //			} else {
    //				break;
    //			}
    //		}
    //
    //	}
    public void createByTrademarkJournalV2(Integer journalNo) {
        List<Trademark> tms = trademarkRepository.findTrademarksByJournalNo(journalNo);
        self.processBatch(tms);
    }

    public void createByTrademarkJournal(Integer journalNo) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Long lastSeenId = 0L;
        List<Trademark> batch;

        try {
            do {
                // ✅ Keyset pagination — no OFFSET cost, consistent O(log n) per page
                batch = trademarkRepository.findByJournalNoAndIdGreaterThan(journalNo, lastSeenId, PageRequest.of(0, BATCH_SIZE));

                if (batch.isEmpty()) break;

                // ✅ Capture for lambda, update cursor
                final List<Trademark> currentBatch = List.copyOf(batch);
                lastSeenId = batch.get(batch.size() - 1).getId();

                // ✅ Each batch processed in parallel on thread pool
                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> self.processBatch(currentBatch), executor);
                futures.add(future);
            } while (batch.size() == BATCH_SIZE);

            // ✅ Wait for all batches to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        } finally {
            executor.shutdown();
        }
    }

    @Transactional
    public void processBatch(List<Trademark> trademarks) {
        // ── Step 1: Build tokens (pure CPU, no DB) ─────────────────────────
        List<TrademarkToken> tokens = trademarks.stream().flatMap(tm -> generateTrademarkTokens(tm).stream()).collect(Collectors.toList());

        // ── Step 2: Bulk save tokens, clear cache immediately ──────────────
        List<TrademarkToken> savedTokens = trademarkTokenRepository.saveAll(tokens);
        entityManager.flush();
        entityManager.clear(); // ✅ free RAM after token save

        // ── Step 3: Generate phonetics from saved tokens (has IDs now) ─────
        List<TokenPhonetic> tpList = savedTokens
            .parallelStream() // ✅ parallel if CPU heavy
            .map(tokenPhoneticService::generatePhoneticToken)
            .collect(Collectors.toList());

        // ── Step 4: Bulk save phonetics, clear cache again ─────────────────
        tokenPhoneticRepository.saveAll(tpList);
        entityManager.flush();
        entityManager.clear(); // ✅ free RAM after phonetic save
    }

    public void saveTokensAndGeneratePhoneticCodeInBatch(List<Trademark> trademarks) {
        List<Trademark> tms = trademarkRepository.saveAll(trademarks);
        entityManager.flush();
        entityManager.clear();
        Lists.partition(tms, BATCH_SIZE).forEach(batch -> self.processBatch(batch));
    }
}
