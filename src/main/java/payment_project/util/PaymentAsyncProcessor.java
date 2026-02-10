package payment_project.util;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;

import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentAsyncProcessor {
    private final PaymentRepository paymentRepository;

    @Async
    public void process(UUID paymentId) {
        try {
            Payment payment = paymentRepository.findById(paymentId)
                    .orElseThrow();

            payment.setStatus(Status.PENDING);
            paymentRepository.save(payment);

            //imitation of taking money from wallet
            Thread.sleep(8000);

            payment.setStatus(Status.SUCCESS);
            paymentRepository.save(payment);
        }catch (Exception e) {
            Payment payment = paymentRepository.findById(paymentId).orElseThrow();
            payment.setStatus(Status.FAILED);
            paymentRepository.save(payment);
        }
    }
}
