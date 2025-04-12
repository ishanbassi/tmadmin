package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.repository.TmAgentRepository;
import com.bassi.tmapp.service.criteria.TmAgentCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TmAgent} entities in the database.
 * The main input is a {@link TmAgentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TmAgent} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TmAgentQueryService extends QueryService<TmAgent> {

    private static final Logger LOG = LoggerFactory.getLogger(TmAgentQueryService.class);

    private final TmAgentRepository tmAgentRepository;

    public TmAgentQueryService(TmAgentRepository tmAgentRepository) {
        this.tmAgentRepository = tmAgentRepository;
    }

    /**
     * Return a {@link Page} of {@link TmAgent} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TmAgent> findByCriteria(TmAgentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TmAgent> specification = createSpecification(criteria);
        return tmAgentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TmAgentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TmAgent> specification = createSpecification(criteria);
        return tmAgentRepository.count(specification);
    }

    /**
     * Function to convert {@link TmAgentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TmAgent> createSpecification(TmAgentCriteria criteria) {
        Specification<TmAgent> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TmAgent_.id),
                buildStringSpecification(criteria.getFullName(), TmAgent_.fullName),
                buildStringSpecification(criteria.getAddress(), TmAgent_.address),
                buildRangeSpecification(criteria.getCreatedDate(), TmAgent_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), TmAgent_.modifiedDate),
                buildSpecification(criteria.getDeleted(), TmAgent_.deleted),
                buildStringSpecification(criteria.getCompanyName(), TmAgent_.companyName),
                buildStringSpecification(criteria.getAgentCode(), TmAgent_.agentCode),
                buildStringSpecification(criteria.getEmail(), TmAgent_.email)
            );
        }
        return specification;
    }
}
