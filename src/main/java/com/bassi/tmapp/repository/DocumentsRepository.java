package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Documents;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.enumeration.DocumentType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Documents entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentsRepository extends JpaRepository<Documents, Long> {
    @Query(value = "SELECT d from Documents d WHERE d.trademark = ?1 AND d.documentType = ?2 ORDER BY d.createdDate LIMIT 1")
    Optional<Documents> findByTrademark(Trademark trademark, DocumentType documentType);

    List<Documents> findByTrademark(Trademark trademark);
}
