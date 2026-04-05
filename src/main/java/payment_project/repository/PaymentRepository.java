package payment_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import payment_project.entity.Payment;
import payment_project.entity.dto.PaymentInfo;
import payment_project.entity.enums.Status;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByStatusInAndCreatedAtBefore(Collection<Status> statuses, Instant createdAtBefore);

    @Modifying
    @Query("""
                UPDATE Payment p
                SET p.status = :status
                WHERE p.id = :id
            """)
    void updateStatus(UUID id, Status status);

    @Query("""
                SELECT p.status FROM Payment p WHERE p.id = :id
            """)
    Status getStatus(UUID id);

    @Query("""
                SELECT new payment_project.entity.dto.PaymentInfo(p.userId, p.amount)
                FROM Payment p
                WHERE p.id = :id
            """)
    PaymentInfo getPaymentInfo(UUID id);
}

