package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.TrademarkToken;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TokenPhonetic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenPhoneticRepository extends JpaRepository<TokenPhonetic, Long> {
    List<TokenPhonetic> findByTrademarkToken(TrademarkToken token);

    @Query(
        """
        SELECT p
        FROM TokenPhonetic p
        JOIN p.trademarkToken tok
        WHERE tok.trademark.id IN :tmIds
        """
    )
    List<TokenPhonetic> findByTrademarkIds(@Param("tmIds") Set<Long> tmIds);
}
