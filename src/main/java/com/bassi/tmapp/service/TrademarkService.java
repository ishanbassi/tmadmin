package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkSimiliarityResultDTO;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import com.bassi.tmapp.service.extended.TmAgentServiceExtended;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.extended.dto.TrademarkWithLogoDto;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.LongFilter;

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

    private final CurrentUserService currentUserService;

    private final TrademarkQueryService trademarkQueryService;

    private final UserProfileService userProfileService;

    private final TrademarkTokenService trademarkTokenService;

    private final WordSanitizationService wordSanitizationService;

    private final SimilarityScorerService similarityScorerService;

    private TmAgentServiceExtended agentServiceExtended;
    private final TokenPhoneticRepository tokenPhoneticRepository;
    private final TrademarkTokenRepository trademarkTokenRepository;

    private static final double MIN_SCORE_THRESHOLD = 0.65;

    public TrademarkService(
        TrademarkRepository trademarkRepository,
        TrademarkMapper trademarkMapper,
        DocumentsService documentsService,
        CurrentUserService currentUserService,
        TrademarkQueryService trademarkQueryService,
        UserProfileService userProfileService,
        TrademarkTokenService trademarkTokenService,
        WordSanitizationService wordSanitizationService,
        TmAgentServiceExtended agentServiceExtended,
        SimilarityScorerService similarityScorerService,
        TokenPhoneticRepository tokenPhoneticRepository,
        TrademarkTokenRepository trademarkTokenRepository
    ) {
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
        this.documentsService = documentsService;
        this.currentUserService = currentUserService;
        this.trademarkQueryService = trademarkQueryService;
        this.userProfileService = userProfileService;
        this.trademarkTokenService = trademarkTokenService;
        this.wordSanitizationService = wordSanitizationService;
        this.agentServiceExtended = agentServiceExtended;
        this.similarityScorerService = similarityScorerService;
        this.tokenPhoneticRepository = tokenPhoneticRepository;
        this.trademarkTokenRepository = trademarkTokenRepository;
    }

    /**
     * Save a trademark.
     *
     * @param trademarkDTO the entity to save.
     * @return the persisted entity.
     */
    public TrademarkDTO save(TrademarkDTO trademarkDTO) {
        LOG.debug("Request to save Trademark : {}", trademarkDTO);
        if (trademarkDTO.getUser() != null) {
            Optional<UserProfileDTO> userProfileOptional = userProfileService.findOne(trademarkDTO.getUser().getId());
            if (userProfileOptional.isPresent()) {
                if (trademarkDTO.getProprietorName() == null) {
                    trademarkDTO.setProprietorName(userProfileOptional.get().getFullName());
                }
                if (trademarkDTO.getEmail() == null) {
                    trademarkDTO.setEmail(userProfileOptional.get().getEmail());
                }
                if (trademarkDTO.getPhoneNumber() == null) {
                    trademarkDTO.setPhoneNumber(userProfileOptional.get().getPhoneNumber());
                }
                if (trademarkDTO.getName() != null) {
                    trademarkTokenService.saveTokensAndGeneratePhoneticCode(trademarkMapper.toEntity(trademarkDTO));
                }
            }
        }
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
                String oldName = existingTrademark.getName();
                trademarkMapper.partialUpdate(existingTrademark, trademarkDTO);
                if (!Objects.equals(oldName, trademarkDTO.getName())) {
                    trademarkTokenService.recreateTokens(trademarkMapper.toEntity(trademarkDTO));
                }
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

    public List<TrademarkDTO> getTrademarkForCurrentUser() {
        UserProfile userProfile = currentUserService.getCurrentUserProfile();
        List<Trademark> trademarks = trademarkRepository.findByUser(userProfile);
        return trademarkMapper.toDto(trademarks);
    }

    public List<TrademarkDTO> getTrademarkForCurrentUser(Boolean documents) {
        if (Boolean.FALSE.equals(documents)) {
            return getTrademarkForCurrentUser();
        }
        UserProfile userProfile = currentUserService.getCurrentUserProfile();
        TrademarkCriteria criteria = new TrademarkCriteria();
        LongFilter filter = new LongFilter();
        filter.setEquals(userProfile.getId());
        criteria.setUserId(filter);
        return trademarkQueryService.findByCriteria(criteria, Pageable.ofSize(20)).toList();
    }

    public void saveTrademarksAndGenerateTokens(List<Trademark> journalTrademarks) {
        List<Trademark> trademarks = trademarkRepository.saveAll(journalTrademarks);
        trademarkTokenService.saveTokensAndGeneratePhoneticCode(trademarks);
    }

    public void saveTrademarksAndGenerateTokens(Trademark tm, TrademarkSource trademarkSource) {
        tm.setSource(trademarkSource);
        if (tm.getName() != null) {
            tm.setNormalizedName(wordSanitizationService.sanitizeWord(tm.getName()));
        }
        Trademark trademarks = trademarkRepository.save(tm);
        trademarkTokenService.saveTokensAndGeneratePhoneticCode(trademarks);
        agentServiceExtended.saveTmAgentsFromTrademarks(trademarks);
    }

    @Transactional
    public List<TrademarkSimiliarityResultDTO> runWeeklyComparison(int journalNo) {
        List<Object[]> pairs = trademarkRepository.findAllCandidatePairs(journalNo);
        Set<Long> allTrademarkIds = new HashSet<>();

        for (Object[] pair : pairs) {
            allTrademarkIds.add((Long) pair[0]);
            allTrademarkIds.add((Long) pair[1]);
        }

        List<TrademarkToken> allTokens = trademarkTokenRepository.findByTrademarkIds(allTrademarkIds);

        List<TokenPhonetic> allPhonetics = tokenPhoneticRepository.findByTrademarkIds(allTrademarkIds);

        Map<Long, List<TrademarkToken>> tokensByTmId = allTokens.stream().collect(Collectors.groupingBy(t -> t.getTrademark().getId()));

        Map<Long, List<TokenPhonetic>> phoneticsByTmId = allPhonetics
            .stream()
            .collect(Collectors.groupingBy(p -> p.getTrademarkToken().getTrademark().getId()));

        List<TrademarkSimiliarityResultDTO> similiarityResultDTOs = new ArrayList<>();

        Map<Long, Trademark> cache = new HashMap<>();

        for (Object[] row : pairs) {
            Long clientId = ((Number) row[0]).longValue();
            Long publishedId = ((Number) row[1]).longValue();

            Trademark client = cache.computeIfAbsent(clientId, id -> trademarkRepository.findById(id).orElseThrow());

            Trademark published = cache.computeIfAbsent(publishedId, id -> trademarkRepository.findById(id).orElseThrow());

            List<TrademarkToken> clientTokens = tokensByTmId.getOrDefault(clientId, List.of());

            List<TrademarkToken> publishedTokens = tokensByTmId.getOrDefault(publishedId, List.of());

            List<TokenPhonetic> clientPhonetics = phoneticsByTmId.getOrDefault(clientId, List.of());

            List<TokenPhonetic> publishedPhonetics = phoneticsByTmId.getOrDefault(publishedId, List.of());

            double score = similarityScorerService.computeFinalScore(
                client,
                published,
                clientTokens,
                publishedTokens,
                clientPhonetics,
                publishedPhonetics
            );
            if (score > MIN_SCORE_THRESHOLD) {
                TrademarkSimiliarityResultDTO trademarkSimiliarityResultDTO = new TrademarkSimiliarityResultDTO(
                    client,
                    published,
                    score,
                    journalNo
                );

                similiarityResultDTOs.add(trademarkSimiliarityResultDTO);
            }
        }

        similiarityResultDTOs.sort(
            Comparator.comparingDouble(TrademarkSimiliarityResultDTO::getScore)
                .reversed()
                .thenComparing(r -> r.getPublishedTradmark().getTmClass())
        );
        return similiarityResultDTOs;
    }

    public List<TrademarkSimiliarityResultDTO> findSimiliarTrademarks(String trademark) {
        Trademark tm = new Trademark();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        tm.setName(trademark);
        tm.setNormalizedName(sanitizedTrademark);
        List<TrademarkToken> searchTT = trademarkTokenService.generateTrademarkTokens(trademark);
        List<TokenPhonetic> searchTP = trademarkTokenService.generateTrademarkTokenPhonetics(trademark);
        List<String> phonetics = searchTP.stream().map(TokenPhonetic::getPhoneticCode).toList();
        List<Long> candidateIds = trademarkRepository.findCandidatePublishedIds(phonetics, sanitizedTrademark);
        if (candidateIds.isEmpty()) {
            return List.of();
        }

        Set<Long> allTrademarkIds = new HashSet<>();

        for (Long candidateId : candidateIds) {
            allTrademarkIds.add(candidateId);
        }
        List<TrademarkToken> allTokens = trademarkTokenRepository.findByTrademarkIds(allTrademarkIds);

        List<TokenPhonetic> allPhonetics = tokenPhoneticRepository.findByTrademarkIds(allTrademarkIds);

        Map<Long, List<TrademarkToken>> tokensByTmId = allTokens.stream().collect(Collectors.groupingBy(t -> t.getTrademark().getId()));

        Map<Long, List<TokenPhonetic>> phoneticsByTmId = allPhonetics
            .stream()
            .collect(Collectors.groupingBy(p -> p.getTrademarkToken().getTrademark().getId()));

        Map<Long, Trademark> cache = new HashMap<>();
        List<TrademarkSimiliarityResultDTO> similiarityResultDTOs = new ArrayList<>();
        for (Long candidateId : candidateIds) {
            Trademark savedTm = cache.computeIfAbsent(candidateId, id -> trademarkRepository.findById(id).orElseThrow());
            List<TrademarkToken> savedTT = tokensByTmId.getOrDefault(candidateId, List.of());
            List<TokenPhonetic> savedTP = phoneticsByTmId.getOrDefault(candidateId, List.of());

            double score = similarityScorerService.computeFinalScore(tm, savedTm, searchTT, savedTT, searchTP, savedTP);
            if (score > MIN_SCORE_THRESHOLD) {
                TrademarkSimiliarityResultDTO trademarkSimiliarityResultDTO = new TrademarkSimiliarityResultDTO(tm, savedTm, score, null);

                similiarityResultDTOs.add(trademarkSimiliarityResultDTO);
            }
        }

        similiarityResultDTOs.sort(
            Comparator.comparingDouble(TrademarkSimiliarityResultDTO::getScore)
                .reversed()
                .thenComparing(r -> r.getPublishedTradmark().getTmClass())
        );
        return similiarityResultDTOs;
    }

    public List<TrademarkDTO> findLiveSuggestions(String trademark) {
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        if (sanitizedTrademark == null || sanitizedTrademark.length() < 2) {
            return List.of();
        }

        return trademarkMapper.toDto(trademarkRepository.findLiveSuggestions(sanitizedTrademark));
    }

    public List<Integer> getJournalNumbers() {
        return trademarkRepository.getJournalNumbers();
    }
}
