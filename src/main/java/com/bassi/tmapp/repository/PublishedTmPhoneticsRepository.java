package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.PublishedTmPhonetics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PublishedTmPhonetics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublishedTmPhoneticsRepository extends JpaRepository<PublishedTmPhonetics, Long> {}
