package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.service.dto.StatusCountDTO;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trademark entity.
 *
 * When extending this class, extend TrademarkRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TrademarkRepository
    extends TrademarkRepositoryWithBagRelationships, JpaRepository<Trademark, Long>, JpaSpecificationExecutor<Trademark> {
    default Optional<Trademark> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Trademark> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Trademark> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Trademark> findByUser(UserProfile user);

    @Query(
        value = "SELECT new com.bassi.tmapp.service.dto.StatusCountDTO(COALESCE(CAST(tm.trademarkStatus AS string), 'UNKNOWN') , CAST(COUNT(tm.id) AS long)) FROM Trademark tm WHERE tm.user = ?1 AND tm.deleted = false GROUP BY tm.trademarkStatus"
    )
    List<StatusCountDTO> countByUserGroupByStatus(UserProfile userProfile);

    @Query(value = "SELECT tm FROM Trademark tm WHERE tm.user= ?1 AND deleted=false ORDER BY createdDate LIMIT 5")
    List<Trademark> findRecentTrademarkApplications(UserProfile user);
}
