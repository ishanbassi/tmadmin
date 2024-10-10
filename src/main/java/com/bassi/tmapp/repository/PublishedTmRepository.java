package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.PublishedTm;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.bassi.tmapp.domain.PublishedTm;

/**
 * Spring Data JPA repository for the PublishedTm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublishedTmRepository extends JpaRepository<PublishedTm, Long>, JpaSpecificationExecutor<PublishedTm> {}
