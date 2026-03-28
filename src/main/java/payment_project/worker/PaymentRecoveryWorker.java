package payment_project.worker;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;

import java.time.Instant;
import java.util.List;

@Component
@AllArgsConstructor
public class PaymentRecoveryWorker {
    private static final Logger log = LoggerFactory.getLogger(PaymentRecoveryWorker.class);
    private final PaymentRepository paymentRepository;

    @Scheduled(fixedDelay = 30000)
    public void recoveryStuckPayments() {
        Instant threshold = Instant.now().minusSeconds(600);

        List<Payment> stuckPayments =
                paymentRepository.findByStatusInAndCreatedAtBefore(
                        List.of(Status.NEW, Status.PENDING),
                        threshold
                );

        if (stuckPayments.isEmpty()) {
            return;
        }

        for (Payment payment : stuckPayments) {

            if (payment.getStatus() == Status.SUCCESS ||
                    payment.getStatus() == Status.FAILED) {
                continue;
            }

            payment.setStatus(Status.FAILED);

            log.warn("Payment {} has been successfully recovered", payment.getId());
        }

        paymentRepository.saveAll(stuckPayments);
    }
}
