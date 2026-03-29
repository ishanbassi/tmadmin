package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.TrademarkTokenFrequency;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TrademarkTokenFrequency entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrademarkTokenFrequencyRepository extends JpaRepository<TrademarkTokenFrequency, Long> {
    @Query("SELECT tf.word FROM TrademarkTokenFrequency tf WHERE tf.word = ?1 ORDER BY tf.word LIMIT 1")
    String findByWord(String word);
}
