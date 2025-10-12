package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.UserProfile;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT up FROM userProfiel up WHERE  up.user.login= ?1")
    Optional<UserProfile> findByUserEmail(String userLogin);
}
