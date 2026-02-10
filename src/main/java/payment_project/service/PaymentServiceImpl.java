package payment_project.service;

import payment_project.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;

import java.time.Instant;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    // Creating new payment request into db
    @Transactional
    @Override
    public Payment createPayment(CreatePaymentRequest request) {
        return paymentRepository.findByIdempotencyKey(request.idempotencyKey())
                .orElseGet(() -> {
                    Payment payment = new Payment();
                    payment.setIdempotencyKey(request.idempotencyKey());
                    payment.setUserId(request.userId());
                    payment.setAmount(request.amount());
                    payment.setStatus(Status.NEW);
                    payment.setCreatedAt(Instant.now());

                    return paymentRepository.save(payment);
                });
    }
}
