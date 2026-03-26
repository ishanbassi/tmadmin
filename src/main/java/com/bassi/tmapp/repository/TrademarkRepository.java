package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.PublishedTm;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import com.bassi.tmapp.domain.enumeration.TrademarkSource;
import com.bassi.tmapp.service.dto.StatusCountDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import com.bassi.tmapp.service.dto.TrademarkSimilarityCandidateDto;
import com.bassi.tmapp.service.dto.TrademarkSimilarityCandidateWithPubTmDto;
import com.bassi.tmapp.service.dto.TrademarkSuggestionDto;
import com.bassi.tmapp.service.dto.TrademarkSuggestionInterface;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trademark entity.
 *
 * When extending this class, extend TrademarkRepositoryWithBagRelationships
 * too. For more information refer to
 * https://github.com/jhipster/generator-jhipster/issues/17990.
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
        value = """
        WITH client_phonetics AS (
            SELECT t.id AS client_id, t.tm_class, tp.phonetic_code
            FROM trademark t
            JOIN trademark_token tt ON tt.trademark_id = t.id
            JOIN token_phonetic tp ON tp.trademark_token_id = tt.id
            WHERE t.source = 'EXCEL' AND tt.token_type = 'CORE'
        ),
        pub_phonetics AS (
            SELECT t.id AS pub_id, t.tm_class, tp.phonetic_code
            FROM trademark t
            JOIN trademark_token tt ON tt.trademark_id = t.id
            JOIN token_phonetic tp ON tp.trademark_token_id = tt.id
            WHERE t.source = 'JOURNAL_PUBLICATION' AND t.journal_no = ?1 AND tt.token_type = 'CORE'
        ),
        distinct_pairs AS (
            SELECT DISTINCT cp.client_id, pp.pub_id
            FROM client_phonetics cp
            JOIN pub_phonetics pp
                ON pp.phonetic_code = cp.phonetic_code
               AND pp.tm_class = cp.tm_class
        )
        SELECT
            dp.client_id, tc.name,tc.normalized_name,
            dp.pub_id,tp.name, tp.normalized_name, tp.application_no, tp.tm_class,tp.img_url, tp.proprietor_name, tp.proprietor_address, tp.details
        FROM distinct_pairs dp
        JOIN trademark tc ON tc.id = dp.client_id
        JOIN trademark tp ON tp.id = dp.pub_id;
        """,
        nativeQuery = true
    )
    List<TrademarkSimilarityCandidateWithPubTmDto> findAllCandidatePairs(int journalNo);

    @Query(
        value = """
            SELECT tm.id, tm.normalized_name, tm.name, tm.application_no, tm.tm_class, tm.img_url, tm.proprietor_name, tm.application_date, tm.details, tm.type
            FROM trademark tm
            JOIN trademark_token tt_pub
                 ON tt_pub.trademark_id = tm.id
            LEFT JOIN token_phonetic tp_pub
                 ON tp_pub.trademark_token_id = tt_pub.id
            WHERE
              (
                    tp_pub.phonetic_code IN (:phonetics)
                    OR tm.normalized_name LIKE CONCAT('%', :normalizedName, '%')
                  )
        """,
        nativeQuery = true
    )
    List<TrademarkSimilarityCandidateDto> findCandidatePublishedIds(
        @Param("phonetics") List<String> phonetics,
        @Param("normalizedName") String normalizedName
    );

    @Query(
        value = """
            SELECT t.name, t.details, t.application_no, t.tm_class, t.img_url, t.type,t.proprietor_name, t.application_date::date
            FROM trademark t
            WHERE t.normalized_name LIKE CONCAT('%', :prefix, '%')
            ORDER BY
              CASE
                  WHEN t.normalized_name = :prefix THEN 1
                  WHEN t.normalized_name LIKE CONCAT(:prefix, '%') THEN 2
                  WHEN t.normalized_name LIKE CONCAT('% ', :prefix, '%') THEN 3
                  ELSE 4
              END,
              t.normalized_name
            LIMIT :limit
        """,
        nativeQuery = true
    )
    List<TrademarkSuggestionDto> findLiveSuggestionsByLimit(@Param("prefix") String prefix, @Param("limit") Integer limit);

    @Query(value = "SELECT DISTINCT t.journalNo FROM Trademark t ORDER BY t.journalNo DESC")
    List<Integer> getJournalNumbers();

    @Query(value = "SELECT DISTINCT t.journalNo FROM Trademark t ORDER BY t.journalNo DESC LIMIT 1")
    Integer getLatestJournalNumber();

    //	@Query(value="SELECT tm from Trademark left join fetch tm.trademarkClasses left join fetch tm.documents tm WHERE tm.applicationNo = ?1  ORDER BY tm.applicationNo LIMIT 1")
    Optional<Trademark> findFirstByApplicationNoOrderById(Long appNo);

    @Query(
        value = "SELECT * FROM trademark tm WHERE tm.journal_no= ?1 AND normalized_name is null AND name is NOT NULL",
        nativeQuery = true
    )
    List<Trademark> findByJournalNoAndNullNormalizedName(Integer journalNo);

    @Query(value = "SELECT tm FROM Trademark tm WHERE tm.name IS NULL AND tm.journalNo = ?1")
    List<Trademark> findTrademarksWhereNameIsNull(Integer journalNo);

    @Query(value = "SELECT tm FROM Trademark tm WHERE tm.journalNo = ?1")
    List<Trademark> findTrademarksByJournalNo(Integer journalNo);

    @Query(value = "SELECT distinct tm.journalNo FROM Trademark tm where tm.name is null order by tm.journalNo desc limit 1")
    Integer findLatestJournalNoWithMissingData();

    long countByJournalNoAndSource(int journalNo, TrademarkSource source);

    @Query(
        """
            SELECT t
            FROM Trademark t
            WHERE t.journalNo = :journalNo
              AND t.id > :lastId
            ORDER BY t.id ASC
        """
    )
    List<Trademark> findByJournalNoAndIdGreaterThan(@Param("journalNo") Integer journalNo, @Param("lastId") Long lastId, Pageable pageable);
}
