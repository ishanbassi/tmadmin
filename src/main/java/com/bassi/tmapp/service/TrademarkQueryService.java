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

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkQueryService.class);

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
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Trademark> specification = createSpecification(criteria);
        return trademarkRepository.fetchBagRelationships(trademarkRepository.findAll(specification, page)).map(trademarkMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrademarkCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
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
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Trademark_.id),
                buildStringSpecification(criteria.getName(), Trademark_.name),
                buildStringSpecification(criteria.getDetails(), Trademark_.details),
                buildRangeSpecification(criteria.getApplicationNo(), Trademark_.applicationNo),
                buildRangeSpecification(criteria.getApplicationDate(), Trademark_.applicationDate),
                buildStringSpecification(criteria.getAgentName(), Trademark_.agentName),
                buildStringSpecification(criteria.getAgentAddress(), Trademark_.agentAddress),
                buildStringSpecification(criteria.getProprietorName(), Trademark_.proprietorName),
                buildStringSpecification(criteria.getProprietorAddress(), Trademark_.proprietorAddress),
                buildStringSpecification(criteria.getHeadOffice(), Trademark_.headOffice),
                buildStringSpecification(criteria.getImgUrl(), Trademark_.imgUrl),
                buildRangeSpecification(criteria.getTmClass(), Trademark_.tmClass),
                buildRangeSpecification(criteria.getJournalNo(), Trademark_.journalNo),
                buildSpecification(criteria.getDeleted(), Trademark_.deleted),
                buildStringSpecification(criteria.getUsage(), Trademark_.usage),
                buildStringSpecification(criteria.getAssociatedTms(), Trademark_.associatedTms),
                buildStringSpecification(criteria.getTrademarkStatus(), Trademark_.trademarkStatus),
                buildRangeSpecification(criteria.getCreatedDate(), Trademark_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), Trademark_.modifiedDate),
                buildRangeSpecification(criteria.getRenewalDate(), Trademark_.renewalDate),
                buildSpecification(criteria.getType(), Trademark_.type),
                buildRangeSpecification(criteria.getPageNo(), Trademark_.pageNo),
                buildSpecification(criteria.getSource(), Trademark_.source),
                buildStringSpecification(criteria.getPhoneNumber(), Trademark_.phoneNumber),
                buildStringSpecification(criteria.getEmail(), Trademark_.email),
                buildStringSpecification(criteria.getOrganizationType(), Trademark_.organizationType),
                buildStringSpecification(criteria.getNormalizedName(), Trademark_.normalizedName),
                buildStringSpecification(criteria.getFilingMode(), Trademark_.filingMode),
                buildStringSpecification(criteria.getState(), Trademark_.state),
                buildStringSpecification(criteria.getCountry(), Trademark_.country),
                buildSpecification(criteria.getLeadId(), root -> root.join(Trademark_.lead, JoinType.LEFT).get(Lead_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(Trademark_.user, JoinType.LEFT).get(UserProfile_.id)),
                buildSpecification(criteria.getTrademarkPlanId(), root ->
                    root.join(Trademark_.trademarkPlan, JoinType.LEFT).get(TrademarkPlan_.id)
                ),
                buildSpecification(criteria.getTmAgentId(), root -> root.join(Trademark_.tmAgent, JoinType.LEFT).get(TmAgent_.id)),
                buildSpecification(criteria.getTrademarkClassesId(), root ->
                    root.join(Trademark_.trademarkClasses, JoinType.LEFT).get(TrademarkClass_.id)
                ),
                buildSpecification(criteria.getDocumentsId(), root -> root.join(Trademark_.documents, JoinType.LEFT).get(Documents_.id))
            );
        }
        return specification;
    }
}
