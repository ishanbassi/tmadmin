package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Trademark;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class TrademarkRepositoryWithBagRelationshipsImpl implements TrademarkRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String TRADEMARKS_PARAMETER = "trademarks";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Trademark> fetchBagRelationships(Optional<Trademark> trademark) {
        return trademark.map(this::fetchTrademarkClasses);
    }

    @Override
    public Page<Trademark> fetchBagRelationships(Page<Trademark> trademarks) {
        return new PageImpl<>(fetchBagRelationships(trademarks.getContent()), trademarks.getPageable(), trademarks.getTotalElements());
    }

    @Override
    public List<Trademark> fetchBagRelationships(List<Trademark> trademarks) {
        return Optional.of(trademarks).map(this::fetchTrademarkClasses).orElse(Collections.emptyList());
    }

    Trademark fetchTrademarkClasses(Trademark result) {
        return entityManager
            .createQuery(
                "select trademark from Trademark trademark left join fetch trademark.trademarkClasses where trademark.id = :id",
                Trademark.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Trademark> fetchTrademarkClasses(List<Trademark> trademarks) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, trademarks.size()).forEach(index -> order.put(trademarks.get(index).getId(), index));
        List<Trademark> result = entityManager
            .createQuery(
                "select trademark from Trademark trademark left join fetch trademark.trademarkClasses where trademark in :trademarks",
                Trademark.class
            )
            .setParameter(TRADEMARKS_PARAMETER, trademarks)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
