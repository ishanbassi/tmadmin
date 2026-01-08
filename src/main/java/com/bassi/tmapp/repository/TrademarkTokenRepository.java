package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TrademarkToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrademarkToken entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkTokenRepository extends JpaRepository<TrademarkToken, Long> {}
