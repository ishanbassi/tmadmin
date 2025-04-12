package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.criteria.LeadCriteria;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Lead} entities in the database.
 * The main input is a {@link LeadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Lead} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeadQueryService extends QueryService<Lead> {

    private static final Logger log = LoggerFactory.getLogger(LeadQueryService.class);

    private final LeadRepository leadRepository;

    public LeadQueryService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    /**
     * Return a {@link List} of {@link Lead} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Lead> findByCriteria(LeadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.findAll(specification);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.count(specification);
    }

    /**
     * Function to convert {@link LeadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lead> createSpecification(LeadCriteria criteria) {
        Specification<Lead> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lead_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Lead_.fullName));
            }
            if (criteria.getPhoneNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhoneNumber(), Lead_.phoneNumber));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Lead_.email));
            }
            if (criteria.getCity() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCity(), Lead_.city));
            }
            if (criteria.getBrandName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBrandName(), Lead_.brandName));
            }
            if (criteria.getSelectedPackage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSelectedPackage(), Lead_.selectedPackage));
            }
            if (criteria.getTmClass() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTmClass(), Lead_.tmClass));
            }
            if (criteria.getComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComments(), Lead_.comments));
            }
            if (criteria.getContactMethod() != null) {
                specification = specification.and(buildSpecification(criteria.getContactMethod(), Lead_.contactMethod));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Lead_.createdDate));
            }
            if (criteria.getModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedDate(), Lead_.modifiedDate));
            }
            if (criteria.getDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getDeleted(), Lead_.deleted));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Lead_.status));
            }
            if (criteria.getLeadSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeadSource(), Lead_.leadSource));
            }
            if (criteria.getAssignedToId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAssignedToId(), root -> root.join(Lead_.assignedTo, JoinType.LEFT).get(Employee_.id))
                );
            }
        }
        return specification;
    }
}
