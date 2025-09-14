package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TrademarkPlan;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrademarkPlan entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkPlanRepository extends JpaRepository<TrademarkPlan, Long> {}
