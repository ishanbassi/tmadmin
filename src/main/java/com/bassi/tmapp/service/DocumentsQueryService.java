package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.*; // for static metamodels
import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.repository.DocumentsRepository;
import com.bassi.tmapp.service.criteria.DocumentsCriteria;
import com.bassi.tmapp.service.dto.DocumentsDTO;
import com.bassi.tmapp.service.mapper.DocumentsMapper;
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
 * Service for executing complex queries for {@link Documents} entities in the database.
 * The main input is a {@link DocumentsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DocumentsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DocumentsQueryService extends QueryService<Documents> {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentsQueryService.class);

    private final DocumentsRepository documentsRepository;

    private final DocumentsMapper documentsMapper;

    public DocumentsQueryService(DocumentsRepository documentsRepository, DocumentsMapper documentsMapper) {
        this.documentsRepository = documentsRepository;
        this.documentsMapper = documentsMapper;
    }

    /**
     * Return a {@link Page} of {@link DocumentsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentsDTO> findByCriteria(DocumentsCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Documents> specification = createSpecification(criteria);
        return documentsRepository.findAll(specification, page).map(documentsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DocumentsCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Documents> specification = createSpecification(criteria);
        return documentsRepository.count(specification);
    }

    /**
     * Function to convert {@link DocumentsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Documents> createSpecification(DocumentsCriteria criteria) {
        Specification<Documents> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Documents_.id),
                buildSpecification(criteria.getDocumentType(), Documents_.documentType),
                buildStringSpecification(criteria.getFileContentType(), Documents_.fileContentType),
                buildStringSpecification(criteria.getFileName(), Documents_.fileName),
                buildStringSpecification(criteria.getFileUrl(), Documents_.fileUrl),
                buildRangeSpecification(criteria.getCreatedDate(), Documents_.createdDate),
                buildRangeSpecification(criteria.getModifiedDate(), Documents_.modifiedDate),
                buildSpecification(criteria.getDeleted(), Documents_.deleted),
                buildSpecification(criteria.getStatus(), Documents_.status),
                buildSpecification(criteria.getTrademarkId(), root -> root.join(Documents_.trademark, JoinType.LEFT).get(Trademark_.id)),
                buildSpecification(criteria.getUserProfileId(), root ->
                    root.join(Documents_.userProfile, JoinType.LEFT).get(UserProfile_.id)
                )
            );
        }
        return specification;
    }
}
