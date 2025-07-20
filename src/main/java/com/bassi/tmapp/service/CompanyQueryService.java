package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.Company;
import com.bassi.tmapp.repository.CompanyRepository;
import com.bassi.tmapp.service.criteria.CompanyCriteria;
import com.bassi.tmapp.service.dto.CompanyDTO;
import com.bassi.tmapp.service.mapper.CompanyMapper;
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
 * Service for executing complex queries for {@link Company} entities in the database.
 * The main input is a {@link CompanyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CompanyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompanyQueryService extends QueryService<Company> {

    private static final Logger LOG = LoggerFactory.getLogger(CompanyQueryService.class);

    private final CompanyRepository companyRepository;

    private final CompanyMapper companyMapper;

    public CompanyQueryService(CompanyRepository companyRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
    }

    /**
     * Return a {@link Page} of {@link CompanyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompanyDTO> findByCriteria(CompanyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.findAll(specification, page).map(companyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompanyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Company> specification = createSpecification(criteria);
        return companyRepository.count(specification);
    }

    /**
     * Function to convert {@link CompanyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Company> createSpecification(CompanyCriteria criteria) {
        Specification<Company> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Company_.id),
                buildStringSpecification(criteria.getType(), Company_.type),
                buildStringSpecification(criteria.getName(), Company_.name),
                buildStringSpecification(criteria.getCin(), Company_.cin),
                buildStringSpecification(criteria.getGstin(), Company_.gstin),
                buildStringSpecification(criteria.getNatureOfBusiness(), Company_.natureOfBusiness),
                buildStringSpecification(criteria.getAddress(), Company_.address),
                buildStringSpecification(criteria.getState(), Company_.state),
                buildStringSpecification(criteria.getPincode(), Company_.pincode),
                buildStringSpecification(criteria.getCity(), Company_.city),
                buildRangeSpecification(criteria.getCreatedDate(), Company_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), Company_.modifiedDate),
                buildSpecification(criteria.getDeleted(), Company_.deleted),
                buildSpecification(criteria.getUserId(), root -> root.join(Company_.user, JoinType.LEFT).get(UserProfile_.id))
            );
        }
        return specification;
    }
}
