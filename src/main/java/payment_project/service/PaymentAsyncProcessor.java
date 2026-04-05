package payment_project.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.events.PaymentFailedEvent;
import payment_project.events.PaymentSucceededEvent;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentAsyncProcessor {
    private final PaymentRepository paymentRepository;
    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher publisher;

    @Async
    @Transactional
    public void process(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        try {
            paymentRepository.updateStatus(paymentId, Status.PENDING);

            int updated = walletRepository.withdraw(
                    payment.getUserId(),
                    payment.getAmount()
            );

            if (updated == 0) {
                paymentRepository.updateStatus(paymentId, Status.FAILED);

                publisher.publishEvent(new PaymentFailedEvent(
                        payment.getId(), payment.getUserId(), payment.getAmount()
                ));
                return;
            }

            paymentRepository.updateStatus(paymentId, Status.SUCCESS);

            publisher.publishEvent(new PaymentSucceededEvent(
                    payment.getId(), payment.getUserId(), payment.getAmount()
            ));
        } catch (Exception e) {
            paymentRepository.updateStatus(paymentId, Status.FAILED);
            log.error("Payment failed {}", paymentId, e);
        }
    }
}