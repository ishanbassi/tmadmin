package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TmAgent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TmAgent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TmAgentRepository extends JpaRepository<TmAgent, Long> {}
