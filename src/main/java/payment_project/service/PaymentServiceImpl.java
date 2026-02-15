package payment_project.service;

import jakarta.persistence.EntityNotFoundException;
import payment_project.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.dto.PaymentResponse;
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
    public PaymentResponse createPayment(CreatePaymentRequest request) {
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

        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }
}
