package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.repository.TrademarkRepository;
import com.bassi.tmapp.service.criteria.TrademarkCriteria;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.mapper.TrademarkMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Trademark} entities in the database.
 * The main input is a {@link TrademarkCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrademarkDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrademarkQueryService extends QueryService<Trademark> {

    private static final Logger log = LoggerFactory.getLogger(TrademarkQueryService.class);

    private final TrademarkRepository trademarkRepository;

    private final TrademarkMapper trademarkMapper;

    public TrademarkQueryService(TrademarkRepository trademarkRepository, TrademarkMapper trademarkMapper) {
        this.trademarkRepository = trademarkRepository;
        this.trademarkMapper = trademarkMapper;
    }

    /**
     * Return a {@link Page} of {@link TrademarkDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrademarkDTO> findByCriteria(TrademarkCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trademark> specification = createSpecification(criteria);
        return trademarkRepository.findAll(specification, page).map(trademarkMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrademarkCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Trademark> specification = createSpecification(criteria);
        return trademarkRepository.count(specification);
    }

    /**
     * Function to convert {@link TrademarkCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Trademark> createSpecification(TrademarkCriteria criteria) {
        Specification<Trademark> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Trademark_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Trademark_.name));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), Trademark_.details));
            }
            if (criteria.getApplicationNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApplicationNo(), Trademark_.applicationNo));
            }
            if (criteria.getApplicationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApplicationDate(), Trademark_.applicationDate));
            }
            if (criteria.getAgentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgentName(), Trademark_.agentName));
            }
            if (criteria.getAgentAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgentAddress(), Trademark_.agentAddress));
            }
            if (criteria.getProprietorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProprietorName(), Trademark_.proprietorName));
            }
            if (criteria.getProprietorAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProprietorAddress(), Trademark_.proprietorAddress));
            }
            if (criteria.getHeadOffice() != null) {
                specification = specification.and(buildSpecification(criteria.getHeadOffice(), Trademark_.headOffice));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), Trademark_.imgUrl));
            }
            if (criteria.getTmClass() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTmClass(), Trademark_.tmClass));
            }
            if (criteria.getJournalNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJournalNo(), Trademark_.journalNo));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Trademark_.deleted));
            }
            if (criteria.getUsage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsage(), Trademark_.usage));
            }
            if (criteria.getAssociatedTms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssociatedTms(), Trademark_.associatedTms));
            }
            if (criteria.getTrademarkStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTrademarkStatus(), Trademark_.trademarkStatus));
            }
            if (criteria.getTmAgentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTmAgentId(), root -> root.join(Trademark_.tmAgent, JoinType.LEFT).get(TmAgent_.id))
                );
            }
        }
        return specification;
    }
}
