package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.TrademarkClass;
import com.bassi.tmapp.repository.TrademarkClassRepository;
import com.bassi.tmapp.service.criteria.TrademarkClassCriteria;
import com.bassi.tmapp.service.dto.TrademarkClassDTO;
import com.bassi.tmapp.service.mapper.TrademarkClassMapper;
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
 * Service for executing complex queries for {@link TrademarkClass} entities in the database.
 * The main input is a {@link TrademarkClassCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TrademarkClassDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TrademarkClassQueryService extends QueryService<TrademarkClass> {

    private static final Logger LOG = LoggerFactory.getLogger(TrademarkClassQueryService.class);

    private final TrademarkClassRepository trademarkClassRepository;

    private final TrademarkClassMapper trademarkClassMapper;

    public TrademarkClassQueryService(TrademarkClassRepository trademarkClassRepository, TrademarkClassMapper trademarkClassMapper) {
        this.trademarkClassRepository = trademarkClassRepository;
        this.trademarkClassMapper = trademarkClassMapper;
    }

    /**
     * Return a {@link Page} of {@link TrademarkClassDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TrademarkClassDTO> findByCriteria(TrademarkClassCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TrademarkClass> specification = createSpecification(criteria);
        return trademarkClassRepository.findAll(specification, page).map(trademarkClassMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TrademarkClassCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TrademarkClass> specification = createSpecification(criteria);
        return trademarkClassRepository.count(specification);
    }

    /**
     * Function to convert {@link TrademarkClassCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TrademarkClass> createSpecification(TrademarkClassCriteria criteria) {
        Specification<TrademarkClass> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TrademarkClass_.id),
                buildRangeSpecification(criteria.getCode(), TrademarkClass_.code),
                buildRangeSpecification(criteria.getTmClass(), TrademarkClass_.tmClass),
                buildStringSpecification(criteria.getKeyword(), TrademarkClass_.keyword),
                buildStringSpecification(criteria.getTitle(), TrademarkClass_.title),
                buildStringSpecification(criteria.getDescription(), TrademarkClass_.description),
                buildRangeSpecification(criteria.getCreatedDate(), TrademarkClass_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), TrademarkClass_.modifiedDate),
                buildSpecification(criteria.getDeleted(), TrademarkClass_.deleted),
                buildSpecification(criteria.getTrademarksId(), root ->
                    root.join(TrademarkClass_.trademarks, JoinType.LEFT).get(Trademark_.id)
                )
            );
        }
        return specification;
    }
}
