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

    @Scheduled(fixedDelay = 6000)
    public void recoveryStuckPayments() {
        Instant threshold = Instant.now().minusSeconds(300);

        List<Payment> stuckPayments =
                paymentRepository.findByStatusAndCreatedAtBefore(
                        Status.PENDING,
                        threshold
                );

        for (Payment payment : stuckPayments) {
            payment.setStatus(Status.FAILED);
            paymentRepository.save(payment);

            log.info("Payment " + payment.getId() + " has been successfully recovered");
        }
    }
}
