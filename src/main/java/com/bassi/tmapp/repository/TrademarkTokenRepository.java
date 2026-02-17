package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.domain.enumeration.TrademarkTokenType;
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
        SELECT t
        FROM TrademarkToken t
        WHERE t.trademark.id IN :tmIds
        """
    )
    List<TrademarkToken> findByTrademarkIds(@Param("tmIds") Set<Long> tmIds);

    @Modifying
    @Query(value = "DELETE FROM TrademarkToken tt WHERE  tt.trademark.id = ?1")
    void deleteByTrademark(Long trademarId);
}
