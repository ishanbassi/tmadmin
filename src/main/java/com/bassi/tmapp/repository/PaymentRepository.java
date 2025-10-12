package com.bassi.tmapp.repository;

import com.bassi.tmapp.domain.Payment;
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
}
