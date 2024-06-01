package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Phonetics;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Phonetics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PhoneticsRepository extends JpaRepository<Phonetics, Long> {}
