package com.bassi.tmapp.repository.extended;

import com.bassi.tmapp.domain.PublishedTmPhonetics;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PublishedTmPhonetics entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PublishedTmPhoneticsRepositoryExtended extends JpaRepository<PublishedTmPhonetics, Long> {
}
