package payment_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findByStatusInAndCreatedAtBefore(Collection<Status> statuses, Instant createdAtBefore);
}

