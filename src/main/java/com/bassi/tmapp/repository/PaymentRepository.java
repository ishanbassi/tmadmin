package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Payment;
import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.PaymentDTO;
import com.bassi.tmapp.service.dto.TrademarkDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>, JpaSpecificationExecutor<Payment> {
    Optional<Payment> findPaymentByOrderId(String orderId);

    Optional<Payment> findPaymentByGatewayOrderId(String gatewayOrderId);

    List<Payment> findByTrademark(Trademark dto);

    List<Payment> findByTrademarkId(Long trademarkId);

    @Query(value = "SELECT p FROM Payment p WHERE p.trademark.id = ?1 AND LOWER(p.status) IN ?2")
    List<Payment> findByTrademarkIdAndStatusIn(Long trademarkId, List<String> paymentStatus);
}
