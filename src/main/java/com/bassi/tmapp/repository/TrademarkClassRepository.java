package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TrademarkClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrademarkClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkClassRepository extends JpaRepository<TrademarkClass, Long>, JpaSpecificationExecutor<TrademarkClass> {}
