package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TrademarkRepositoryWithBagRelationships {
    Optional<Trademark> fetchBagRelationships(Optional<Trademark> trademark);

    List<Trademark> fetchBagRelationships(List<Trademark> trademarks);

    Page<Trademark> fetchBagRelationships(Page<Trademark> trademarks);
}
