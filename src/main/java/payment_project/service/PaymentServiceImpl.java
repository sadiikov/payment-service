package payment_project.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataIntegrityViolationException;
import payment_project.entity.dto.CreatePaymentRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import payment_project.entity.dto.PaymentResponse;
import payment_project.entity.Payment;
import payment_project.entity.Wallet;
import payment_project.entity.enums.Status;
import payment_project.events.PaymentCreatedEvent;
import payment_project.events.PaymentRefundedEvent;
import payment_project.repository.PaymentRepository;
import payment_project.repository.WalletRepository;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentAsyncProcessor asyncProcessor;
    private final WalletRepository walletRepository;
    private final ApplicationEventPublisher publisher;

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

                    try {
                        return paymentRepository.save(p);
                    }catch (DataIntegrityViolationException e){
                        return paymentRepository
                                .findByIdempotencyKey(request.idempotencyKey())
                                .orElseThrow(() -> new DataIntegrityViolationException(e.getMessage()));
                    }
                });

        paymentRepository.flush();

        asyncProcessor.process(payment.getId());

        publisher.publishEvent(
                new PaymentCreatedEvent(payment.getId(), payment.getUserId(), payment.getAmount())
        );

        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }

    @Override
    public PaymentResponse getPaymentById(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
        return new PaymentResponse(payment.getAmount(), payment.getStatus(), payment.getCreatedAt());
    }

    @Transactional
    @Override
    public void refundPayment(UUID paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));

        if (payment.getStatus() == Status.REFUNDED) {
            throw new OptimisticLockException("Payment already refunded");
        }

        if(payment.getStatus() != Status.SUCCESS) {
            throw new IllegalStateException("Only successful payment can be refunded");
        }

        Wallet wallet = walletRepository.findById(payment.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Wallet not found"));

        try {
            wallet.setBalance(wallet.getBalance() + payment.getAmount());
            walletRepository.save(wallet);
        }catch (OptimisticLockException e){
            wallet.setBalance(wallet.getBalance() - payment.getAmount());
            walletRepository.save(wallet);
        }

        publisher.publishEvent(
                new PaymentRefundedEvent(payment.getId(), payment.getUserId(), payment.getAmount())
        );

        payment.setStatus(Status.REFUNDED);
        paymentRepository.save(payment);
    }
}
