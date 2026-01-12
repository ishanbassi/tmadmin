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

    @Query(
        value = " SELECT DISTINCT t_client.id   AS client_id, t_pub.id      AS published_id FROM trademark t_client " +
        "JOIN trademark_token tt_client ON tt_client.trademark_id = t_client.id JOIN token_phonetic tp_client ON " +
        "tp_client.trademark_token_id = tt_client.id JOIN token_phonetic tp_pub ON tp_pub.phonetic_code = tp_client.phonetic_code" +
        " JOIN trademark_token tt_pub ON tt_pub.id = tp_pub.trademark_token_id JOIN trademark t_pub ON t_pub.id = tt_pub.trademark_id " +
        "WHERE t_client.source = 'EXCEL' AND t_pub.source = 'JOURNAL_PUBLICATION' AND tt_client.token_type = 'CORE' AND t_client.tm_class = t_pub.tm_class AND t_client.id <> t_pub.tm_class " +
        "AND t_pub.journal_no = ?1 ",
        nativeQuery = true
    )
    List<Object[]> findAllCandidatePairs(int journalNo);
}
