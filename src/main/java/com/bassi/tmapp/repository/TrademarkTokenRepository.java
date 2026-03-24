package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
import com.bassi.tmapp.service.dto.PartialTrademarkTokenDto;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrademarkToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkTokenRepository extends JpaRepository<TrademarkToken, Long> {
    List<TrademarkToken> findByTrademark(Trademark tm);

    List<TrademarkToken> findByTrademarkAndTokenType(Trademark tm, TrademarkTokenType core);

    @Query(
        """
            SELECT new com.bassi.tmapp.service.dto.PartialTrademarkTokenDto(
                t.tokenText,
                t.tokenType,
                t.position,
                t.trademark.id
            )
            FROM TrademarkToken t
            WHERE t.trademark.id IN :tmIds
        """
    )
    List<PartialTrademarkTokenDto> findByTrademarkIds(@Param("tmIds") Set<Long> tmIds);

    @Modifying
    @Query(value = "DELETE FROM TrademarkToken tt WHERE  tt.trademark.id = ?1")
    void deleteByTrademark(Long trademarId);

    @Modifying
    @Query(value = "DELETE FROM TrademarkToken tt WHERE  tt.trademark.journalNo = ?1")
    void deleteByTrademarkJournal(Integer journalNo);
}
