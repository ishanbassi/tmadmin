package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Lead;
import com.bassi.tmapp.domain.TmAgent;
import com.bassi.tmapp.domain.enumeration.LeadStatus;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    @Query(value = "SELECT l FROM Lead l WHERE l.leadSource=?1 and l.status = ?2 and l.email IS NOT NULL")
    public List<Lead> findByLeadSourceAndStatus(String leadSource, LeadStatus leadStatus);
}
