package payment_project.service;

import org.springframework.scheduling.annotation.Async;
import payment_project.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.Payment;
import payment_project.entity.enums.Status;
import payment_project.repository.PaymentRepository;
import payment_project.util.PaymentAsyncProcessor;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAsyncProcessor asyncProcessor;

    // Creating new payment request into db
    @Transactional
    @Override
    public Payment createPayment(CreatePaymentRequest request) {
        Payment payment = paymentRepository.findByIdempotencyKey(request.idempotencyKey())
                .orElseGet(() -> {
                    Payment p = new Payment();
                    p.setIdempotencyKey(request.idempotencyKey());
                    p.setUserId(request.userId());
                    p.setAmount(request.amount());
                    p.setStatus(Status.NEW);
                    p.setCreatedAt(Instant.now());

                    return paymentRepository.save(p);
                });

        paymentRepository.flush();

        asyncProcessor.process(payment.getId());

        return payment;
    }

    @Override
    public Payment getPaymentById(UUID paymentId) {
        return paymentRepository.findById(paymentId).orElseThrow();
    }
}
