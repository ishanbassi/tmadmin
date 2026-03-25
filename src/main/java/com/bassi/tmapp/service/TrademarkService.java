package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.repository.TokenPhoneticRepository;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.repository.TrademarkTokenRepository;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
import com.bassi.tmapp.service.dto.PartialTokenPhoneticDto;
import com.bassi.tmapp.service.dto.PartialTrademarkTokenDto;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkSimilarityCandidateDto;
import com.bassi.tmapp.service.dto.TrademarkSimilarityCandidateWithPubTmDto;
import com.bassi.tmapp.service.dto.TrademarkSuggestionDto;
import com.bassi.tmapp.service.dto.UserProfileDTO;
import com.bassi.tmapp.service.extended.TmAgentServiceExtended;
import com.bassi.tmapp.service.extended.WordSanitizationService;
import com.bassi.tmapp.service.extended.dto.TrademarkWithLogoDto;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import com.bassi.tmapp.service.webScraping.TrademarkScrapingService;
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
import org.springframework.transaction.annotation.Propagation;
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

    private static final double MIN_SCORE_THRESHOLD = 0.7;

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
        trademarkRepository
            .findById(trademarkDTO.getId())
            .map(existingTrademark -> {
                String oldName = existingTrademark.getName();
                trademarkMapper.partialUpdate(existingTrademark, trademarkDTO);
                if (!Objects.equals(oldName, trademarkDTO.getName())) {
                    if (trademarkDTO.getName() != null) {
                        trademarkDTO.setNormalizedName(wordSanitizationService.sanitizeWord(trademarkDTO.getName()));
                    }
                    trademarkTokenService.recreateTokens(trademarkMapper.toEntity(trademarkDTO));
                }
                return existingTrademark;
            });
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
                    if (trademarkDTO.getName() != null) {
                        trademarkDTO.setNormalizedName(wordSanitizationService.sanitizeWord(trademarkDTO.getName()));
                    }
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

    public void saveAllTrademarksAndGenerateTokensInBatch(List<Trademark> trademarks) {
        trademarkTokenService.saveTokensAndGeneratePhoneticCodeInBatch(trademarks);
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
    public List<TrademarkSimilarityCandidateWithPubTmDto> runWeeklyComparison(int journalNo) {
        List<TrademarkSimilarityCandidateWithPubTmDto> pairs = trademarkRepository.findAllCandidatePairs(journalNo);
        Set<Long> allTrademarkIds = new HashSet<>();

        for (TrademarkSimilarityCandidateWithPubTmDto pair : pairs) {
            allTrademarkIds.add(pair.getClientId());
            allTrademarkIds.add(pair.getPublishedId());
        }

        List<PartialTrademarkTokenDto> allTokens = trademarkTokenRepository.findByTrademarkIds(allTrademarkIds);

        List<PartialTokenPhoneticDto> allPhonetics = tokenPhoneticRepository.findByTrademarkIds(allTrademarkIds);

        Map<Long, List<PartialTrademarkTokenDto>> tokensByTmId = allTokens.stream().collect(Collectors.groupingBy(t -> t.getTrademarkId()));

        Map<Long, List<PartialTokenPhoneticDto>> phoneticsByTmId = allPhonetics
            .stream()
            .collect(Collectors.groupingBy(p -> p.getTrademarkId()));

        List<TrademarkSimilarityCandidateWithPubTmDto> similiarityResultDTOs = new ArrayList<>();

        Map<Long, Trademark> cache = new HashMap<>();

        for (TrademarkSimilarityCandidateWithPubTmDto row : pairs) {
            Long clientId = row.getClientId();
            Long publishedId = row.getPublishedId();

            List<PartialTrademarkTokenDto> clientTokens = tokensByTmId.getOrDefault(clientId, List.of());

            List<PartialTrademarkTokenDto> publishedTokens = tokensByTmId.getOrDefault(publishedId, List.of());

            List<PartialTokenPhoneticDto> clientPhonetics = phoneticsByTmId.getOrDefault(clientId, List.of());

            List<PartialTokenPhoneticDto> publishedPhonetics = phoneticsByTmId.getOrDefault(publishedId, List.of());

            double score = similarityScorerService.computeFinalScore(
                row.getClientNormalizedTrademark(),
                row.getPublishedNormalizedTrademark(),
                clientTokens,
                publishedTokens,
                clientPhonetics,
                publishedPhonetics
            );
            if (score >= MIN_SCORE_THRESHOLD) {
                row.setScore(score);
                row.setJournalNum(journalNo);

                similiarityResultDTOs.add(row);
            }
        }

        similiarityResultDTOs.sort(
            Comparator.comparingDouble(TrademarkSimilarityCandidateWithPubTmDto::getScore)
                .reversed()
                .thenComparing(r -> r.getPublishedTrademarkClass())
        );
        return similiarityResultDTOs;
    }

    public List<TrademarkSimilarityCandidateDto> findSimiliarTrademarks(String trademark) {
        Trademark tm = new Trademark();
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        tm.setName(trademark);
        tm.setNormalizedName(sanitizedTrademark);
        List<PartialTrademarkTokenDto> searchTT = trademarkTokenService.generatePartialTrademarkTokens(trademark);
        List<PartialTokenPhoneticDto> searchTP = trademarkTokenService.generatePartialTrademarkTokenPhonetics(trademark);
        List<String> phonetics = searchTP.stream().map(PartialTokenPhoneticDto::getPhoneticCode).toList();
        List<TrademarkSimilarityCandidateDto> candidates = trademarkRepository.findCandidatePublishedIds(phonetics, sanitizedTrademark);
        if (candidates.isEmpty()) {
            return List.of();
        }

        Set<Long> allTrademarkIds = new HashSet<>();

        for (TrademarkSimilarityCandidateDto candidate : candidates) {
            allTrademarkIds.add(candidate.getId());
        }
        List<PartialTrademarkTokenDto> allTokens = trademarkTokenRepository.findByTrademarkIds(allTrademarkIds);

        List<PartialTokenPhoneticDto> allPhonetics = tokenPhoneticRepository.findByTrademarkIds(allTrademarkIds);

        Map<Long, List<PartialTrademarkTokenDto>> tokensByTmId = allTokens.stream().collect(Collectors.groupingBy(t -> t.getTrademarkId()));

        Map<Long, List<PartialTokenPhoneticDto>> phoneticsByTmId = allPhonetics
            .stream()
            .collect(Collectors.groupingBy(p -> p.getTrademarkId()));

        Map<Long, Trademark> cache = new HashMap<>();
        List<TrademarkSimilarityCandidateDto> similiarityResultDTOs = new ArrayList<>();
        for (TrademarkSimilarityCandidateDto candidate : candidates) {
            List<PartialTrademarkTokenDto> savedTT = tokensByTmId.getOrDefault(candidate.getId(), List.of());
            List<PartialTokenPhoneticDto> savedTP = phoneticsByTmId.getOrDefault(candidate.getId(), List.of());

            double score = similarityScorerService.computeFinalScore(
                tm.getNormalizedName(),
                candidate.getNormalizedName(),
                searchTT,
                savedTT,
                searchTP,
                savedTP
            );
            if (score >= MIN_SCORE_THRESHOLD) {
                candidate.setScore(score);
                similiarityResultDTOs.add(candidate);
            }
        }

        similiarityResultDTOs = similiarityResultDTOs
            .stream()
            .map(t -> {
                String slug = SlugUtil.generate(t.getName(), t.getTmClass(), t.getApplicationNo(), t.getType());

                t.setUrl("/trademarks/" + slug);
                return t;
            })
            .sorted(
                Comparator.comparingDouble(TrademarkSimilarityCandidateDto::getScore)
                    .reversed()
                    .thenComparing(TrademarkSimilarityCandidateDto::getTmClass)
            )
            .collect(Collectors.toList());

        return similiarityResultDTOs;
    }

    public List<TrademarkSuggestionDto> findLiveSuggestions(String trademark, Integer limit) {
        String sanitizedTrademark = this.wordSanitizationService.sanitizeWord(trademark);
        if (sanitizedTrademark == null || sanitizedTrademark.length() < 2) {
            return List.of();
        }
        return trademarkRepository
            .findLiveSuggestionsByLimit(sanitizedTrademark, limit)
            .stream()
            .map(tm -> {
                String slug = SlugUtil.generate(tm.getName(), tm.getTmClass(), tm.getApplicationNo(), tm.getType());

                tm.setUrl("/trademarks/" + slug);
                return tm;
            })
            .toList();
    }

    public List<Integer> getJournalNumbers() {
        return trademarkRepository.getJournalNumbers();
    }

    public void updateNormalizedNamesForMissingTrademarks(Integer journalNo) {
        List<Trademark> tms = trademarkRepository
            .findByJournalNoAndNullNormalizedName(journalNo)
            .stream()
            .map(tm -> {
                if (tm.getName() != null) {
                    tm.setNormalizedName(wordSanitizationService.sanitizeWord(tm.getName()));
                }
                return tm;
            })
            .toList();

        trademarkRepository.saveAll(tms);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateNameAndTrademarkStatusByIdOrApplicationNo(String name, String statusText, Trademark tm) {
        String sanatizeName = wordSanitizationService.sanitizeWord(statusText);
        tm.setName(name);
        tm.setNormalizedName(sanatizeName);
        tm.setTrademarkStatus(statusText);
        trademarkRepository.save(tm);
        trademarkTokenService.saveTokensAndGeneratePhoneticCode(tm);
    }
}
