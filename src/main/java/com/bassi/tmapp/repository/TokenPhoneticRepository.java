package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TokenPhonetic;
import com.bassi.tmapp.domain.TrademarkToken;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TokenPhonetic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenPhoneticRepository extends JpaRepository<TokenPhonetic, Long> {
    List<TokenPhonetic> findByTrademarkToken(TrademarkToken token);
}
