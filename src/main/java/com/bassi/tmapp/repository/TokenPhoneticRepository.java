package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TokenPhonetic;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TokenPhonetic entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TokenPhoneticRepository extends JpaRepository<TokenPhonetic, Long> {}
