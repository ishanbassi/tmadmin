package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.UserEventsTracking;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserEventsTracking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserEventsTrackingRepository extends JpaRepository<UserEventsTracking, Long> {}
