package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trademark entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkRepository extends JpaRepository<Trademark, Long> {}
