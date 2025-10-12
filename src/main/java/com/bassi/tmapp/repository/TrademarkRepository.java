package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.domain.UserProfile;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Trademark entity.
 *
 * When extending this class, extend TrademarkRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TrademarkRepository
    extends TrademarkRepositoryWithBagRelationships, JpaRepository<Trademark, Long>, JpaSpecificationExecutor<Trademark> {
    default Optional<Trademark> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Trademark> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Trademark> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    List<Trademark> findByUser(UserProfile user);
}
