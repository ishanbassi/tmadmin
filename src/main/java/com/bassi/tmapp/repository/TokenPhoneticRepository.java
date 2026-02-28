package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.TrademarkToken;
import com.bassi.tmapp.service.dto.PartialTokenPhoneticDto;
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
            SELECT new com.bassi.tmapp.service.dto.PartialTokenPhoneticDto(
                p.algorithm,
                p.phoneticCode,
                p.secondaryPhoneticCode,
                p.trademarkToken.trademark.id
            )
            FROM TokenPhonetic p
            WHERE p.trademarkToken.trademark.id IN :tmIds
        """
    )
    List<PartialTokenPhoneticDto> findByTrademarkIds(@Param("tmIds") Set<Long> tmIds);
}
