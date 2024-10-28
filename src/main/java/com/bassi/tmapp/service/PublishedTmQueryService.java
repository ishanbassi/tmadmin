package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.repository.PublishedTmRepository;
import com.bassi.tmapp.service.criteria.PublishedTmCriteria;
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
 * Service for executing complex queries for {@link PublishedTm} entities in the database.
 * The main input is a {@link PublishedTmCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PublishedTm} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PublishedTmQueryService extends QueryService<PublishedTm> {

    private static final Logger LOG = LoggerFactory.getLogger(PublishedTmQueryService.class);

    private final PublishedTmRepository publishedTmRepository;

    public PublishedTmQueryService(PublishedTmRepository publishedTmRepository) {
        this.publishedTmRepository = publishedTmRepository;
    }

    /**
     * Return a {@link Page} of {@link PublishedTm} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PublishedTm> findByCriteria(PublishedTmCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PublishedTm> specification = createSpecification(criteria);
        return publishedTmRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PublishedTmCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PublishedTm> specification = createSpecification(criteria);
        return publishedTmRepository.count(specification);
    }

    /**
     * Function to convert {@link PublishedTmCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PublishedTm> createSpecification(PublishedTmCriteria criteria) {
        Specification<PublishedTm> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PublishedTm_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PublishedTm_.name));
            }
            if (criteria.getDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetails(), PublishedTm_.details));
            }
            if (criteria.getApplicationNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApplicationNo(), PublishedTm_.applicationNo));
            }
            if (criteria.getApplicationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApplicationDate(), PublishedTm_.applicationDate));
            }
            if (criteria.getAgentName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgentName(), PublishedTm_.agentName));
            }
            if (criteria.getAgentAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAgentAddress(), PublishedTm_.agentAddress));
            }
            if (criteria.getProprietorName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProprietorName(), PublishedTm_.proprietorName));
            }
            if (criteria.getProprietorAddress() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getProprietorAddress(), PublishedTm_.proprietorAddress)
                );
            }
            if (criteria.getHeadOffice() != null) {
                specification = specification.and(buildSpecification(criteria.getHeadOffice(), PublishedTm_.headOffice));
            }
            if (criteria.getImgUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getImgUrl(), PublishedTm_.imgUrl));
            }
            if (criteria.getTmClass() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTmClass(), PublishedTm_.tmClass));
            }
            if (criteria.getJournalNo() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getJournalNo(), PublishedTm_.journalNo));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), PublishedTm_.deleted));
            }
            if (criteria.getUsage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUsage(), PublishedTm_.usage));
            }
            if (criteria.getAssociatedTms() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssociatedTms(), PublishedTm_.associatedTms));
            }
            if (criteria.getTrademarkStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getTrademarkStatus(), PublishedTm_.trademarkStatus));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), PublishedTm_.createdDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), PublishedTm_.modifiedDate));
            }
            if (criteria.getTmAgentId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTmAgentId(), root -> root.join(PublishedTm_.tmAgent, JoinType.LEFT).get(TmAgent_.id))
                );
            }
        }
        return specification;
    }
}
