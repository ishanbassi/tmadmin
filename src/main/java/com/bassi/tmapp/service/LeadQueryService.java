package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.repository.LeadRepository;
import com.bassi.tmapp.service.criteria.LeadCriteria;
import com.bassi.tmapp.service.dto.LeadDTO;
import com.bassi.tmapp.service.mapper.LeadMapper;
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
 * It returns a {@link List} of {@link LeadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeadQueryService extends QueryService<Lead> {

    private static final Logger LOG = LoggerFactory.getLogger(LeadQueryService.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    public LeadQueryService(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    /**
     * Return a {@link List} of {@link LeadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeadDTO> findByCriteria(LeadCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadMapper.toDto(leadRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeadCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
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
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Lead_.id),
                buildStringSpecification(criteria.getFullName(), Lead_.fullName),
                buildStringSpecification(criteria.getPhoneNumber(), Lead_.phoneNumber),
                buildStringSpecification(criteria.getEmail(), Lead_.email),
                buildStringSpecification(criteria.getCity(), Lead_.city),
                buildStringSpecification(criteria.getBrandName(), Lead_.brandName),
                buildStringSpecification(criteria.getSelectedPackage(), Lead_.selectedPackage),
                buildRangeSpecification(criteria.getTmClass(), Lead_.tmClass),
                buildStringSpecification(criteria.getComments(), Lead_.comments),
                buildSpecification(criteria.getContactMethod(), Lead_.contactMethod),
                buildRangeSpecification(criteria.getCreatedDate(), Lead_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), Lead_.modifiedDate),
                buildSpecification(criteria.getDeleted(), Lead_.deleted),
                buildSpecification(criteria.getStatus(), Lead_.status),
                buildStringSpecification(criteria.getLeadSource(), Lead_.leadSource),
                buildSpecification(criteria.getAssignedToId(), root -> root.join(Lead_.assignedTo, JoinType.LEFT).get(Employee_.id))
            );
        }
        return specification;
    }
}
